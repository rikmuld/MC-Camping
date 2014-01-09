package rikmuld.camping.block;

import rikmuld.camping.entity.tileentity.TileEntityAntlerThrophy;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntlerThrophy extends BlockMain{

	public BlockAntlerThrophy(String name) 
	{
		super(name, Material.wood);
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityAntlerThrophy();
	}
}