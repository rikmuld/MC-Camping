package rikmuld.camping.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import rikmuld.camping.entity.tileentity.TileEntityLight;

public class BlockLight extends BlockMain {

	public BlockLight(String name)
	{
		super(name, Material.air);
		setLightValue(1.0F);
		setBlockBounds(0F, 0F, 0F, 0F, 0F, 0F);
		setCreativeTab(null);
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		return new TileEntityLight();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
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
}
