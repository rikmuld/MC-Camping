package rikmuld.camping.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import rikmuld.camping.entity.tileentity.TileEntityRotation;

public class BlockBoundsHelper extends BlockMain {

	public BlockBoundsHelper(String name)
	{
		super(name, Material.sponge);
		setCreativeTab(null);
	}

	public static boolean isBlockHeadOfBed(int meta)
	{
		return true;
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB alignedBB, List list, Entity entity)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		if(tile.bounds != null)
		{
			tile.bounds.setBlockCollision(this);
		}
		super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);

		if(world.getBlockId(tile.baseX, tile.baseY, tile.baseZ) != 0)
		{
			Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].breakBlock(world, tile.baseX, tile.baseY, tile.baseZ, par5, par6);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityBounds();
	}

	@Override
	public int getBedDirection(IBlockAccess world, int x, int y, int z)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		return ((TileEntityRotation)world.getBlockTileEntity(tile.baseX, tile.baseY, tile.baseZ)).rotation;
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		if(world.getBlockId(tile.baseX, tile.baseY, tile.baseZ) != 0) return Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].blockHardness;

		return blockHardness;
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.cloth.getIcon(0, 0);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLivingBase player)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		return Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].isBed(world, tile.baseX, tile.baseY, tile.baseZ, player);
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		return Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].onBlockActivated(world, tile.baseX, tile.baseY, tile.baseZ, player, par6, par7, par8, par9);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		if(Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)] != null)
		{
			Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].onNeighborBlockChange(world, tile.baseX, tile.baseY, tile.baseZ, id);
		}
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityBounds tile = (TileEntityBounds)world.getBlockTileEntity(x, y, z);
		if(tile.bounds != null)
		{
			tile.bounds.setBlockBounds(this);
		}
	}
}
