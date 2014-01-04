package com.akjava.gwt.webappmaker.client;

import com.akjava.gwt.html5.client.file.ui.FileNameAndText;
import com.google.common.base.Function;

/**
 * .settings/org.eclipse.core.resources.prefs
 * force set file encoding
 * @author aki
 *
 */
public class FileToEncodingFunction implements Function<FileNameAndText,String>{
private String directory;
private String encoding;
public FileToEncodingFunction(String directory,String encoding){
	this.directory=directory;
	if(directory.startsWith("/")){
		directory=directory.substring(1);
	}
	if(!directory.endsWith("/")){
		directory=directory+"/";
	}
	this.encoding=encoding;
}
@Override
public String apply(FileNameAndText file) {
	return "encoding//"+directory+file.getName()+"="+encoding;
}
}
