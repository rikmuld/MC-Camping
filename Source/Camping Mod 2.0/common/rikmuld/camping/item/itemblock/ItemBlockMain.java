package rikmuld.camping.item.itemblock;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModTabs;

public class ItemBlockMain extends ItemBlock {

	private Icon[] iconBuffer;
	private String[] metadata;
	boolean useIcon;

	public ItemBlockMain(int id, String name, boolean useIcon)
	{
		super(id);
		setUnlocalizedName(name);
		setCreativeTab(ModTabs.campingTab);
		ModItems.registerItemBlock(this, name);
		this.useIcon = useIcon;
	}

	public ItemBlockMain(int id, String[] meta, String[] metaName, boolean renderMeta, boolean useIcon)
	{
		super(id);
		if(renderMeta)
		{
			metadata = metaName;
		}
		setHasSubtypes(true);
		setCreativeTab(ModTabs.campingTab);
		ModItems.registerItemBlockWithMeta(this, meta);
		this.useIcon = useIcon;
	}

	@Override
	public Icon getIconFromDamage(int metadata)
	{
		if((this.metadata != null) && useIcon)
		{
			itemIcon = iconBuffer[metadata];
		}
		return itemIcon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if(useIcon)
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
}
