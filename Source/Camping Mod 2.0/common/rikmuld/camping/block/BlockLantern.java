package rikmuld.camping.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.entity.tileentity.TileEntityLantern;
import rikmuld.camping.item.itemblock.ItemBlockLantern;

public class BlockLantern extends BlockMain {

	int burnTime;

	public BlockLantern(String name)
	{
		super(name, Material.glass, ItemBlockLantern.metadataNames, ItemBlockLantern.class, false);
		setBlockBounds(0.3F, 0, 0.3F, 0.7F, 0.9F, 0.7F);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		burnTime = world.getBlockTileEntity(x, y, z) != null? ((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime:0;
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		int l = world.getBlockId(x, y, z);
		Block block = Block.blocksList[l];
		return ((block == null) || block.isBlockReplaceable(world, x, y, z)) && BlockUtil.isTouchableBlockPartitionalSolidForSideAndHasCorrectBounds(world, x, y, z, 0, 1);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityLantern();
	}

	@Override
	protected void dropBlockAsItem_do(World world, int x, int y, int z, ItemStack stack)
	{
		if(!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("time", burnTime);

			float f = 0.7F;
			double d0 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			double d1 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			double d2 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack);
			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
		}
	}

	public void dropIfCantStay(World world, int x, int y, int z)
	{
		if(!BlockUtil.isTouchableBlockPartitionalSolidForSideAndHasCorrectBounds(world, x, y, z, 0, 1))
		{
			breakBlock(world, x, y, z, blockID, 0);
			for(ItemStack item: getBlockDropped(world, x, y, z, 0, 1))
			{
				dropBlockAsItem_do(world, x, y, z, item);
			}
			world.setBlock(x, y, z, 0);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 0? 15:0;
	}

	@Override
	public int getRenderType()
	{
		return 1;
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if(!world.isRemote)
		{
			if((world.getBlockMetadata(x, y, z) == 1) && (player.getCurrentEquippedItem().itemID == Item.glowstone.itemID))
			{
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y, z);

				((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime = 1500;

				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, blockID, 10);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		world.scheduleBlockUpdate(x, y, z, blockID, 10);
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)
		{
			dropIfCantStay(world, x, y, z);
		}
	}
}
