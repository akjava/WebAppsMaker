package com.akjava.gwt.webappmaker.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.taskdefs.SendEmail;
import org.mortbay.jetty.Request;

public class ZipGenerateServlet extends HttpServlet{
public static final String KEY_FILE_NUMBER="filenumber";
public static final String KEY_FILE_PATH="filepath";
public static final String KEY_FILE_CONTENT="filecontent";
public static final String KEY_DOWNLOAD_NAME="downloadname";
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	int fileNumber=0;
	String fileNumberString=req.getParameter(KEY_FILE_NUMBER);
	try{
		fileNumber=Integer.parseInt(fileNumberString);
	}catch (Exception e) {
	}
	if(fileNumber==0){
		resp.sendError(500, "invalid filenumber value");
		return;
	}
	
	String downloadName=req.getParameter(KEY_DOWNLOAD_NAME);
	if(downloadName==null){
		downloadName="output.zip";
	}
	
	String filePathBase=req.getParameter(KEY_FILE_PATH);
	if(filePathBase==null){
		filePathBase="path";
	}
	String fileContentBase=req.getParameter(KEY_FILE_CONTENT);
	if(fileContentBase==null){
		fileContentBase="text";
	}
	
	ByteArrayOutputStream bout=new ByteArrayOutputStream();
	ZipOutputStream zipOut=new ZipOutputStream(bout);
	for(int i=1;i<=fileNumber;i++){
		String path=req.getParameter(filePathBase+i);
		if(path==null){
			resp.sendError(500, "not found path:"+(filePathBase+i));
			return;
		}
		String content=req.getParameter(fileContentBase+i);
		if(content==null){
			resp.sendError(500, "not found content:"+(fileContentBase+i));
			return;
		}
		ZipEntry entry=new ZipEntry(path);
		zipOut.putNextEntry(entry);
		byte[] chars=content.getBytes("UTF-8");
		zipOut.write(chars);
		zipOut.closeEntry();
	}
	zipOut.finish();
	
	byte[] bytes=bout.toByteArray();
	
	resp.setContentType( "application/zip" ); 
	resp.setContentLength( bytes.length ); 
    resp.setHeader( "Content-Disposition", "inline; filename=" 
+ downloadName); 
    resp.getOutputStream().write( bytes ); 
}


}