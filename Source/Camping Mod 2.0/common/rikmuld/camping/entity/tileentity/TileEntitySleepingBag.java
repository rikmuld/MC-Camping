package rikmuld.camping.entity.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.item.itemblock.ItemBlockSleepingBag;

public class TileEntitySleepingBag extends TileEntityRotation {

	private int update;
	public EntityPlayer sleepingPlayer;

	public void breakHead()
	{
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1)
		{
			if(rotation == 0)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord - 1, 0);
			}
			if(rotation == 1)
			{
				worldObj.setBlock(xCoord + 1, yCoord, zCoord, 0);
			}
			if(rotation == 2)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord + 1, 0);
			}
			if(rotation == 3)
			{
				worldObj.setBlock(xCoord - 1, yCoord, zCoord, 0);
			}
		}
	}

	private void breakIfNeeded()
	{
		if(rotation == 0)
		{
			if((worldObj.getBlockId(xCoord, yCoord, zCoord - 1) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1) != 0))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1)).rotation != 0)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 1)
		{
			if((worldObj.getBlockId(xCoord + 1, yCoord, zCoord) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord) != 0))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord)).rotation != 1)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 2)
		{
			if((worldObj.getBlockId(xCoord, yCoord, zCoord + 1) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1) != 0))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1)).rotation != 2)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 3)
		{
			if((worldObj.getBlockId(xCoord - 1, yCoord, zCoord) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord) != 0))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord)).rotation != 3)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
	}

	private void setBedBottom()
	{
		if(rotation == 2)
		{
			if(worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == 0)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord - 1, ModBlocks.sleepingbag.blockID, 1, 2);
				if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1) != null)
				{
					((TileEntityRotation)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1)).rotation = 2;
				}
			}
			else if((worldObj.getBlockId(xCoord, yCoord, zCoord - 1) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1) != ItemBlockSleepingBag.BED_FOOD))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1)).rotation != 2)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 0)
		{
			if(worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == 0)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord + 1, ModBlocks.sleepingbag.blockID, 1, 2);
				if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1) != null)
				{
					((TileEntityRotation)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1)).rotation = 0;
				}
			}
			else if((worldObj.getBlockId(xCoord, yCoord, zCoord + 1) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1) != ItemBlockSleepingBag.BED_FOOD))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1)).rotation != 0)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 1)
		{
			if(worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == 0)
			{
				worldObj.setBlock(xCoord - 1, yCoord, zCoord, ModBlocks.sleepingbag.blockID, 1, 2);
				if(worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord) != null)
				{
					((TileEntityRotation)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord)).rotation = 1;
				}
			}
			else if((worldObj.getBlockId(xCoord - 1, yCoord, zCoord) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord) != ItemBlockSleepingBag.BED_FOOD))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord)).rotation != 1)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
		if(rotation == 3)
		{
			if(worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == 0)
			{
				worldObj.setBlock(xCoord + 1, yCoord, zCoord, ModBlocks.sleepingbag.blockID, 1, 2);
				if(worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord) != null)
				{
					((TileEntityRotation)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord)).rotation = 3;
				}
			}
			else if((worldObj.getBlockId(xCoord + 1, yCoord, zCoord) != ModBlocks.sleepingbag.blockID) || (worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord) != ItemBlockSleepingBag.BED_FOOD))
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			else if(((TileEntitySleepingBag)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord)).rotation != 3)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		update++;
		if(update > 5)
		{
			update = 0;
			if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 0)
			{
				setBedBottom();
			}
			if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1)
			{
				breakIfNeeded();
			}
		}

		if(!worldObj.isRemote) if((sleepingPlayer != null) && !sleepingPlayer.isPlayerSleeping())
		{
			sleepingPlayer = null;
		}
	}
}
