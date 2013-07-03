package com.akjava.gwt.webappmaker.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.gwt.user.client.Window;

public class FileNameAndTextGenerator {

	public static FileNameAndText generateTopTemplate(List<FormData> formdatas){
		FileNameAndText file=new FileNameAndText();
		
		file.setName("main_top.html");
		String base=Bundles.INSTANCE.top_links_html().getText();
		String sub=Bundles.INSTANCE.top_links_sub_html().getText();
		
		Map<String,String> baseMap=new HashMap<String, String>();
		
		String datas="";
		for(FormData fdata:formdatas){
			String path=fdata.getClassName().toLowerCase()+"/";
			String title=fdata.getName();
			Map<String,String> subMap=new HashMap<String, String>();
			subMap.put("path",path);
			subMap.put("title",title);
			datas+=TemplateUtils.createAdvancedText(sub, subMap);
		}
		
		baseMap.put("data",datas);
		String text=TemplateUtils.createAdvancedText(base, baseMap);
		file.setText(text);
		
		return file;
	}
	
	public static FileNameAndText generateAdminTopTemplate(List<FormData> formdatas){
		FileNameAndText file=new FileNameAndText();
		
		file.setName("admin_top.html");
		String base=Bundles.INSTANCE.top_links_html().getText();
		String sub=Bundles.INSTANCE.top_links_sub_html().getText();
		
		Map<String,String> baseMap=new HashMap<String, String>();
		
		String datas="";
		for(FormData fdata:formdatas){
			String path=fdata.getClassName().toLowerCase()+"/";
			String title=fdata.getName();
			Map<String,String> subMap=new HashMap<String, String>();
			subMap.put("path",path);
			subMap.put("title",title);
			datas+=TemplateUtils.createAdvancedText(sub, subMap);
		}
		
		baseMap.put("data",datas);
		String text=TemplateUtils.createAdvancedText(base, baseMap);
		file.setText(text);
		
		return file;
	}
	
	public static FileNameAndText generateTopServlet(String basePackage){
		FileNameAndText file=new FileNameAndText();
		String className="TopServlet";
		
		file.setName(className+".java");
		String javaTemplate=Bundles.INSTANCE.top_servlet().getText();
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("className", className);
		map.put("basePackage", basePackage);
		map.put("package", basePackage+"main");
		
		map.put("baseTemplate", "mainBase.html");
		
		map.put("mainTemplate", "main_top.html");
		map.put("title", Internationals.getMessage("top"));
		map.put("path","/index.html");
		
		String text=TemplateUtils.createAdvancedText(javaTemplate, map);
		file.setText(text);
		
		return file;
	}
	
	public static FileNameAndText generateAdminTopServlet(String basePackage){
		FileNameAndText file=new FileNameAndText();
		String className="AdminTopServlet";
		
		file.setName(className+".java");
		String javaTemplate=Bundles.INSTANCE.top_servlet().getText();
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("className", className);
		map.put("basePackage", basePackage);
		map.put("package", basePackage+"admin");
		
		map.put("baseTemplate", "adminBase.html");
		
		map.put("mainTemplate", "admin_top.html");
		map.put("title", Internationals.getMessage("admintop"));
		map.put("path","/admin/index.html");
		
		String text=TemplateUtils.createAdvancedText(javaTemplate, map);
		file.setText(text);
		
		return file;
	}
}
