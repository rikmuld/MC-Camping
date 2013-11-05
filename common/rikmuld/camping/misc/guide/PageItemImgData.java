package rikmuld.camping.misc.guide;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.register.ModLogger;

public class PageItemImgData {

	Node data;
	
	public ItemStack stack;
	public int x;
	public int y;
	
	public PageItemImgData(Node dataNode)
	{
		this.data = dataNode;
		this.setData();
	}

	private void setData()
	{
		Element data = (Element) this.data;
		
		Element textData = (Element) data.getElementsByTagName("stack").item(0);
		String stackData = textData.getTextContent();
		
		int id = 0;
		
		try
		{
			id = Integer.parseInt(stackData.split("/")[0]);
		}
		catch (NumberFormatException e)
		{
			id = ItemInfo.id(stackData.split("/")[0]) + 256;
		}
		
		stack = new ItemStack(id, 1, Integer.parseInt(stackData.split("/")[1]));
		
		Element posData = (Element) data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		this.x = Integer.parseInt(pos.split("/")[0]);
		this.y = Integer.parseInt(pos.split("/")[1]);
	}
}
