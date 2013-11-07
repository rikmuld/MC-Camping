package rikmuld.camping.misc.guide;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ItemInfo;

public class PageCraftData {

	Node data;
	
	public int x;
	public int y;
	public boolean shapeless;
	public ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
	
	public PageCraftData(Node dataNode)
	{
		this.data = dataNode;
		this.setData();
	}

	private void setData()
	{
		Element data = (Element) this.data;
		
		Element type = (Element) data.getElementsByTagName("shapeless").item(0);
		this.shapeless = Boolean.parseBoolean(type.getTextContent());
		
		Element textData = (Element) data.getElementsByTagName("stacks").item(0);
		NodeList list = textData.getElementsByTagName("stack");
		
		for(int i = 0; i<list.getLength(); i++)
		{
			if(list.item(i).getTextContent().length()>0)
			{					
				int id = 0;
				
				try
				{
					id = Integer.parseInt(list.item(i).getTextContent().split("/")[0]);
				}
				catch (NumberFormatException e)
				{
					if(ItemInfo.id(list.item(i).getTextContent().split("/")[0])!=-1)id = ItemInfo.id(list.item(i).getTextContent().split("/")[0]) + 256;
					else if(BlockInfo.id(list.item(i).getTextContent().split("/")[0])!=-1)id = BlockInfo.id(list.item(i).getTextContent().split("/")[0]);
				}
				stacks.add(new ItemStack(id, 1, Integer.parseInt(list.item(i).getTextContent().split("/")[1])));
			}
			else stacks.add(null);
			
		}
		
		Element posData = (Element) data.getElementsByTagName("pos").item(0);
		String pos = posData.getTextContent();
		this.x = Integer.parseInt(pos.split("/")[0]);
		this.y = Integer.parseInt(pos.split("/")[1]);
	}
}
