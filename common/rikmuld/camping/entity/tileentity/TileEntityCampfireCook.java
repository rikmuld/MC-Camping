package rikmuld.camping.entity.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.misc.cooking.CookingEquipment;
import rikmuld.camping.misc.cooking.CookingEquipmentList;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketItems;
import cpw.mods.fml.common.network.PacketDispatcher;
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
		for(int i = 0; i<20; i++)
		{
			coals[0][i] = rand.nextFloat()/5F;
			coals[1][i] = rand.nextFloat()/5F;
			coals[2][i] = rand.nextFloat()*360;
		}
	}
	
	@Override
	public void updateEntity()
	{
		manageCookingEquipment();
		if(!worldObj.isRemote)
		{
			active = fuel>0;
			manageFuel();
			cookFood();	
			manageLight();
			oldActive = active;
		}
	}
	
	public void manageLight()
	{			
		if(active!=oldActive)update=3;
		if(update>0)
		{
			update--;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);	
		}
	}

	private void cookFood()
	{
		if(this.equipment!=null)
		{
			for(int i = 0; i<this.equipment.maxFood; i++)
			{
				if(this.fuel>0)
				{
					if(this.getStackInSlot(i+2)!=null&&(this.getStackInSlot(i+2).itemID!=ModItems.parts.itemID||this.getStackInSlot(i+2).getItemDamage()!=ItemParts.ASH))this.cookProgress[i]++;
				}
				else
				{
					if(this.cookProgress[i]>0)this.cookProgress[i] = 0;
				}
				if(this.getStackInSlot(i+2)==null&&this.cookProgress[i]>0)this.cookProgress[i] = 0;
				
				if(this.cookProgress[i]>=this.equipment.cookTime)
				{
					if(this.equipment.canCook(this.getStackInSlot(i+2).itemID, this.getStackInSlot(i+2).getItemDamage()))
					{
						this.setInventorySlotContents(i+2,  this.equipment.cookableFoood.get(Arrays.asList(getStackInSlot(i+2).itemID, getStackInSlot(i+2).getItemDamage())).copy());
						PacketDispatcher.sendPacketToAllPlayers(PacketTypeHandler.populatePacket(new PacketItems(i+2, xCoord, yCoord, zCoord, this.getStackInSlot(i+2))));
					}
					else
					{
						this.setInventorySlotContents(i+2, new ItemStack(ModItems.parts, 1, ItemParts.ASH));
						PacketDispatcher.sendPacketToAllPlayers(PacketTypeHandler.populatePacket(new PacketItems(i+2, xCoord, yCoord, zCoord, this.getStackInSlot(i+2))));
					}					
					this.cookProgress[i] = 0;
				}
			}
		}
	}

	public void manageCookingEquipment()
	{
		if(equipment==null&&this.getStackInSlot(1)!=null)this.equipment = CookingEquipmentList.getCooking(this.getStackInSlot(1));
		else if(equipment!=null&&this.getStackInSlot(1)==null)this.equipment = null;
		
		if(slots!=null)
		{
			if(equipment!=null)
			{
				for(int i = 0; i<this.equipment.maxFood; i++)
				{
					if(!this.slots.get(i).active)this.slots.get(i).activate(this.equipment.slots[0][i], this.equipment.slots[1][i], this.equipment.cookableFoood.keySet());
				}
			}
			
			if(equipment==null)
			{
				for(int i = 0; i<10; i++)
				{
					if(this.slots.get(i).active)
					{
						this.slots.get(i).deActivate();
						if(this.slots.get(i).getStack()!=null)
						{
							ItemStackUtil.dropItemsInWorld(new ItemStack[]{this.slots.get(i).getStack()}, worldObj, xCoord, yCoord, zCoord);
							this.slots.get(i).putStack(null);
						}
					}
				}
			}
		}
	}

	public void manageFuel()
	{
		if(fuel>0)
		{
			fuel--;			
			this.sendTileData(0, true, fuel);
		}
		if(fuel+fuelForCoal<=maxFeul&&this.getStackInSlot(0)!=null)
		{			
			this.decrStackSize(0, 1);
			this.fuel+=fuelForCoal;
		}
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)this.fuel = data[0];
	}
	
	@Override
	public int getSizeInventory()
	{
		return 12;
	}
	
	public float getScaledCoal(int maxPixels)
	{
		return ((float)this.fuel/(float)this.maxFeul)*(float)maxPixels;
	}
	
	public float getScaledcookProgress(int maxPixels, int foodNum)
	{
		return (((float)this.cookProgress[foodNum]+1)/(float)this.equipment.cookTime)*(float)maxPixels;
	}
	
	public int getCoalPieces()
	{
		return fuel>0? ((this.fuel/this.fuelForCoal)+1<=20? this.fuel/this.fuelForCoal+1:20):0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		fuel = tag.getInteger("fuel");
		cookProgress=tag.getIntArray("cookProgress");
		
		for(int i = 0; i<coals.length; i++)
		{
			for(int j = 0; j<coals[i].length; j++)
			{
				coals[i][j]=tag.getFloat("coals"+i+j);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("fuel", fuel);
		tag.setIntArray("cookProgress", cookProgress);

		for(int i = 0; i<coals.length; i++)
		{
			for(int j = 0; j<coals[i].length; j++)
			{
				tag.setFloat("coals"+i+j, coals[i][j]);
			}
		}
	}

	public void setSlots(ArrayList<SlotCooking> slots)
	{
		this.slots = slots;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
	}
}