package rikmuld.camping.core.register;

import rikmuld.camping.core.lib.TileentityInfo;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.entity.tileentity.TileEntityLight;
import rikmuld.camping.entity.tileentity.TileEntityLog;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileentitys {

	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityCampfireCook.class, TileentityInfo.CAMPFIRE_BASE);
		GameRegistry.registerTileEntity(TileEntityCampfireDeco.class, TileentityInfo.CAMPFIRE_DECO_NAME);
		GameRegistry.registerTileEntity(TileEntityLight.class, TileentityInfo.LIGHT);
		GameRegistry.registerTileEntity(TileEntityLog.class, TileentityInfo.LOG);
	}
}
