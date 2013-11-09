package rikmuld.camping.entity;

import org.lwjgl.input.Keyboard;

import rikmuld.camping.core.register.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityMountableBlock extends Entity{

	int xFlag;
	int yFlag;
	int zFlag;
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(this.worldObj.getBlockId(xFlag, yFlag, zFlag)!=ModBlocks.log.blockID)this.setDead();
		
		if(this.riddenByEntity!=null)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				((EntityPlayer)this.riddenByEntity).mountEntity(null);
				this.riddenByEntity = null;
			}
		}
	}
	
	public EntityMountableBlock(World world, int x, int y, int z)
	{
		super(world);
		
		this.setSize(0.25F, 0.25F);
		
		this.xFlag = x;
		this.yFlag = y;
		this.zFlag = z;
		
		this.setPosition(xFlag+0.5F, yFlag+0.1F, zFlag+0.5F);
	}	

	@Override
	protected void entityInit()
	{}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{}
	
	@Override
	public boolean interactFirst(EntityPlayer player)
    {
        if (this.riddenByEntity != null)
        {
            return false;
        }
        else
        {
        	player.mountEntity(this);
            return true;
        }
    }
}
