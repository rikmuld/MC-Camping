package rikmuld.camping.misc.guide;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PageLinkData {

	Node data;

	int link;
	int x;
	int y;
	int width;
	int height;

	public PageLinkData(Node dataNode)
	{
		data = dataNode;
		setData();
	}

	private void setData()
	{
		Element data = (Element)this.data;

		Element textData = (Element)data.getElementsByTagName("data").item(0);
		link = Integer.parseInt(textData.getTextContent());

		Element posData = (Element)data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		x = Integer.parseInt(pos.split("/")[0]);
		y = Integer.parseInt(pos.split("/")[1]);
		width = Integer.parseInt(pos.split("/")[2]);
		height = Integer.parseInt(pos.split("/")[3]);
	}
}
