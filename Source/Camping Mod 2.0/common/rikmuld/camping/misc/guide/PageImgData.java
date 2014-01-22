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
		data = dataNode;
		setData();
	}

	private void setData()
	{
		Element data = (Element)this.data;

		Element textData = (Element)data.getElementsByTagName("source").item(0);
		source = textData.getTextContent();

		Element posData = (Element)data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		x = Integer.parseInt(pos.split("/")[0]);
		y = Integer.parseInt(pos.split("/")[1]);
		width = Integer.parseInt(pos.split("/")[2]);
		height = Integer.parseInt(pos.split("/")[3]);
		scale = Float.parseFloat(pos.split("/")[4]);
	}
}
