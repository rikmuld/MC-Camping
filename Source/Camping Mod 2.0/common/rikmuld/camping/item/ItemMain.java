package rikmuld.camping.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModTabs;

public class ItemMain extends Item {

	private Icon[] iconBuffer;
	private String[] metadata;

	public ItemMain(String name)
	{
		super(ItemInfo.id(name));
		setUnlocalizedName(name);
		setCreativeTab(ModTabs.campingTab);
		ModItems.register(this, name);
	}

	public ItemMain(String name, String[] meta, String[] metaName, boolean renderMeta)
	{
		super(ItemInfo.id(name));
		if(renderMeta)
		{
			metadata = metaName;
		}
		else
		{
			setUnlocalizedName(name);
		}
		setHasSubtypes(true);
		setCreativeTab(ModTabs.campingTab);
		ModItems.registerWithMeta(this, name, meta);
	}

	@Override
	public Icon getIconFromDamage(int metadata)
	{
		if(this.metadata != null)
		{
			itemIcon = iconBuffer[metadata];
		}
		return itemIcon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if(metadata == null)
		{
			itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
		}
		else
		{
			iconBuffer = new Icon[metadata.length + 1];
			for(int x = 0; x < metadata.length; x++)
			{
				iconBuffer[x] = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + metadata[x].toString());
			}
		}
	}
}
