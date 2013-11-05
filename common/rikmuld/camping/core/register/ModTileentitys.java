package rikmuld.camping.core.register;

import rikmuld.camping.core.lib.TileentityInfo;
import rikmuld.camping.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.tileentity.TileEntityCampfireCook;
import rikmuld.camping.tileentity.TileEntityLight;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileentitys {

	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityCampfireCook.class, TileentityInfo.CAMPFIRE_BASE);
		GameRegistry.registerTileEntity(TileEntityCampfireDeco.class, TileentityInfo.CAMPFIRE_DECO_NAME);
		GameRegistry.registerTileEntity(TileEntityLight.class, TileentityInfo.LIGHT);
	}
}
