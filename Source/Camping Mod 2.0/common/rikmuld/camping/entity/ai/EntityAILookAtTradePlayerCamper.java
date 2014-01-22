package rikmuld.camping.entity.ai;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.entity.EntityCamper;

public class EntityAILookAtTradePlayerCamper extends EntityAIWatchClosest {

	private final EntityCamper theMerchant;

	public EntityAILookAtTradePlayerCamper(EntityCamper par1EntityCamper)
	{
		super(par1EntityCamper, EntityPlayer.class, 8.0F);
		theMerchant = par1EntityCamper;
	}

	@Override
	public boolean shouldExecute()
	{
		if(theMerchant.isTrading())
		{
			closestEntity = theMerchant.getCustomer();
			return true;
		}
		else return false;
	}
}
