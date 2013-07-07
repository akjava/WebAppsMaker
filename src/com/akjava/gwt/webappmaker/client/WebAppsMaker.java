package com.akjava.gwt.webappmaker.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.lib.client.GWTHTMLUtils;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageException;
import com.akjava.gwt.lib.client.widget.PasteValueReceiveArea;
import com.akjava.gwt.lib.client.widget.TabInputableTextArea;
import com.akjava.gwt.webappmaker.client.ServletDataDto.FormDataToAdminServletDataFunction;
import com.akjava.gwt.webappmaker.client.ServletDataDto.FormDataToMainServletDataFunction;
import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormDataDto;
import com.akjava.lib.common.functions.MapToAdvancedTemplatedTextFunction;
import com.akjava.lib.common.utils.TemplateUtils;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebAppsMaker implements EntryPoint {

	private TabInputableTextArea input;
	private TextArea output;
	private TextBox packageBox;
	private CellList<FileNameAndText> cellList;
	private TextArea fileTextArea;
	private VerticalPanel downloadLinks;

	private StorageControler storageControler=new StorageControler();
	private SingleSelectionModel<FileNameAndText> selectionModel;
	public void onModuleLoad() {
		
		String lang=GWTHTMLUtils.getInputValueById("gwtlang", "en");
		Internationals.lang=lang;
		
		HorizontalPanel root=new HorizontalPanel();
		VerticalPanel leftVertical=new VerticalPanel();
		root.add(leftVertical);
		GWTHTMLUtils.getPanelIfExist("gwtapp").add(root);
		
		PasteValueReceiveArea test=new PasteValueReceiveArea();
		 test.setStylePrimaryName("readonly");
		 test.setText("Click(Focus) & Paste Here");
		 leftVertical.add(test);
		 test.setSize("600px", "60px");
		 test.setFocus(true);
		 test.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				input.setText(event.getValue());
				doConvert();
			}
			 
		});
		 
		 
		 HorizontalPanel hpanel=new HorizontalPanel();
		 hpanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		 leftVertical.add(hpanel);
		 packageBox = new TextBox();
		 packageBox.setWidth("200px");
		 packageBox.setText(storageControler.getValue("packageValue", "com.akjava.gae.app1."));
		
		 hpanel.add(new Label("package"));
		 hpanel.add(packageBox);
		 
		 leftVertical.add(new Label("Csv(tab only)"));
		 input = new TabInputableTextArea();
		 input.setText(storageControler.getValue("inputCsv", ""));
		 //GWTHTMLUtils.setPlaceHolder(input, "className,package,servletName,path");
		 input.setSize("600px","200px");
		 leftVertical.add(input);
		 Button convert=new Button("Convert",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doConvert();
			}
		});
		 leftVertical.add(convert);
		 
		 output=new TextArea();
		 output.setSize("600px","200px");
		 output.setReadOnly(true);
		 leftVertical.add(output);
		 
		 VerticalPanel centerVertical=new VerticalPanel();
		 root.add(centerVertical);
		 
		 ScrollPanel scroll=new ScrollPanel();
		 scroll.setSize("300px", "800px");
		
		 FileNameAndTextCell cell=new FileNameAndTextCell();
		 cellList = new CellList<FileNameAndText>(cell);
		 cellList.setPageSize(100);
		 scroll.setWidget(cellList);
		 centerVertical.add(scroll);
		 
		 selectionModel = new SingleSelectionModel<FileNameAndText>();
		 cellList.setSelectionModel(selectionModel);
		 selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				FileNameAndText select=selectionModel.getSelectedObject();
				if(select!=null){
				fileTextArea.setText(select.getText());
				downloadLinks.clear();
				downloadLinks.add(select.createDownloadLink("Download"));
				}else{
					fileTextArea.setText("");
					downloadLinks.clear();
				}
			}
		});
		 VerticalPanel rightVertical=new VerticalPanel();
		 root.add(rightVertical);
		 downloadLinks = new VerticalPanel();
		 rightVertical.add(downloadLinks);
		 fileTextArea = new TextArea();
		 fileTextArea.setReadOnly(true);
		 fileTextArea.setSize("800px", "800px");
		 rightVertical.add(fileTextArea);
		 
	}
	public String getPackage(){
		return packageBox.getText();
	}

	
	
	protected void doConvert() {
		cellList.getSelectionModel().setSelected(selectionModel.getSelectedObject(), false);
		List<FormData> datas=FormDataDto.linesToFormData(input.getText());
		//debug text
		String out="";
		for(FormData data:datas){
			out+=data.toString();
			out+="\n";
		}
		output.setText(out);
		
		List<FileNameAndText> files=new ArrayList<FileNameAndText>();
		List<ServletData> sdatas=Lists.newArrayList();
		
		
		
		for(FormData fdata:datas){
			//Tools.java
			ToolsGenerator toolsGenerator=new ToolsGenerator(fdata,ValuesUtils.chomp(packageBox.getText()));
			files.add(toolsGenerator.createFileNameAndText());
			//Modifier.java
			ModifierGenerator modifierGenerator=new ModifierGenerator(fdata,ValuesUtils.chomp(packageBox.getText()));
			files.add(modifierGenerator.createFileNameAndText());
			//Validator.java
			ValidatorGenerator validatorGenerator=new ValidatorGenerator(fdata,ValuesUtils.chomp(packageBox.getText()));
			files.add(validatorGenerator.createFileNameAndText());
			
			
			List<ServletData> sdata=new FormDataToMainServletDataFunction(getPackage()).apply(fdata);
			Iterables.addAll(sdatas, sdata);
			
			List<FileNameAndText> mainServletFiles=Lists.transform(sdata, new ServletDataDto.ServletDataToServletFileFunction());
			Iterables.addAll(files, mainServletFiles);
			
			
			List<List<FileNameAndText>> templateFiles=Lists.transform(sdata, new ServletDataDto.ServletDataToTemplateFileFunction());
			for(List<FileNameAndText> templates:templateFiles){
				Iterables.addAll(files, templates);
			}
			//TODO list always contain add link,however usually not need it.
			//TODO option allow add,edit delete
				
		}
		
		
		//admin use another mainbase
		for(FormData fdata:datas){
			List<ServletData> sdata=new FormDataToAdminServletDataFunction(getPackage()).apply(fdata);
			Iterables.addAll(sdatas, sdata);
			
			List<FileNameAndText> mainServletFiles=Lists.transform(sdata, new ServletDataDto.ServletDataToServletFileFunction());
			Iterables.addAll(files, mainServletFiles);
			
			
			List<List<FileNameAndText>> templateFiles=Lists.transform(sdata, new ServletDataDto.ServletDataToTemplateFileFunction());
			for(List<FileNameAndText> templates:templateFiles){
				Iterables.addAll(files, templates);
			}
		}
		
		files.add(FileNameAndTextGenerator.generateSharedUtils(getPackage()));
		
		//Top
		files.add(FileNameAndTextGenerator.generateTopServlet(getPackage()));
		//template
		files.add(FileNameAndTextGenerator.generateTopTemplate(datas));
		
		//AdminTop
		files.add(FileNameAndTextGenerator.generateAdminTopServlet(getPackage()));
		//template
		files.add(FileNameAndTextGenerator.generateAdminTopTemplate(datas));
		
		
		//mainBase
		files.add(FileNameAndTextGenerator.generateMainBase(datas));
		//adminBase
		files.add(FileNameAndTextGenerator.generateAdminBase(datas));
		
		
		//web.xml
		String template=Bundles.INSTANCE.servlet().getText();
		
		//create list for add
		List<ServletWebXmlData> xmlDatas=Lists.newArrayList(Collections2.transform(sdatas, ServletDataDto.getServletDataToServletWebXmlFunction()));
		
		//add top
		ServletWebXmlData topData=new ServletWebXmlData();
		topData.setName("Top");
		topData.setFullClassName(getPackage()+"main.TopServlet");
		topData.setPath("/index.html");
		xmlDatas.add(topData);
		
		//add admin top
		ServletWebXmlData adminTopData=new ServletWebXmlData();
		adminTopData.setName("AdminTop");
		adminTopData.setFullClassName(getPackage()+"admin.AdminTopServlet");
		adminTopData.setPath("/admin/index.html");
		xmlDatas.add(adminTopData);
		
		List<Map<String,String>> xmlTextMaps=Lists.transform(xmlDatas, ServletWebXmlDataDto.getServletWebXmlDataToMapFunction());
		List<String> webXmls=Lists.transform(xmlTextMaps, new MapToAdvancedTemplatedTextFunction(template));
		String webXmlTemplate=Bundles.INSTANCE.web().getText();
		
		Map<String,String> map=new HashMap<String, String>();
		map.put("welcome", "index.html");
		map.put("servlets", Joiner.on("\n").join(webXmls));
		files.add(new FileNameAndText("web.xml",TemplateUtils.createAdvancedText(webXmlTemplate, map)));
		
		cellList.setRowData(0, files);
		try {
			storageControler.setValue("packageValue",packageBox.getText());
			storageControler.setValue("inputCsv", input.getText());
		} catch (StorageException e) {
			LogUtils.log(e.getMessage());
			e.printStackTrace();
		}
	}
}
