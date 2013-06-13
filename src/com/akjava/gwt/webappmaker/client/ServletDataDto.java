package com.akjava.gwt.webappmaker.client;

import java.util.List;

import com.akjava.lib.common.form.FormData;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ServletDataDto {
private ServletDataDto(){}

public ServletDataToServletWebXmlFunction getServletDataToServletWebXmlFunction(){
	return ServletDataToServletWebXmlFunction.INSTANCE;
}
public enum ServletDataToServletWebXmlFunction implements Function<ServletData,ServletWebXmlData>{
	INSTANCE;
	@Override
	public ServletWebXmlData apply(ServletData data) {
		String lower=data.getDataClassName().toLowerCase();
		ServletWebXmlData xdata=new ServletWebXmlData();
		xdata.setFullClassName(data.getBasePackage()+data.getLastPackage()+"."+data.getServletClassName());
		xdata.setPath("/"+lower+"/"+data.getPath());
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
		datas.add(new ServletData(basePackage,"main",ServletData.TYPE_LIST,fdata,"/"));
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
		file.setText("hello world");
		return file;
	}
	
	
}

}
