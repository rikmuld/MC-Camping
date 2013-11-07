package rikmuld.camping.misc.guide;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rikmuld.camping.core.register.ModLogger;

public class PageTextData {

	Node data;
	
	public int x;
	public int y;
	public int width;
	public float size;
	public String text;

	public PageTextData(Node dataNode)
	{
		this.data = dataNode;
		this.setData();
	}

	public void setData()
	{
		Element data = (Element) this.data;
		
		Element textData = (Element) data.getElementsByTagName("data").item(0);
		this.text = textData.getTextContent();
		
		Element posData = (Element) data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		this.x = Integer.parseInt(pos.split("/")[0]);
		this.y = Integer.parseInt(pos.split("/")[1]);
		this.width = Integer.parseInt(pos.split("/")[2]);
		this.size = Float.parseFloat(pos.split("/")[3]);
	}
}
