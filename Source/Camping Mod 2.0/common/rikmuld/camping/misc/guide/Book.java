package rikmuld.camping.misc.guide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Book {

	public static String MAIN_GUIDE_PATH = "/assets/camping/guide/";
	Document document;

	public ArrayList<Page> pages = new ArrayList<Page>();

	public Book(InputStream input)
	{
		File tempFile = null;

		try
		{
			tempFile = File.createTempFile("temp", "temp");
			tempFile.deleteOnExit();

			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(input, out);

			DocumentBuilderFactory docBuilderUtil = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderUtil.newDocumentBuilder();
			document = docBuilder.parse(tempFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		loadPages();
	}

	public void loadPages()
	{
		NodeList pages = document.getElementsByTagName("page");
		for(int i = 0; i < pages.getLength(); i++)
		{
			this.pages.add(new Page(pages.item(i)));
		}
	}
}