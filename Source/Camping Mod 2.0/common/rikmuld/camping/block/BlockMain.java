package rikmuld.camping.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModTabs;

public class BlockMain extends BlockContainer {

	public Icon[][] iconBuffer;
	public String[] metadata;
	public boolean useSides;

	public BlockMain(String name, Material material)
	{
		this(name, material, false);
	}

	public BlockMain(String name, Material material, boolean side)
	{
		super(BlockInfo.id(name), material);
		setCreativeTab(ModTabs.campingTab);
		useSides = side;
		setUnlocalizedName(name);
		ModBlocks.register(this, name);
	}

	public BlockMain(String name, Material material, String[] meta, Class<? extends ItemBlock> itemBlock, boolean side)
	{
		super(BlockInfo.id(name), material);
		metadata = meta;
		if(meta == null)
		{
			setUnlocalizedName(name);
		}
		useSides = side;
		setCreativeTab(ModTabs.campingTab);
		ModBlocks.registerWithMeta(this, name, itemBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if(useSides == true)
		{
			blockIcon = iconBuffer[metadata][side];
		}
		else if(this.metadata != null)
		{
			blockIcon = iconBuffer[metadata][0];
		}
		return blockIcon;
	}

	public String[] getSides(int metadata)
	{
		String[] sides = new String[6];
		for(int i = 0; i < sides.length; i++)
		{
			sides[i] = "side";
		}
		return sides;
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if(metadata == null)
		{
			if(useSides == false)
			{
				blockIcon = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().substring(5));
			}
			else
			{
				iconBuffer = new Icon[1][6];
				for(int side = 0; side < 6; side++)
				{
					iconBuffer[0][side] = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + getSides(0)[side]);
				}
			}
		}
		else
		{
			if(useSides == false)
			{
				iconBuffer = new Icon[metadata.length + 1][1];
				for(int x = 0; x < metadata.length; x++)
				{
					iconBuffer[x][0] = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + metadata[x].toString());
				}
			}
			else
			{
				iconBuffer = new Icon[metadata.length + 1][6];
				for(int x = 0; x < metadata.length; x++)
				{
					for(int side = 0; side < 6; side++)
					{
						iconBuffer[x][side] = iconRegister.registerIcon(ModInfo.MOD_ID + ":" + metadata[x].toString() + "_" + getSides(x)[side]);
					}
				}
			}
		}
	}
}