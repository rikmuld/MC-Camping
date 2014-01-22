package rikmuld.camping.item.armor;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.EnumHelper;
import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModTabs;

public class ItemArmorMain extends ItemArmor {

	static EnumArmorMaterial FUR = EnumHelper.addArmorMaterial("FUR", 20, new int[]{2, 5, 4, 2}, 20);

	public ItemArmorMain(String name, EnumArmorMaterial material, int type)
	{
		super(ItemInfo.id(name), material, 0, type);
		setUnlocalizedName(name);
		maxStackSize = 1;
		setCreativeTab(ModTabs.campingTab);
		ModItems.register(this, name);
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
	}
}
