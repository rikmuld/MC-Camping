package rikmuld.camping.core.register;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rikmuld.camping.item.ItemKit;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.misc.cooking.CookingEquipment;
import rikmuld.camping.misc.cooking.CookingEquipmentList;
import rikmuld.camping.misc.cooking.Grill;
import rikmuld.camping.misc.cooking.Pan;
import rikmuld.camping.misc.cooking.Spit;

public class ModCookingEquipment {

	public static CookingEquipment grill;
	public static CookingEquipment spit;
	public static CookingEquipment pan;

	static ItemStack stick = new ItemStack(Item.stick);
	static ItemStack ironStick = new ItemStack(ModItems.parts.itemID, 1, ItemParts.STICK);
	public static void init()
	{		
		CookingEquipmentList.addGrillFood(Item.fishRaw.itemID, new ItemStack(Item.fishCooked.itemID, 1, 0));
		CookingEquipmentList.addGrillFood(Item.beefRaw.itemID, new ItemStack(Item.beefCooked.itemID, 1, 0));
		CookingEquipmentList.addGrillFood(Item.porkRaw.itemID, new ItemStack(Item.porkCooked.itemID, 1, 0));
		
		CookingEquipmentList.addPanFood(Item.potato.itemID, new ItemStack(Item.bakedPotato.itemID, 1, 0));
		CookingEquipmentList.addPanFood(Item.rottenFlesh.itemID, new ItemStack(Item.leather.itemID, 1, 0));

		CookingEquipmentList.addSpitFood(Item.chickenRaw.itemID, new ItemStack(Item.chickenCooked.itemID, 1, 0));
		CookingEquipmentList.addSpitFood(Item.fishRaw.itemID, new ItemStack(Item.fishCooked.itemID, 1, 0));
		
		spit = new Spit(new ItemStack(ModItems.kit.itemID, 1, ItemKit.KIT_SPIT));	
		grill = new Grill(new ItemStack(ModItems.kit.itemID, 1, ItemKit.KIT_GRILL));	
		pan = new Pan(new ItemStack(ModItems.kit.itemID, 1, ItemKit.KIT_PAN));
		
		CookingEquipmentList.addEquipmentRecipe(spit, stick, stick, ironStick);
		CookingEquipmentList.addEquipmentRecipe(grill, stick, stick, stick, stick,  ironStick, ironStick, new ItemStack(Block.fenceIron));
		CookingEquipmentList.addEquipmentRecipe(pan, stick, stick,  ironStick, new ItemStack(Item.silk), new ItemStack(ModItems.parts, 1, 2));
	}
	
	public static void register(CookingEquipment cooking, ItemStack item)
	{
		CookingEquipmentList.addCooking(item, cooking);
	}
}
