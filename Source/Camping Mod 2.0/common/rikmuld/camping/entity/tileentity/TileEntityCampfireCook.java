package rikmuld.camping.entity.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.misc.cooking.CookingEquipment;
import rikmuld.camping.misc.cooking.CookingEquipmentList;
import rikmuld.camping.network.packets.PacketItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityCampfireCook extends TileEntityInventory {

	public int maxFeul = 12000;
	public int fuelForCoal = 600;
	public int fuel;
	public float[][] coals = new float[3][20];
	Random rand = new Random();

	public int[] cookProgress = new int[10];

	public CookingEquipment equipment;
	public ArrayList<SlotCooking> slots;

	private boolean active;
	private boolean oldActive;

	private int update;

	public TileEntityCampfireCook()
	{
		for(int i = 0; i < 20; i++)
		{
			coals[0][i] = rand.nextFloat() / 5F;
			coals[1][i] = rand.nextFloat() / 5F;
			coals[2][i] = rand.nextFloat() * 360;
		}
	}

	private void cookFood()
	{
		if(equipment != null)
		{
			for(int i = 0; i < equipment.maxFood; i++)
			{
				if(fuel > 0)
				{
					if((getStackInSlot(i + 2) != null) && ((getStackInSlot(i + 2).itemID != ModItems.parts.itemID) || (getStackInSlot(i + 2).getItemDamage() != ItemParts.ASH)))
					{
						cookProgress[i]++;
					}
				}
				else
				{
					if(cookProgress[i] > 0)
					{
						cookProgress[i] = 0;
					}
				}
				if((getStackInSlot(i + 2) == null) && (cookProgress[i] > 0))
				{
					cookProgress[i] = 0;
				}

				if(cookProgress[i] >= equipment.cookTime)
				{
					if(equipment.canCook(getStackInSlot(i + 2).itemID, getStackInSlot(i + 2).getItemDamage()))
					{
						setInventorySlotContents(i + 2, equipment.cookableFoood.get(Arrays.asList(getStackInSlot(i + 2).itemID, getStackInSlot(i + 2).getItemDamage())).copy());
					}
					else
					{
						setInventorySlotContents(i + 2, new ItemStack(ModItems.parts, 1, ItemParts.ASH));
					}
					PacketUtil.sendToAllPlayers(new PacketItems(i + 2, xCoord, yCoord, zCoord, getStackInSlot(i + 2)));
					cookProgress[i] = 0;
				}
			}
		}
	}

	public int getCoalPieces()
	{
		return fuel > 0? (((fuel / fuelForCoal) + 1) <= 20? (fuel / fuelForCoal) + 1:20):0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
	}

	public float getScaledCoal(int maxPixels)
	{
		return ((float)fuel / (float)maxFeul) * maxPixels;
	}

	public float getScaledcookProgress(int maxPixels, int foodNum)
	{
		return (((float)cookProgress[foodNum] + 1) / equipment.cookTime) * maxPixels;
	}

	@Override
	public int getSizeInventory()
	{
		return 12;
	}

	public void manageCookingEquipment()
	{
		if((equipment == null) && (getStackInSlot(1) != null))
		{
			equipment = CookingEquipmentList.getCooking(getStackInSlot(1));
		}
		else if((equipment != null) && (getStackInSlot(1) == null))
		{
			equipment = null;
		}

		if(slots != null)
		{
			if(equipment != null)
			{
				for(int i = 0; i < equipment.maxFood; i++)
				{
					if(!slots.get(i).active)
					{
						slots.get(i).activate(equipment.slots[0][i], equipment.slots[1][i], equipment.cookableFoood.keySet());
					}
				}
			}

			if(equipment == null)
			{
				for(int i = 0; i < 10; i++)
				{
					if(slots.get(i).active)
					{
						slots.get(i).deActivate();
						if(slots.get(i).getStack() != null)
						{
							ItemStackUtil.dropItemsInWorld(new ItemStack[]{slots.get(i).getStack()}, worldObj, xCoord, yCoord, zCoord);
							slots.get(i).putStack(null);
						}
					}
				}
			}
		}
	}

	public void manageFuel()
	{
		if(fuel > 0)
		{
			fuel--;
			sendTileData(0, true, fuel);
		}
		if(((fuel + fuelForCoal) <= maxFeul) && (getStackInSlot(0) != null))
		{
			decrStackSize(0, 1);
			fuel += fuelForCoal;
		}
	}

	public void manageLight()
	{
		if(active != oldActive)
		{
			update = 3;
		}
		if(update > 0)
		{
			update--;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		fuel = tag.getInteger("fuel");
		cookProgress = tag.getIntArray("cookProgress");

		for(int i = 0; i < coals.length; i++)
		{
			for(int j = 0; j < coals[i].length; j++)
			{
				coals[i][j] = tag.getFloat("coals" + i + j);
			}
		}
	}

	public void setSlots(ArrayList<SlotCooking> slots)
	{
		this.slots = slots;
	}

	@Override
	public void setTileData(int id, int[] data)
	{
		if(id == 0)
		{
			fuel = data[0];
		}
	}

	@Override
	public void updateEntity()
	{
		manageCookingEquipment();
		if(!worldObj.isRemote)
		{
			active = fuel > 0;
			manageFuel();
			cookFood();
			manageLight();
			oldActive = active;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("fuel", fuel);
		tag.setIntArray("cookProgress", cookProgress);

		for(int i = 0; i < coals.length; i++)
		{
			for(int j = 0; j < coals[i].length; j++)
			{
				tag.setFloat("coals" + i + j, coals[i][j]);
			}
		}
	}
}