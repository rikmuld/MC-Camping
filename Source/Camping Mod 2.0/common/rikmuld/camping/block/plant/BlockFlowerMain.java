package rikmuld.camping.block.plant;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFlowerMain extends BlockFlower {

	public int metadata;
	public static Icon[] icon;

	protected BlockFlowerMain(String name, Material material)
	{
		super(BlockInfo.id(name), material);
		setUnlocalizedName(name);
		ModBlocks.register(this, name);
	}

	@Override
	public Icon getIcon(int par1, int par2)
	{
		if((par2 < 0) || (par2 > metadata))
		{
			par2 = metadata;
		}
		return icon[par2];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		icon = new Icon[metadata + 1];

		for(int i = 0; i < icon.length; ++i)
		{
			icon[i] = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + i);
		}
	}

	public int setGrowStates(int Metadata)
	{
		metadata = Metadata;
		return metadata;
	}
}
