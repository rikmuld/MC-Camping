package rikmuld.camping.core.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockUtil {

	static Random rand = new Random();

	public static void dropItems(World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity instanceof IInventory)
			{
				IInventory inventory = (IInventory)tileEntity;
				for(int i = 0; i < inventory.getSizeInventory(); i++)
				{
					ItemStack itemStack = inventory.getStackInSlot(i);
					if((itemStack != null) && (itemStack.stackSize > 0))
					{
						float dX = (rand.nextFloat() * 0.8F) + 0.1F;
						float dY = (rand.nextFloat() * 0.8F) + 0.1F;
						float dZ = (rand.nextFloat() * 0.8F) + 0.1F;
						EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));
						if(itemStack.hasTagCompound())
						{
							entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
						}
						float factor = 0.05F;
						entityItem.motionX = rand.nextGaussian() * factor;
						entityItem.motionY = (rand.nextGaussian() * factor) + 0.2F;
						entityItem.motionZ = rand.nextGaussian() * factor;
						world.spawnEntityInWorld(entityItem);
						itemStack.stackSize = 0;
					}
				}
			}
		}
	}

	public static int[][] getBlocks(World world, int x, int y, int z)
	{
		int[][] ids = new int[6][2];

		ids[0][0] = world.getBlockId(x, y - 1, z);
		ids[1][0] = world.getBlockId(x, y + 1, z);
		ids[2][0] = world.getBlockId(x, y, z - 1);
		ids[3][0] = world.getBlockId(x, y, z + 1);
		ids[4][0] = world.getBlockId(x - 1, y, z);
		ids[5][0] = world.getBlockId(x + 1, y, z);

		ids[0][1] = world.getBlockMetadata(x, y - 1, z);
		ids[1][1] = world.getBlockMetadata(x, y + 1, z);
		ids[2][1] = world.getBlockMetadata(x, y, z - 1);
		ids[3][1] = world.getBlockMetadata(x, y, z + 1);
		ids[4][1] = world.getBlockMetadata(x - 1, y, z);
		ids[5][1] = world.getBlockMetadata(x + 1, y, z);

		return ids;
	}

	public static boolean isTouchableBlockPartitionalSolidForSideAndHasCorrectBounds(World world, int x, int y, int z, int... side)
	{
		int[][] coords = new int[][]{{0, 0, 0, 0, -1, 1}, {-1, 1, 0, 0, 0, 0}, {0, 0, -1, 1, 0, 0}};

		for(int i = 0; i < 6; i++)
		{
			boolean flag = false;

			if(side.length == 0)
			{
				flag = true;
			}
			else
			{
				for(int j = 0; j < side.length; j++)
				{
					if(i == side[j])
					{
						flag = true;
					}
				}
			}

			if(flag && world.isBlockSolidOnSide(x + coords[0][i], y + coords[1][i], z + coords[2][i], ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[ForgeDirection.getOrientation(i).ordinal()]))) return true;

			Block block = Block.blocksList[world.getBlockId(x + coords[0][i], y + coords[1][i], z + coords[2][i])];
			if((block != null) && flag)
			{
				ForgeDirection checkSide = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[ForgeDirection.getOrientation(i).ordinal()]);
				double[] bounds = new double[]{block.getBlockBoundsMinY(), block.getBlockBoundsMaxY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxZ(), block.getBlockBoundsMinX(), block.getBlockBoundsMaxX()};
				if((checkSide.ordinal() % 2) == 0? bounds[checkSide.ordinal()] == 0.0F:bounds[checkSide.ordinal()] == 1.0F) return true;
			}
		}
		return false;
	}
}
