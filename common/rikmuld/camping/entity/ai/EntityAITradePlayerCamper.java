package rikmuld.camping.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import rikmuld.camping.entity.EntityCamper;

public class EntityAITradePlayerCamper extends EntityAIBase {

	private EntityCamper Camper;

	public EntityAITradePlayerCamper(EntityCamper camper)
	{
		this.Camper = camper;
		this.setMutexBits(5);
	}

	public boolean shouldExecute()
	{
		if(!this.Camper.isEntityAlive())
		{
			return false;
		}
		else if(this.Camper.isInWater())
		{
			return false;
		}
		else if(!this.Camper.onGround)
		{
			return false;
		}
		else if(this.Camper.velocityChanged)
		{
			return false;
		}
		else
		{
			EntityPlayer entityplayer = this.Camper.getCustomer();
			return entityplayer==null ? false : (this.Camper.getDistanceSqToEntity(entityplayer)>16.0D ? false : entityplayer.openContainer instanceof Container);
		}
	}

	public void startExecuting()
	{
		this.Camper.getNavigator().clearPathEntity();
	}

	public void resetTask()
	{
		this.Camper.setCustomer((EntityPlayer) null);
	}
}
