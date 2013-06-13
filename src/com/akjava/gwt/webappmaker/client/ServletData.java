package com.akjava.gwt.webappmaker.client;

import com.akjava.lib.common.form.FormData;

public class ServletData {
private String basePackage;
private String lastPackage;
private String servletType;
private FormData formData;
public FormData getFormData() {
	return formData;
}
public void setFormData(FormData formData) {
	this.formData = formData;
}
private String path;
public static final String TYPE_INDEX="Index";
public static final String TYPE_LIST="List";
public static final String TYPE_SHOW="Show";
public static final String TYPE_ADD="Add";
public static final String TYPE_ADD_CONFIRM="AddConfirm";
public static final String TYPE_ADD_EXEC="AddExec";
public static final String TYPE_EDIT="Edit";
public static final String TYPE_EDIT_CONFIRM="EditConfirm";
public static final String TYPE_EDIT_EXEC="EditExec";
public static final String TYPE_DELETE_CONFIRM="DeleteConfirm";
public static final String TYPE_DELETE_EXEC="DeleteExec";

public ServletData(String baseString,String lastPackage,String serlvetType,FormData formData,String path){
	this.basePackage=baseString;
	this.lastPackage=lastPackage;
	this.servletType=serlvetType;
	this.formData=formData;
	this.path=path;
}
public String getBasePackage() {
	return basePackage;
}
public void setBasePackage(String basePackage) {
	this.basePackage = basePackage;
}
public String getLastPackage() {
	return lastPackage;
}
public void setLastPackage(String lastPackage) {
	this.lastPackage = lastPackage;
}
public String getServletType() {
	return servletType;
}
public void setServletType(String servletType) {
	this.servletType = servletType;
}

public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getDataClassName(){
	return formData.getClassName();
}
public String getServletClassName(){
	return this.getDataClassName()+this.getServletType()+"Servlet";
}
}
