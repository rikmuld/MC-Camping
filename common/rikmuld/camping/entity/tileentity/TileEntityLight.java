package rikmuld.camping.entity.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.core.util.CampingInvUtil;

public class TileEntityLight extends TileEntityMain {

	int tick;
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			tick++;
			if(tick>10)
			{
				boolean flag = true;
				
				ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord-2, yCoord-2, zCoord-2, xCoord+2, yCoord+2, zCoord+2));
				for(EntityPlayer player:players)
				{
					if(CampingInvUtil.hasLantarn(player))flag = false;
				}
				if(flag)worldObj.setBlock(xCoord, yCoord, zCoord, 0);
				if(!flag)tick=0;
			}
		}
	}
}
