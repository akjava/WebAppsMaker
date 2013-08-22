package com.akjava.gwt.webappmaker.client;

import com.akjava.lib.common.utils.ValuesUtils;

public class DirectoryDetector {

	public static String detectDirectory(String basePackage,FileNameAndText file){
		String name=file.getName();
		String packageName=basePackage.replace('.', '/');
		if(packageName.endsWith("/")){
			packageName=ValuesUtils.chomp(packageName);
		}
		if(name.equals("web.xml")){
			return "war/WEB-INF/"+name;
		}
		
		if(name.endsWith(".html")){
			return "war/WEB-INF/template/"+name;
		}else{//java
			if(name.startsWith("Admin")){
				return "src/"+packageName+"/admin/"+name;
			}else if(isCoreName(name)){
				return "src/"+packageName+"/"+name;
			}else{
				return "src/"+packageName+"/main/"+name;
			}
			//admin
			//main
			//core	
		}
		
	}
	
	public static boolean isCoreName(String name){
		if(name.endsWith("Tools.java")){
			return true;
		}
		if(name.endsWith("Modifier.java")){
			return true;
		}
		if(name.endsWith("Validator.java")){
			return true;
		}
		if(name.endsWith("SharedUtils.java")){
			return true;
		}
		return false;
	}
}
