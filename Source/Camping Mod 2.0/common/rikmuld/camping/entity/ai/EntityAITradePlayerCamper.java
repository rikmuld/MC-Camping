package rikmuld.camping.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import rikmuld.camping.entity.EntityCamper;

public class EntityAITradePlayerCamper extends EntityAIBase {

	private EntityCamper Camper;

	public EntityAITradePlayerCamper(EntityCamper camper)
	{
		Camper = camper;
		setMutexBits(5);
	}

	@Override
	public void resetTask()
	{
		Camper.setCustomer((EntityPlayer)null);
	}

	@Override
	public boolean shouldExecute()
	{
		if(!Camper.isEntityAlive()) return false;
		else if(Camper.isInWater()) return false;
		else if(!Camper.onGround) return false;
		else if(Camper.velocityChanged) return false;
		else
		{
			EntityPlayer entityplayer = Camper.getCustomer();
			return entityplayer == null? false:(Camper.getDistanceSqToEntity(entityplayer) > 16.0D? false:entityplayer.openContainer instanceof Container);
		}
	}

	@Override
	public void startExecuting()
	{
		Camper.getNavigator().clearPathEntity();
	}
}
