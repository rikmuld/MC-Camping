package rikmuld.camping.core.register;

import rikmuld.camping.core.lib.TileentityInfo;
import rikmuld.camping.entity.tileentity.TileEntityBerry;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.entity.tileentity.TileEntityLantern;
import rikmuld.camping.entity.tileentity.TileEntityLight;
import rikmuld.camping.entity.tileentity.TileEntityLog;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.entity.tileentity.TileEntitySleepingBag;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileentitys {

	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityCampfireCook.class, TileentityInfo.CAMPFIRE_BASE);
		GameRegistry.registerTileEntity(TileEntityCampfireDeco.class, TileentityInfo.CAMPFIRE_DECO_NAME);
		GameRegistry.registerTileEntity(TileEntityLight.class, TileentityInfo.LIGHT);
		GameRegistry.registerTileEntity(TileEntityLog.class, TileentityInfo.LOG);
		GameRegistry.registerTileEntity(TileEntityLantern.class, TileentityInfo.LANTERN);
		GameRegistry.registerTileEntity(TileEntityRotation.class, TileentityInfo.ROTATION);
		GameRegistry.registerTileEntity(TileEntitySleepingBag.class, TileentityInfo.SLEEPING);
		GameRegistry.registerTileEntity(TileEntityBerry.class, TileentityInfo.BERRY);
		GameRegistry.registerTileEntity(TileEntityTent.class, TileentityInfo.TENT);
		GameRegistry.registerTileEntity(TileEntityBounds.class, TileentityInfo.BOUNDS);
	}
}
