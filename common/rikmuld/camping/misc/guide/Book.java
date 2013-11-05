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

/*

XML structure:

Book -> Pages

Page 	->	Text Elements (text)
		->	Link Elements (link)
		->	Image Elements (img)
		->	Crafting Elements (craft)
		->  Item Image Elements (item)

Text Element 		-> Text Data (data)
					-> Position (pos) (A String x/y/width/size)
				
Link Element 		-> Link num to page	(page)
					-> position (pos) (A String x/y/width/height)
		
Image Element 		-> Image source (source)
					-> Position (pos) (A String x/y/width/height)
					-> OPTIONAL: A Link num to page (link)
				
Crafting Element	-> position (pos) (A String x/y)
					-> Shapless or not (shapeless) (true or false)
					-> ItemData Elements (stacks)
							
Item Image Element 	-> ItemStack source (stack)(A String id/meta)
					-> Position (pos) (A String x/y)
					
					ItemData Element 	-> Item Data (stack) (id/meta)
					
*/