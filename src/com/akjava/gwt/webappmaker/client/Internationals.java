package com.akjava.gwt.webappmaker.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.akjava.gwt.lib.client.ValueUtils;
import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.utils.CSVUtils;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.gwt.core.shared.GWT;

public class Internationals {
public static String lang="en";

private static Map<String,String> japanese;
private static Map<String,String> english;
public static void init(){
	japanese=CSVUtils.csvTextToMap('\t', Bundles.INSTANCE.japanese().getText());
	english=CSVUtils.csvTextToMap('\t', Bundles.INSTANCE.english().getText());
	
	
}

public static  String getMessage(String key){
	if(japanese==null || english==null){
		init();
	}
	if(lang.equals("ja")){
		String v= japanese.get(key);
		if(v==null){
			return english.get(key);
		}else{
			return v;
		}
	}else{
		return english.get(key);
	}
}
}
