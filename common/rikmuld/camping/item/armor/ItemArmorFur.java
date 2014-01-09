package rikmuld.camping.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModItems;

public class ItemArmorFur extends ItemArmorMain{

	public ItemArmorFur(String name, int type)
	{
		super(name, FUR, type);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
	{
		if(stack.itemID==ModItems.armorFurLeg.itemID) return TextureInfo.ARMOR_FUR_LEG;		
		else return TextureInfo.ARMOR_FUR_MAIN;
	}
}
