package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.BlockUtil;

public class TileEntityBarbedWire extends TileEntityMain {

	public boolean[] sides = new boolean[4];
	public boolean isBase = false;
	public int update = 20;

	public boolean canStay(ForgeDirection checkedSide, int checkLeft)
	{
		if(!isBase)
		{
			checkLeft--;
			if(checkLeft <= 0) return false;

			ForgeDirection[] directions = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST};
			int[][] blocks = BlockUtil.getBlocks(worldObj, xCoord, yCoord, zCoord);

			for(ForgeDirection direction: directions)
			{
				if(!direction.equals(checkedSide))
				{
					if(blocks[direction.ordinal()][0] == ModBlocks.wireBarbed.blockID)
					{
						if(getTileForDirection(direction) != null)
						{
							TileEntityBarbedWire tile = getTileForDirection(direction);
							if(tile.canStay(ForgeDirection.values()[ForgeDirection.OPPOSITES[direction.ordinal()]], checkLeft)) return true;
						}
					}
				}
			}
			return false;
		}
		return true;
	}

	public TileEntityBarbedWire getTileForDirection(ForgeDirection direction)
	{
		switch(direction.ordinal())
		{
			case 2:
				return((worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityBarbedWire)? (TileEntityBarbedWire)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1):null);
			case 3:
				return((worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityBarbedWire)? (TileEntityBarbedWire)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1):null);
			case 4:
				return((worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityBarbedWire)? (TileEntityBarbedWire)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord):null);
			case 5:
				return((worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityBarbedWire)? (TileEntityBarbedWire)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord):null);
		}
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		for(int i = 0; i < sides.length; i++)
		{
			sides[i] = tag.getBoolean("sides" + i);
		}
		isBase = tag.getBoolean("isBase");
	}

	@Override
	public void updateEntity()
	{
		isBase = worldObj.isBlockOpaqueCube(xCoord - 1, yCoord, zCoord) || worldObj.isBlockOpaqueCube(xCoord + 1, yCoord, zCoord) || worldObj.isBlockOpaqueCube(xCoord, yCoord, zCoord + 1) || worldObj.isBlockOpaqueCube(xCoord, yCoord, zCoord - 1);
		if(update-- <= 0)
		{
			worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, 0);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		for(int i = 0; i < sides.length; i++)
		{
			tag.setBoolean("sides" + i, sides[i]);
		}
		tag.setBoolean("isBase", isBase);
	}
}
