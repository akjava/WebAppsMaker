package com.akjava.gwt.webappmaker.client;

import java.util.List;

import com.akjava.gwt.lib.client.GWTHTMLUtils;
import com.akjava.gwt.lib.client.widget.TabInputableTextArea;
import com.akjava.gwt.lib.client.widget.PasteValueReceiveArea;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormDataDto;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebAppsMaker implements EntryPoint {

	private TabInputableTextArea input;
	private TextArea output;
	private TextBox packageBox;

	public void onModuleLoad() {
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
		 packageBox.setWidth("100px");
		 packageBox.setText("com.akjava.gae.app1.");
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
	}

	protected void doConvert() {
		List<FormData> datas=FormDataDto.linesToFormData(input.getText());
		String out="";
		for(FormData data:datas){
			out+=data.toString();
			out+="\n";
		}
		output.setText(out);
	}
}
