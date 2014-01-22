package rikmuld.camping.entity.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.ItemStackUtil;

public class TileEntityAntlerThrophy extends TileEntityMain {

	public int[] block = new int[4];

	public void attatchToBlock(int x, int y, int z, int side)
	{
		block[0] = x;
		block[1] = y;
		block[2] = z;
		block[3] = side;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		block = tag.getIntArray("block");
	}

	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			if(!worldObj.isBlockOpaqueCube(block[0], block[1], block[2]))
			{
				ItemStackUtil.dropItemInWorld(new ItemStack(ModBlocks.throphy), worldObj, xCoord, yCoord, zCoord);
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setIntArray("block", block);
	}
}
