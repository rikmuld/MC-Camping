package rikmuld.camping.misc.creativeTab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.register.ModBlocks;

public class TabMain extends CreativeTabs {

	public static ArrayList<Integer> eggIds = new ArrayList<Integer>();

	public TabMain(String label)
	{
		super(label);	
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(ModBlocks.campfireDeco);
	}
	
	@Override
	public void displayAllReleventItems(List list)
    {
		super.displayAllReleventItems(list);
		
		Iterator iterator = EntityList.entityEggs.values().iterator();

        while (iterator.hasNext())
        {
            EntityEggInfo entityegginfo = (EntityEggInfo)iterator.next();
        	if(eggIds.contains(entityegginfo.spawnedID))
        	{
        		list.add(new ItemStack(Item.monsterPlacer.itemID, 1, entityegginfo.spawnedID));
        	}
        }
	}
}