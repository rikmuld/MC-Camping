package rikmuld.camping.core.register;

import net.minecraft.block.Block;
import rikmuld.camping.block.BlockCampfireCook;
import rikmuld.camping.block.BlockCampfireDeco;
import rikmuld.camping.block.BlockLight;
import rikmuld.camping.block.BlockLog;
import rikmuld.camping.block.plant.BlockFlowerHemp;
import rikmuld.camping.core.lib.BlockInfo;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModBlocks {

	public static Block campfireBase;
	public static Block campfireDeco;
	public static Block light;
	public static Block hemp;
	public static Block log;

	public static void init()
	{		
		campfireBase = new BlockCampfireCook(BlockInfo.CAMPFIRE_BASE);	
		campfireDeco = new BlockCampfireDeco(BlockInfo.CAMPFIRE_DECO_NAME);	
		light = new BlockLight(BlockInfo.LIGHT);
		hemp = new BlockFlowerHemp(BlockInfo.HEMP);
		log = new BlockLog(BlockInfo.LOG);	
	}
	
	public static void register(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		LanguageRegistry.addName(block, BlockInfo.name(name));
	}
}
