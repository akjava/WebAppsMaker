package com.akjava.gwt.webappmaker.client;

import java.util.ArrayList;
import java.util.List;

import com.akjava.gwt.lib.client.GWTHTMLUtils;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageException;
import com.akjava.gwt.lib.client.widget.PasteValueReceiveArea;
import com.akjava.gwt.lib.client.widget.TabInputableTextArea;
import com.akjava.gwt.webappmaker.client.ServletDataDto.FormDataToMainServletDataFunction;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormDataDto;
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
		
		for(FormData fdata:datas){
			List<ServletData> sdata=new FormDataToMainServletDataFunction(getPackage()).apply(fdata);
			List<FileNameAndText> mainServletFiles=Lists.transform(sdata, new ServletDataDto.ServletDataToServletFileFunction());
			Iterables.addAll(files, mainServletFiles);
			//TODO get admin
			
		}
		//TODO create template
		
		cellList.setRowData(0, files);
		try {
			storageControler.setValue("packageValue",packageBox.getText());
		} catch (StorageException e) {
			LogUtils.log(e.getMessage());
			e.printStackTrace();
		}
	}
}
