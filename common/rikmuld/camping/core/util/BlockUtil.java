package rikmuld.camping.core.util;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUtil {

	static Random rand = new Random();

	public static void dropItems(World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity instanceof IInventory)
			{
				IInventory inventory = (IInventory) tileEntity;
				for(int i = 0; i<inventory.getSizeInventory(); i++)
				{
					ItemStack itemStack = inventory.getStackInSlot(i);
					if(itemStack!=null&&itemStack.stackSize>0)
					{
						float dX = rand.nextFloat()*0.8F+0.1F;
						float dY = rand.nextFloat()*0.8F+0.1F;
						float dZ = rand.nextFloat()*0.8F+0.1F;
						EntityItem entityItem = new EntityItem(world, x+dX, y+dY, z+dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));
						if(itemStack.hasTagCompound())
						{
							entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
						}
						float factor = 0.05F;
						entityItem.motionX = rand.nextGaussian()*factor;
						entityItem.motionY = rand.nextGaussian()*factor+0.2F;
						entityItem.motionZ = rand.nextGaussian()*factor;
						world.spawnEntityInWorld(entityItem);
						itemStack.stackSize = 0;
					}
				}
			}
		}
	}
}
