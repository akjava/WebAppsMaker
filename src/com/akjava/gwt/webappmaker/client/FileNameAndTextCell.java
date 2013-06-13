package com.akjava.gwt.webappmaker.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class FileNameAndTextCell extends AbstractCell<FileNameAndText>{

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			FileNameAndText value, SafeHtmlBuilder sb) {
		 if(value == null){
		      return;
		    }
		 sb.appendHtmlConstant(value.getName());
	}

}
