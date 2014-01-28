package rikmuld.camping.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAnimalStuff extends ItemMain {

	public static final int FUR_WHITE = 0;
	public static final int FUR_BROWN = 1;
	public static final int ANTLER = 2;

	public static final String[] metadataIGNames = new String[]{"White Fur", "Brown Fur", "Antler"};
	public static final String[] metadataNames = new String[]{"furWhite", "furBrown", "antler"};

	public ItemAnimalStuff(String name)
	{
		super(name, metadataIGNames, metadataNames, true);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}
}
