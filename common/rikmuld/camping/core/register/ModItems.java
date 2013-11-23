package rikmuld.camping.core.register;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.item.ItemBackpack;
import rikmuld.camping.item.ItemFoodBerry;
import rikmuld.camping.item.ItemFoodMarshmallow;
import rikmuld.camping.item.ItemHemp;
import rikmuld.camping.item.ItemKit;
import rikmuld.camping.item.ItemKnife;
import rikmuld.camping.item.ItemParts;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModItems {
	
	public static Item knife;
	public static Item backpack;
	public static Item kit;
	public static Item parts;
	public static Item hemp;
	public static Item marshmallowCooked;
	public static Item berries;

	public static void init()
	{
		knife = new ItemKnife(ItemInfo.KNIFE);
		backpack = new ItemBackpack(ItemInfo.BACKPACK);
		kit = new ItemKit(ItemInfo.KIT);
		hemp = new ItemHemp(ItemInfo.HEMP);
		parts = new ItemParts(ItemInfo.PARTS);
		marshmallowCooked = new ItemFoodMarshmallow(ItemInfo.MARSHSTICK_COOKED);
		berries = new ItemFoodBerry(ItemInfo.BERRIES);
	}
	
	public static void register(Item item, String name)
	{
		GameRegistry.registerItem(item, name);
		LanguageRegistry.addName(item, ItemInfo.name(name));
	}
	
	public static void registerWithMeta(Item item, String name, String[] names)
	{
		GameRegistry.registerItem(item, name);
		for(int i = 0; i<names.length; i++)LanguageRegistry.addName(new ItemStack(item.itemID, 1, i), names[i]);
	}

	public static void registerItemBlock(ItemBlock item, String name)
	{
		LanguageRegistry.addName(item, BlockInfo.name(name));
	}
	
	public static void registerItemBlockWithMeta(ItemBlock item, String[] names)
	{
		ModLogger.logMulti(names[0], names[1]);
		for(int i = 0; i<names.length; i++)LanguageRegistry.addName(new ItemStack(item.itemID, 1, i), names[i]);
	}
}
