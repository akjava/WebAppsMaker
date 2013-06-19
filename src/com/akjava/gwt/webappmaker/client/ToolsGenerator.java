package com.akjava.gwt.webappmaker.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormFieldData;
import com.akjava.lib.common.tag.LabelAndValue;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


public class ToolsGenerator {
private FormData formData;
private String packageString;
public ToolsGenerator(FormData formData,String packageString){
	this.formData=formData;
	this.packageString=packageString;
}
public String generateToolsText(){
Joiner joiner=Joiner.on("\n").skipNulls();
String base=Bundles.INSTANCE.tools().getText();

Map<String,String> map=new HashMap<String, String>();
map.put("package", packageString);
map.put("dataClassName",formData.getClassName());

map.put("dateFormat", Internationals.getMessage("dateformat"));//base date format for label

//create toLabelMap
if(formData.getFormFieldDatas()!=null){
List<String> toLabelMapTexts=Lists.newArrayList(
Iterables.filter(
	     Lists.transform(formData.getFormFieldDatas(), getFormFieldDataToToLabelMapFunction()),
	     new NotEmpty()
	  )
	  );
Lists.transform(formData.getFormFieldDatas(), getFormFieldDataToToLabelMapFunction());
map.put("toLabelMap",joiner.join(toLabelMapTexts));
}





return TemplateUtils.createAdvancedText(base, map);
}

//TODO create StringPredicates
public class NotEmpty implements Predicate<String>{

	@Override
	public boolean apply(String value) {
		return value!=null && !value.isEmpty();
	}
	
}


public FileNameAndText createFileNameAndText(){
	String generatorText=this.generateToolsText();
	FileNameAndText ftext=new FileNameAndText();
	ftext.setName(formData.getClassName()+"Tools.java");
	ftext.setText(generatorText);
	return ftext;
}


/*
public enum ServletDataToToolsGeneratorFunction implements Function<ServletData,ToolsGenerator>{
	;

	@Override
	public ToolsGenerator apply(ServletData data) {
		return new ToolsGenerator(data);
	}
	
}
*/

public FormFieldDataToToLabelMapFunction getFormFieldDataToToLabelMapFunction(){
	return FormFieldDataToToLabelMapFunction.INSTANCE;
}
public enum FormFieldDataToToLabelMapFunction implements Function<FormFieldData,String>{
	INSTANCE
	;
	@Override
	public String apply(FormFieldData fdata) {
		if(fdata.getType()==FormFieldData.TYPE_CHECK){
			Map<String,String> map=new HashMap<String, String>();
			map.put("key", fdata.getKey());
			map.put("true_value", "on");
			map.put("false_value","");
			String template=Bundles.INSTANCE.tolabelmap_boolean().getText();
			List<LabelAndValue> options=fdata.getOptionValues();
			if(options!=null){
				if(options.size()>0){
					map.put("true_value",options.get(0).getPrintValue());
				}
				if(options.size()>1){
					map.put("false_value",options.get(1).getPrintValue());
				}
			}
			//true
			//false
			return TemplateUtils.createAdvancedText(template, map);
		}
		return "";
	}
	
}
}
