package com.akjava.gwt.webappmaker.client;

public class BundleControler {
public BundleControler(){
	
}
//public static BundleControler controler=
private  static ResourceLoader loader;//=new JavaResourceLoader("com/akjava/gwt/webappmaker/client/resources/");
	public static ResourceLoader getLoader() {
		if(loader==null){
			throw new RuntimeException("ResourceLoader of BundleControler is null.you forget?ClientBundleResourceLoader or JavaResourceLoader");
		}
	return loader;
}
public static void setLoader(ResourceLoader loader) {
	BundleControler.loader = loader;
}
	public static String getText(String id){
	return loader.getText(id);
	}
}
