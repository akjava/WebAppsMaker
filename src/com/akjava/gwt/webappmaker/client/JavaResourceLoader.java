package com.akjava.gwt.webappmaker.client;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class JavaResourceLoader implements ResourceLoader{
private String basePackage;
private Charset charsets=Charsets.UTF_8;
/**
 * 
 * @param basePackage must finish / a class folder include text resources.
 */
public JavaResourceLoader(String basePackage){
	this.basePackage=basePackage;
}
	@Override
	public String getText(String id) {
		URL url = Resources.getResource(basePackage+id);
		try {
			String text = Resources.toString(url,charsets);
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
