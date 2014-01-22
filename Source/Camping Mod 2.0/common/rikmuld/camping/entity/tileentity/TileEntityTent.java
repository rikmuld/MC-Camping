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

public class TileEntityTent extends TileEntityRotation {

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

	public boolean addBed()
	{
		if(((contends + bedCost) <= maxContends) && (beds < maxBeds))
		{
			setContends(beds + 1, BEDS, true, 0);
			return true;
		}
		return false;
	}

	public boolean addChests()
	{
		if(((contends + chestCost) <= maxContends) && (chests < maxChests))
		{
			setContends(chests + 1, CHEST, true, 0);
			return true;
		}
		return false;
	}

	public boolean addContends(ItemStack stack)
	{
		int id = stack.itemID;

		if(id == contendList[0]) return addLentern(stack);
		if(id == contendList[1]) return addChests();
		if(id == contendList[2]) return addBed();
		return false;
	}

	public boolean addLentern(ItemStack stack)
	{
		if(((contends + lanternCost) <= maxContends) && (lanterns < maxLanterns))
		{
			time = stack.hasTagCompound()? stack.getTagCompound().getInteger("time"):-1;

			lanternDamage = time > 0? 0:1;
			sendTileData(3, true, lanternDamage);

			setContends(lanterns + 1, LANTERN, true, 0);
			return true;
		}
		return false;
	}

	public ArrayList<ItemStack> getContends()
	{
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

		ItemStack lanternStack = new ItemStack(contendList[0], lanterns, time > 0? 0:1);

		if(time > 0)
		{
			lanternStack.setTagCompound(new NBTTagCompound());
			lanternStack.getTagCompound().setInteger("time", time);
		}

		if(lanterns > 0)
		{
			stacks.add(lanternStack);
		}
		if(chests > 0)
		{
			stacks.add(new ItemStack(contendList[1], chests, 0));
		}
		if(beds > 0)
		{
			stacks.add(new ItemStack(contendList[2], beds, 0));
		}

		return stacks;
	}

	public ItemStack getContendsFor(int id)
	{
		if(id == ModBlocks.lantern.blockID)
		{
			ItemStack lanternStack = new ItemStack(contendList[0], lanterns, time > 0? 0:1);

			if(time > 0)
			{
				lanternStack.setTagCompound(new NBTTagCompound());
				lanternStack.getTagCompound().setInteger("time", time);
			}

			return lanternStack;
		}
		if(id == Block.chest.blockID) return new ItemStack(contendList[1], 1, 0);
		if(id == ModBlocks.sleepingbag.blockID) return new ItemStack(contendList[2], 1, 0);

		return null;
	}

	private ArrayList<ItemStack> getExcesChestContends()
	{
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for(int i = chests * 5 * 6; i < 150; i++)
		{
			if(getStackInSlot(i + 1) != null)
			{
				list.add(getStackInSlot(i + 1));
				setInventorySlotContents(i + 1, null);
			}
		}
		return list;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		Bounds bound = bounds[rotation];
		AxisAlignedBB.getBoundingBox(bound.xMin, bound.yMin, bound.zMin, bound.xMax, bound.yMax, bound.zMax);
		return bb;
	}

	@Override
	public int getSizeInventory()
	{
		return 151;
	}

	public void initalize()
	{
		if(!worldObj.isRemote)
		{
			structures = ModStructures.tent;
			for(int i = 0; i < 4; i++)
			{
				tracker[i] = new BoundsTracker(xCoord, yCoord, zCoord, bounds[i]);
			}
			isNew = false;
		}
	}

	public void manageSlots()
	{
		if(slots != null)
		{
			if(chests > 2)
			{
				int scaledSlide = (int)MathUtil.getScaledNumber(slide, 144, (5 * chests) - 11);

				for(int i = 0; i < (5 * chests); i++)
				{
					for(int j = 0; j < 6; j++)
					{
						slots[i][j].setStateX(scaledSlide);

						if((i < scaledSlide) || (i >= (scaledSlide + 11)))
						{
							slots[i][j].disable();
						}
						else
						{
							slots[i][j].enable();
						}
					}
				}
			}
			else
			{
				for(int i = 0; i < (5 * chests); i++)
				{
					for(int j = 0; j < 6; j++)
					{
						slots[i][j].setStateX(0);
						slots[i][j].enable();
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		contends = tag.getInteger("contends");
		beds = tag.getInteger("beds");
		lanterns = tag.getInteger("lanterns");
		chests = tag.getInteger("chests");
		lanternDamage = tag.getInteger("lanternDamage");
		time = tag.getInteger("time");
		color = tag.getInteger("color");
	}

	public void removeAll()
	{
		setContends(0, 0, true, 2);
		setContends(0, 1, true, 0);
		setContends(0, 2, true, 0);
	}

	public boolean removeBed()
	{
		if(beds > 0)
		{
			setContends(beds - 1, BEDS, true, 1);
			ItemStackUtil.dropItemInWorld(getContendsFor(ModBlocks.sleepingbag.blockID), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	public boolean removeChest()
	{
		if(chests > 0)
		{
			setContends(chests - 1, CHEST, true, 1);
			ItemStackUtil.dropItemInWorld(getContendsFor(Block.chest.blockID), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	public boolean removeLantern()
	{
		if(lanterns > 0)
		{
			setContends(lanterns - 1, LANTERN, true, 1);
			ItemStackUtil.dropItemInWorld(getContendsFor(ModBlocks.lantern.blockID), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	public void setColor(int color)
	{
		if(!worldObj.isRemote)
		{
			this.color = color;
			sendTileData(6, true, color);
		}
	}

	public void setContends(int contendNum, int contendId, boolean sendData, int drop)
	{
		if(drop == 1)
		{
			ItemStackUtil.dropItemInWorld(getContendsFor(contendList[contendId]), worldObj, xCoord, yCoord, zCoord);
		}
		if(drop == 2)
		{
			ItemStackUtil.dropItemsInWorld(getContends(), worldObj, xCoord, yCoord, zCoord);
		}

		if(sendData)
		{
			sendTileData(1, !worldObj.isRemote, contendNum, contendId, drop);
		}

		if(contendId == LANTERN)
		{
			lanterns = contendNum;
		}
		if(contendId == CHEST)
		{
			chests = contendNum;
		}
		if(contendId == BEDS)
		{
			beds = contendNum;
		}

		contends = (beds * bedCost) + (chests * chestCost) + (lanterns * lanternCost);
		sendTileData(2, !worldObj.isRemote, contends);

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void setRotation(int rotation)
	{
		if(!worldObj.isRemote)
		{
			if(isNew)
			{
				initalize();
			}
			else
			{
				structures[this.rotation].destroyStructure(worldObj, tracker[this.rotation]);
			}

			this.rotation = rotation;
			sendTileData(0, true, this.rotation);

			structures[this.rotation].createStructure(worldObj, tracker[this.rotation]);
		}
	}

	public void setSlideState(int slideState)
	{
		slide = slideState;
		manageSlots();

		sendTileData(4, false, slideState);
	}

	public void setSlots(SlotState[][] slots)
	{
		this.slots = slots;
	}

	@Override
	public void setTileData(int id, int[] data)
	{
		super.setTileData(id, data);

		if(id == 1)
		{
			setContends(data[0], data[1], false, data[2]);
		}
		if(id == 2)
		{
			contends = data[0];
		}
		if(id == 3)
		{
			lanternDamage = data[0];
		}
		if(id == 4)
		{
			slide = data[0];
			manageSlots();
		}
		if(id == 5)
		{
			time = data[0];
		}
		if(id == 6)
		{
			color = data[0];
		}
	}

	public void sleep(EntityPlayer player)
	{
		if(!worldObj.isRemote)
		{
			if(sleepingPlayer == null)
			{
				EnumStatus state = null;

				if(rotation == 0)
				{
					state = player.sleepInBedAt(xCoord, yCoord, zCoord + 1);
				}
				if(rotation == 1)
				{
					state = player.sleepInBedAt(xCoord - 1, yCoord, zCoord);
				}
				if(rotation == 2)
				{
					state = player.sleepInBedAt(xCoord, yCoord, zCoord - 1);
				}
				if(rotation == 3)
				{
					state = player.sleepInBedAt(xCoord + 1, yCoord, zCoord);
				}

				if(state != EnumStatus.OK)
				{
					if(state == EnumStatus.NOT_POSSIBLE_NOW)
					{
						player.addChatMessage("tile.bed.noSleep");
					}
					else if(state == EnumStatus.NOT_SAFE)
					{
						player.addChatMessage("tile.bed.notSafe");
					}
				}
			}
			else
			{
				player.addChatMessage("This sleeping bag is occupied!");
			}
		}
		else
		{
			PacketUtil.sendToSever(new PacketPlayerSleepIntent(xCoord, yCoord, zCoord));
		}
	}

	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			oldTime = time;

			if(chestTracker != chests)
			{
				chestTracker = chests;
				ItemStackUtil.dropItemsInWorld(getExcesChestContends(), worldObj, xCoord, yCoord, zCoord);
			}

			if(lanternTracker != lanterns)
			{
				lanternTracker = lanterns;
				if(lanterns == 0)
				{
					if(getStackInSlot(0) != null)
					{
						ItemStackUtil.dropItemInWorld(getStackInSlot(0), worldObj, xCoord, yCoord, zCoord);
					}
					setInventorySlotContents(0, null);
				}
			}

			if(needLightUpdate)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
				if(lanternUpdateTick == 0)
				{
					needLightUpdate = false;
				}
				if(lanternUpdateTick > 0)
				{
					lanternUpdateTick--;
				}
			}

			if(isNew)
			{
				initalize();
			}

			update++;
			if((update > 10) && (time > 0))
			{
				time--;
				update = 0;
			}

			if(time == 0)
			{
				time = -1;
				lanternDamage = 1;
				sendTileData(3, true, lanternDamage);

				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			}

			if((sleepingPlayer != null) && !sleepingPlayer.isPlayerSleeping())
			{
				sleepingPlayer = null;
			}

			if((time <= 0) && (getStackInSlot(0) != null))
			{
				decrStackSize(0, 1);
				time = 1500;

				lanternDamage = 0;
				sendTileData(3, true, lanternDamage);

				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			}
			if(time != oldTime)
			{
				sendTileData(5, true, time);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setInteger("contends", contends);
		tag.setInteger("beds", beds);
		tag.setInteger("lanterns", lanterns);
		tag.setInteger("chests", chests);
		tag.setInteger("lanternDamage", lanternDamage);
		tag.setInteger("time", time);
		tag.setInteger("color", color);
	}
}
