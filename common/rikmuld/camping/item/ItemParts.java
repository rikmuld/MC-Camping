package rikmuld.camping.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;

public class ItemParts extends ItemMain{

	public static final int CANVAS = 0;
	public static final int STICK = 1;
	public static final int PAN = 2;
	public static final int ASH = 3;
	public static final int LANTERN_OFF = 4;

	public static final String[] metadataIGNames = new String[]{"Canvas", "Iron Stick", "Pan", "Ash", "Empty Lantern"};
	public static final String[] metadataNames = new String[]{"canvas", "stickIron", "pan", "ash", "lanternOff"};

	public ItemParts(String name)
	{
		super(name, metadataIGNames, metadataNames, true);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativetabs, List list)
	{
		for(int meta = 0; meta<5; ++meta)
		{
			list.add(new ItemStack(id, 1, meta));
		}
	}
}
