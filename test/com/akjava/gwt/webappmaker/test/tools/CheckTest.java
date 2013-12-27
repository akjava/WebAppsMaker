package com.akjava.gwt.webappmaker.test.tools;

import junit.framework.TestCase;

import com.akjava.gwt.webappmaker.client.BundleControler;
import com.akjava.gwt.webappmaker.client.WebAppMakerClientBundleResourceLoader;
import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.google.gwt.junit.client.GWTTestCase;

public class CheckTest extends GWTTestCase{

	public void  gwtSetUp(){
		BundleControler.setLoader(new WebAppMakerClientBundleResourceLoader());
	}
	@Override
	public String getModuleName() {
		return "com.akjava.gwt.webappmaker.test.WebAppsMakerTest";
	}
	
	public void test(){
		assertEquals("test", Bundles.INSTANCE.add_html().getText());
	}
	
	
	public void test1(){
		String text=BundleControler.getLoader().getText("add_html.txt");
		assertEquals(text, "hello");
	}

}
