package com.akjava.gwt.webappmaker.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormFieldDataDto;
import com.akjava.lib.common.functions.HtmlFunctions;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Window;

public class ServletDataDto {
private ServletDataDto(){}

public static ServletDataToServletWebXmlFunction getServletDataToServletWebXmlFunction(){
	return ServletDataToServletWebXmlFunction.INSTANCE;
}
public enum ServletDataToServletWebXmlFunction implements Function<ServletData,ServletWebXmlData>{
	INSTANCE;
	@Override
	public ServletWebXmlData apply(ServletData data) {
		String lower=data.getDataClassName().toLowerCase();
		ServletWebXmlData xdata=new ServletWebXmlData();
		xdata.setFullClassName(data.getBasePackage()+data.getLastPackage()+"."+data.getServletClassName());
		xdata.setPath(data.getPath());
		xdata.setName(data.getDataClassName()+data.getServletType());
		return xdata;
	}
	
}


public static class FormDataToMainServletDataFunction implements Function<FormData,List<ServletData>>{
	private String basePackage;
	public FormDataToMainServletDataFunction(String basePackage){
		this.basePackage=basePackage;
	}
	@Override
	public List<ServletData> apply(FormData fdata) {
		List<ServletData> datas=Lists.newArrayList();
		datas.add(new ServletData(basePackage,"main",ServletData.TYPE_LIST,fdata,"/index.html"));
		datas.add(new ServletData(basePackage,"main",ServletData.TYPE_SHOW,fdata,"/show"));
		
		return datas;
	}	
}

public static class FormDataToAdminServletDataFunction implements Function<FormData,List<ServletData>>{
	private String basePackage;
	public FormDataToAdminServletDataFunction(String basePackage){
		this.basePackage=basePackage;
	}
	@Override
	public List<ServletData> apply(FormData fdata) {
		List<ServletData> datas=Lists.newArrayList();
		
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_LIST,fdata,"/"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_SHOW,fdata,"/show"));
		
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_ADD,fdata,"/add"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_ADD_CONFIRM,fdata,"/add_confirm"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_ADD_EXEC,fdata,"/add_exec"));
		
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_EDIT,fdata,"/edit"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_EDIT_CONFIRM,fdata,"/edit_confirm"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_EDIT_EXEC,fdata,"/edit_exec"));
		
		
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_DELETE_CONFIRM,fdata,"/delete_confirm"));
		datas.add(new ServletData(basePackage,"admin",ServletData.TYPE_DELETE_EXEC,fdata,"/delete_exec"));
		return datas;
	}	
}

public static class ServletDataToServletFileFunction implements Function<ServletData,FileNameAndText>{

	@Override
	public FileNameAndText apply(ServletData data) {
		FileNameAndText file=new FileNameAndText();
		file.setName(data.getServletClassName()+".java");
		String javaTemplate=null;
		if(data.getServletType().equals(ServletData.TYPE_LIST)){
			javaTemplate=Bundles.INSTANCE.list_servlet().getText();
		}else if(data.getServletType().equals(ServletData.TYPE_SHOW)){
			javaTemplate=Bundles.INSTANCE.show_servlet().getText();
		}
		
		if(javaTemplate==null){
			Window.alert("invalid type:"+data.getServletType());
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("className", data.getServletClassName());
		map.put("basePackage", data.getBasePackage());
		map.put("package", data.getBasePackage()+data.getLastPackage());
		
		map.put("baseTemplate", data.getLastPackage()+"Base.html");
		String mainTemplate=data.getDataClassName()+"_"+data.getServletType();
		map.put("mainTemplate", mainTemplate.toLowerCase()+".html");
		map.put("title", data.getFormData().getName()+" "+Internationals.getMessage(data.getServletType().toLowerCase()));
		
		map.put("mainRowTemplate", mainTemplate.toLowerCase()+"_row"+".html");
		map.put("dataClassName", data.getDataClassName());
		map.put("path",data.getPath());
		
		String path=data.getPath();
		if(!path.endsWith("/")){
			int last=path.lastIndexOf("/");
			if(last!=-1){
				String indexPath=path.substring(0,last+1);
				map.put("indexPath", indexPath);
			}
		}
		
		String text=TemplateUtils.createAdvancedText(javaTemplate, map);
		file.setText(text);
		
		return file;
	}
	
	
}

public static class ServletDataToTemplateFileFunction implements Function<ServletData,List<FileNameAndText>>{

	@Override
	public List<FileNameAndText> apply(ServletData data) {
		List<FileNameAndText> files=new ArrayList<FileNameAndText>();
		String type=data.getServletType();
		
		FileNameAndText file=new FileNameAndText();
		file.setName(data.getDataClassName().toLowerCase()+"_"+type.toLowerCase()+".html");
		
		String htmlTemplate=null;
		
		if(type.equals(ServletData.TYPE_LIST)){
			htmlTemplate=Bundles.INSTANCE.list_html().getText();
			
			FileNameAndText file2=new FileNameAndText();
			file2.setName(data.getDataClassName().toLowerCase()+"_"+type.toLowerCase()+"_row.html");
			files.add(file2);
			file2.setText(Bundles.INSTANCE.list_row_html().getText());
		}else if(type.equals(ServletData.TYPE_SHOW)){
			htmlTemplate=Bundles.INSTANCE.show_servlet().getText();
		}
		
		if(htmlTemplate==null){
			Window.alert("invalid type:"+data.getServletType());
		}
		file.setText(htmlTemplate);
		files.add(file);
		
		
		Map<String,String> map=new HashMap<String,String>();
		//headers
		List<String> ths=Lists.transform(Lists.transform(data.getFormData().getFormFieldDatas(), FormFieldDataDto.getFormFieldToNameFunction())
				, HtmlFunctions.getStringToTHFunction());
		map.put("headers", Joiner.on("\n").join(ths));
		//columns
		List<String> tds=Lists.transform(
				Lists.transform(
				Lists.transform(data.getFormData().getFormFieldDatas(), FormFieldDataDto.getFormFieldToKeyFunction())
				,new HtmlFunctions.StringToPreFixAndSuffix("${","}"))
				, HtmlFunctions.getStringToTDFunction());
		map.put("columns", Joiner.on("\n").join(tds));
		
		map.put("add_title", Internationals.getMessage("add"));
		map.put("edit_title", Internationals.getMessage("edit"));
		map.put("delete_title", Internationals.getMessage("delete"));
		
		for(FileNameAndText fileText:files){
			String text=TemplateUtils.createAdvancedText(fileText.getText(), map);
			fileText.setText(text);
		}
		
		return files;
	}
	
	
}

}
