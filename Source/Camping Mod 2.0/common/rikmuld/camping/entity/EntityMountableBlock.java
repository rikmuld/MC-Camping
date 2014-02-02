package rikmuld.camping.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import rikmuld.camping.core.register.ModBlocks;

public class EntityMountableBlock extends Entity {

	int xFlag;
	int yFlag;
	int zFlag;
	EntityPlayer player;
	int update = -1;

	public EntityMountableBlock(World world, int x, int y, int z)
	{
		super(world);

		setSize(0.25F, 0.25F);

		xFlag = x;
		yFlag = y;
		zFlag = z;

		setPosition(xFlag + 0.5F, yFlag + 0.1F, zFlag + 0.5F);
	}

	@Override
	protected void entityInit()
	{}

	@Override
	public boolean interactFirst(EntityPlayer player)
	{
		if((riddenByEntity == null) && (update == -1))
		{
			if(!worldObj.isRemote)
			{
				player.mountEntity(this);
			}
			if(worldObj.isRemote)
			{
				this.player = player;
				update = 5;
			}
		}
		return true;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(worldObj.getBlockId(xFlag, yFlag, zFlag) != ModBlocks.log.blockID)
		{
			setDead();
		}

		if(riddenByEntity != null)
		{
			if(Keyboard.isCreated() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Minecraft.getMinecraft().inGameHasFocus)
			{
				((EntityPlayer)riddenByEntity).mountEntity(null);
				riddenByEntity = null;
			}
		}

		if(worldObj.isRemote)
		{
			if(update > 0)
			{
				update--;
			}
			if(update == 0)
			{
				player.mountEntity(this);
				update = -1;
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{}
}
