package com.akjava.gwt.webappmaker.client;

import java.util.List;

import com.akjava.lib.common.form.FormFieldData;

public class JDOCsvConverter {

	public static String convert(List<FormFieldData> datas){
	String result="";
	for(FormFieldData data:datas){
		String type=FormFieldData.getTypeByNumber(data.getType());
		result+=data.getKey()+","+getTypeAndOption(type)+"\n";
	}
	return result;
	}
	
	public static String getTypeAndOption(String type){
		if(type.equals("id")){
			return "Long,valueStrategy=IdGeneratorStrategy.IDENTITY";
		}else if(type.equals("text_long")){
			return "Text,";
		}else if(type.equals("check")){
			return "Boolean,";
		}else if(type.equals("select_single")||type.equals("select")){
			return "Long,";
		}else if(type.equals("select_multi")){
			return "List<Long>,";
		}else if(type.equals("create_date")||type.equals("modified_date")){
			return "Long,";
		}else if(type.equals("number")){
			return "Long,";
		}else{
			return "String,";
		}
	}

}
