package rikmuld.camping.misc.guide;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PageImgData {

	Node data;
	
	public String source;
	public int x;
	public int y;
	public int width;
	public int height;
	public float scale;
	
	public PageImgData(Node dataNode)
	{
		this.data = dataNode;
		this.setData();
	}

	private void setData()
	{
		Element data = (Element) this.data;
		
		Element textData = (Element) data.getElementsByTagName("source").item(0);
		this.source = textData.getTextContent();
		
		Element posData = (Element) data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		this.x = Integer.parseInt(pos.split("/")[0]);
		this.y = Integer.parseInt(pos.split("/")[1]);
		this.width = Integer.parseInt(pos.split("/")[2]);
		this.height = Integer.parseInt(pos.split("/")[3]);
		this.scale = Float.parseFloat(pos.split("/")[4]);
	}
}
