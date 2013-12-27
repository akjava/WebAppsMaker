package com.akjava.gwt.webappmaker.client;

import com.akjava.gwt.webappmaker.client.resources.Bundles;

public class WebAppMakerClientBundleResourceLoader implements ResourceLoader{

	@Override
	public String getText(String id) {
		if(id.equals("add_html.txt")){
			return Bundles.INSTANCE.add_html().getText();
		}
		return null;
	}

}
