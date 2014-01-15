package rikmuld.camping.entity.tileentity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModStructures;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.core.util.MathUtil;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.inventory.slot.SlotState;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.misc.bounds.BoundsStructure;
import rikmuld.camping.misc.bounds.BoundsTracker;
import rikmuld.camping.network.packets.PacketPlayerSleepIntent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityTent extends TileEntityRotation{

	public SlotState[][] slots;
	
	public BoundsStructure[] structures;
	
	public BoundsTracker[] tracker = new BoundsTracker[4];
	public static Bounds[] bounds = new Bounds[]{new Bounds(-0.5F, 0, 0, 1.5F, 1.5F, 3), new Bounds(-2, 0, -0.5F, 1, 1.5F, 1.5F), new Bounds(-0.5F, 0, -2, 1.5F, 1.5F, 1), new Bounds(0, 0, -0.5F, 3, 1.5F, 1.5F)};
	
	boolean isNew = true;
	public boolean dropped;
	
	int[] contendList = new int[]{ModBlocks.lantern.blockID, Block.chest.blockID, ModBlocks.sleepingbag.blockID};
	
	public int maxContends = 10;
	public int chestCost = 2;
	public int bedCost = 5;
	public int lanternCost = 1;
	
	public int chests;
	public int beds;
	public int lanterns;
	
	public int maxChests = 5;
	public int maxBeds = 1;
	public int maxLanterns = 1;
		
	public int contends;
	
	public static int LANTERN = 0;
	public static int CHEST = 1;
	public static int BEDS = 2;
	
	public int time = -1;
	public int oldTime;
	public int lanternDamage = 0;
	
	public int update;
	public EntityPlayer sleepingPlayer;
	
	public boolean needLightUpdate = true;
	int lanternUpdateTick = 3;
	
	public int slide;
	public int maxSlide = 144;
	
	public int chestTracker;
	public int lanternTracker;

	public int color = 15;
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{	
			this.oldTime = time;

			if(chestTracker!=this.chests)
			{
				this.chestTracker = this.chests;
				ItemStackUtil.dropItemsInWorld(this.getExcesChestContends(), worldObj, xCoord, yCoord, zCoord);		
			}
			
			if(lanternTracker!=this.lanterns)
			{
				this.lanternTracker = this.lanterns;
				if(this.lanterns==0)
				{
					if(this.getStackInSlot(0)!=null)ItemStackUtil.dropItemInWorld(this.getStackInSlot(0), worldObj, xCoord, yCoord, zCoord);
					this.setInventorySlotContents(0, null);
				}
			}
			
			if(needLightUpdate)
			{
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
				if(lanternUpdateTick==0)this.needLightUpdate = false;
				if(lanternUpdateTick>0)lanternUpdateTick--;
			}
			
			if(isNew)this.initalize();
			
			update++;
			if(update>10&&time>0)
			{
				time--;
				update=0;
			}
			
			if(this.time==0)
			{
				this.time = -1;
				this.lanternDamage = 1;
				this.sendTileData(3, true, this.lanternDamage);

				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);	
			}
			
			if(sleepingPlayer!=null&&!sleepingPlayer.isPlayerSleeping())this.sleepingPlayer = null;
			
			if(this.time<=0&&this.getStackInSlot(0)!=null)
			{
				this.decrStackSize(0, 1);
				this.time = 1500;
				
				this.lanternDamage = 0;
				this.sendTileData(3, true, this.lanternDamage);

				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);	
			}
			if(time!=oldTime)this.sendTileData(5, true, time);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return 151;
	}
	
	public boolean addContends(ItemStack stack)
	{
		int id = stack.itemID;
		
		if(id==contendList[0])return this.addLentern(stack);
		if(id==contendList[1])return this.addChests();
		if(id==contendList[2])return this.addBed();
		return false;
	}
	
	public boolean addBed()
	{
		if(contends+bedCost<=this.maxContends&&this.beds<this.maxBeds)
		{	
			this.setContends(this.beds+1, BEDS, true, 0);
			return true;
		}
		return false;
	}
	
	public boolean removeBed()
	{
		if(this.beds>0)
		{	
			this.setContends(this.beds-1, BEDS, true, 1);
			ItemStackUtil.dropItemInWorld(this.getContendsFor(ModBlocks.sleepingbag.blockID), worldObj, xCoord, yCoord, zCoord);			
			return true;
		}
		return false;
	}
	
	public boolean addChests()
	{
		if(contends+chestCost<=this.maxContends&&this.chests<this.maxChests)
		{
			this.setContends(this.chests+1, CHEST, true, 0);
			return true;
		}
		return false;
	}
	
	public boolean removeChest()
	{
		if(this.chests>0)
		{	
			this.setContends(this.chests-1, CHEST, true, 1);
			ItemStackUtil.dropItemInWorld(this.getContendsFor(Block.chest.blockID), worldObj, xCoord, yCoord, zCoord);	
			return true;
		}
		return false;
	}
	
	private ArrayList<ItemStack> getExcesChestContends() 
	{
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for(int i = this.chests*5*6; i<150; i++)
		{
			if(this.getStackInSlot(i+1)!=null)
			{
				list.add(this.getStackInSlot(i+1));
				this.setInventorySlotContents(i+1, null);
			}
		}
		return list;
	}
	
	public boolean addLentern(ItemStack stack)
	{
		if(contends+lanternCost<=this.maxContends&&this.lanterns<this.maxLanterns)
		{
			this.time = stack.hasTagCompound()? stack.getTagCompound().getInteger("time"):-1;
			
			this.lanternDamage = this.time>0? 0:1;
			this.sendTileData(3, true, this.lanternDamage);
			
			this.setContends(this.lanterns+1, LANTERN, true, 0);
			return true;
		}
		return false;
	}
	
	public boolean removeLantern()
	{
		if(this.lanterns>0)
		{	
			this.setContends(this.lanterns-1, LANTERN, true, 1);			
			ItemStackUtil.dropItemInWorld(this.getContendsFor(ModBlocks.lantern.blockID), worldObj, xCoord, yCoord, zCoord);			
			return true;
		}
		return false;
	}
	
	public void setContends(int contendNum, int contendId, boolean sendData, int drop)
	{
		if(drop==1)ItemStackUtil.dropItemInWorld(this.getContendsFor(this.contendList[contendId]), worldObj, xCoord, yCoord, zCoord);	
		if(drop==2)ItemStackUtil.dropItemsInWorld(this.getContends(), worldObj, xCoord, yCoord, zCoord);

		if(sendData)this.sendTileData(1, !worldObj.isRemote, contendNum, contendId, drop);

		if(contendId==LANTERN)this.lanterns = contendNum;
		if(contendId==CHEST)this.chests= contendNum;
		if(contendId==BEDS)this.beds = contendNum;
		
		this.contends = beds*bedCost+chests*chestCost+lanterns*lanternCost;
		this.sendTileData(2, !worldObj.isRemote, contends);
				
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);	
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		super.setTileData(id, data);
		
		if(id==1)this.setContends(data[0], data[1], false, data[2]);	
		if(id==2)this.contends = data[0];
		if(id==3)this.lanternDamage = data[0];	
		if(id==4)
		{
			this.slide = data[0];
			this.manageSlots();
		}
		if(id==5)this.time = data[0];
		if(id==6)this.color = data[0];
	}
	
	public void manageSlots()
	{		
		if(this.slots!=null)
		{
			if(chests>2)
			{
				int scaledSlide = (int) MathUtil.getScaledNumber(this.slide, 144, 5*this.chests-11);
				
				for(int i = 0; i<5*this.chests; i++)
				{
					for(int j = 0; j<6; j++)
					{			
						this.slots[i][j].setStateX(scaledSlide);			
	
						if(i<scaledSlide||i>=scaledSlide+11)this.slots[i][j].disable();
						else this.slots[i][j].enable();
					}
				}
			}
			else
			{
				for(int i = 0; i<5*this.chests; i++)
				{
					for(int j = 0; j<6; j++)
					{
						this.slots[i][j].setStateX(0);
						this.slots[i][j].enable();
					}
				}
			}
		}
	}

	public ArrayList<ItemStack> getContends()
	{
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				
		ItemStack lanternStack = new ItemStack(this.contendList[0], this.lanterns, time>0? 0:1);
	
		if(time>0)
		{
			lanternStack.setTagCompound(new NBTTagCompound());
			lanternStack.getTagCompound().setInteger("time", this.time);		
		}
		
		if(this.lanterns>0)stacks.add(lanternStack);
		if(this.chests>0)stacks.add(new ItemStack(this.contendList[1], this.chests, 0));
		if(this.beds>0)stacks.add(new ItemStack(this.contendList[2], this.beds, 0));

		return stacks;
	}
	
	public ItemStack getContendsFor(int id)
	{	
		if(id==ModBlocks.lantern.blockID)
		{
			ItemStack lanternStack = new ItemStack(this.contendList[0], this.lanterns, time>0? 0:1);

			if(time>0)
			{
				lanternStack.setTagCompound(new NBTTagCompound());
				lanternStack.getTagCompound().setInteger("time", this.time);		
			}
			
			return lanternStack;
		}		
		if(id==Block.chest.blockID) return new ItemStack(this.contendList[1], 1, 0);
		if(id==ModBlocks.sleepingbag.blockID) return new ItemStack(this.contendList[2], 1, 0);

		return null;
	}
	
	public void removeAll()
	{
		setContends(0, 0, true, 2);
		setContends(0, 1, true, 0);
		setContends(0, 2, true, 0);
	}
	
	public void initalize()
	{
		if(!worldObj.isRemote)
		{
			this.structures = ModStructures.tent;
			for(int i = 0; i<4; i++)tracker[i] = new BoundsTracker(xCoord, yCoord, zCoord, bounds[i]);
			this.isNew = false;
		}
	}
	
	public void setRotation(int rotation)
	{				
		if(!worldObj.isRemote)
		{
			if(isNew)this.initalize();
			else structures[this.rotation].destroyStructure(worldObj, tracker[this.rotation]);
			
			this.rotation = rotation; 		
			this.sendTileData(0, true, this.rotation);		
			
			structures[this.rotation].createStructure(worldObj, tracker[this.rotation]);
		}
	}
	
	public void setColor(int color)
	{				
		if(!worldObj.isRemote)
		{
			this.color = color;
			this.sendTileData(6, true, color);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        Bounds bound = this.bounds[this.rotation];
        bb.getBoundingBox(bound.xMin, bound.yMin, bound.zMin, bound.xMax, bound.yMax, bound.zMax);
        return bb;
    }
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
				
		this.contends = tag.getInteger("contends");
		this.beds = tag.getInteger("beds");
		this.lanterns = tag.getInteger("lanterns");
		this.chests = tag.getInteger("chests");
		this.lanternDamage = tag.getInteger("lanternDamage");
		this.time = tag.getInteger("time");
		this.color = tag.getInteger("color");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		
		tag.setInteger("contends", this.contends);
		tag.setInteger("beds", this.beds);
		tag.setInteger("lanterns", this.lanterns);
		tag.setInteger("chests", this.chests);
		tag.setInteger("lanternDamage", lanternDamage);
		tag.setInteger("time", time);
		tag.setInteger("color", color);
	}
	
	public void sleep(EntityPlayer player)
	{		
		if(!worldObj.isRemote)
		{
			if(this.sleepingPlayer==null)
			{
				EnumStatus state = null;
				
				if(this.rotation==0)state = player.sleepInBedAt(xCoord, yCoord, zCoord+1);
				if(this.rotation==1)state = player.sleepInBedAt(xCoord-1, yCoord, zCoord);
				if(this.rotation==2)state = player.sleepInBedAt(xCoord, yCoord, zCoord-1);
				if(this.rotation==3)state = player.sleepInBedAt(xCoord+1, yCoord, zCoord);

				if(state!=EnumStatus.OK)
				{
					if(state==EnumStatus.NOT_POSSIBLE_NOW)player.addChatMessage("tile.bed.noSleep");
					else if(state==EnumStatus.NOT_SAFE)player.addChatMessage("tile.bed.notSafe");
				}
			}
			else player.addChatMessage("This sleeping bag is occupied!");
		}
		else PacketUtil.sendToSever(new PacketPlayerSleepIntent(xCoord, yCoord, zCoord));
	}

	public void setSlideState(int slideState) 
	{
		this.slide = slideState;
		this.manageSlots();
		
		this.sendTileData(4, false, slideState);
	}
	
	public void setSlots(SlotState[][] slots)
	{
		this.slots = slots;
	}
}
