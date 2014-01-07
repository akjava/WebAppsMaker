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
		}else if(type.equals(FormFieldData.VALUE_TYPE_TEXT_LONG)||type.equals(FormFieldData.VALUE_TYPE_HIDDEN)){
			return "Text,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_CHECK)){
			return "Boolean,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_SELECT_SINGLE)||type.equals("select_single")){
			return "Long,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_SELECT_MULTI)){
			return "List<Long>,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_CREATE_DATE)||type.equals(FormFieldData.VALUE_TYPE_MODIFIED_DATE)){
			return "Long,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_NUMBER)||type.equals("long")){
			return "Long,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_INTEGER)){
			return "Integer,";
		}else if(type.equals(FormFieldData.VALUE_TYPE_POINT)||type.equals("double")){
			return "Double,";
		}else{//text,create_user,modified_user
			
			return "String,";
		}
	}

}
