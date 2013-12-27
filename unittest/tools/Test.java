package tools;

import junit.framework.TestCase;

import com.akjava.gwt.webappmaker.client.BundleControler;
import com.akjava.gwt.webappmaker.client.JavaResourceLoader;

public class Test extends TestCase{

	public void setUp(){
		BundleControler.setLoader(new JavaResourceLoader("com/akjava/gwt/webappmaker/client/resources/"));
	}

	public void test1(){
		String text=BundleControler.getLoader().getText("add_html.txt");
		assertEquals(text, "hello");
	}
}
