package com.akjava.gwt.webappmaker.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.lib.common.form.FormData;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ServletWebXmlDataDto {
private ServletWebXmlDataDto(){}

public static ServletWebXmlDataToMapFunction getServletWebXmlDataToMapFunction(){
	return ServletWebXmlDataToMapFunction.INSTANCE;
}

public enum  ServletWebXmlDataToMapFunction implements Function<ServletWebXmlData,Map<String,String>>{
	INSTANCE;
	@Override
	public Map<String, String> apply(ServletWebXmlData data) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("name", data.getName());
		map.put("path", data.getPath());
		map.put("fullClassName", data.getFullClassName());
		return map;
	}	
}

}
