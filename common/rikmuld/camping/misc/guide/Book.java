package rikmuld.camping.misc.guide;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rikmuld.camping.core.register.ModLogger;

public class Book {

	public static String MAIN_GUIDE_PATH = "/assets/camping/guide/";
	Document document;  
	
	public ArrayList<Page> pages = new ArrayList<Page>();
	
	public Book(URI path)
	{
		File file = new File(path);
		
		try
		{
			DocumentBuilderFactory docBuilderUtil = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderUtil.newDocumentBuilder();
			document = docBuilder.parse(file);
		}
		catch(ParserConfigurationException | SAXException | IOException e) 
		{
			e.printStackTrace();
		}
		
		this.loadPages();
	}
	
	public void loadPages()
	{
		NodeList pages = document.getElementsByTagName("page");
		for(int i = 0; i<pages.getLength(); i++)
		{
			this.pages.add(new Page(pages.item(i)));
		}
	}
}