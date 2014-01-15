package rikmuld.camping.core.register;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import rikmuld.camping.block.BlockAntlerThrophy;
import rikmuld.camping.block.BlockBerryLeaves;
import rikmuld.camping.block.BlockBoundsHelper;
import rikmuld.camping.block.BlockCampfireCook;
import rikmuld.camping.block.BlockCampfireDeco;
import rikmuld.camping.block.BlockLantern;
import rikmuld.camping.block.BlockLight;
import rikmuld.camping.block.BlockLog;
import rikmuld.camping.block.BlockSapling;
import rikmuld.camping.block.BlockSleepingBag;
import rikmuld.camping.block.BlockTent;
import rikmuld.camping.block.BlockTrap;
import rikmuld.camping.block.BlockWireBarbed;
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
	public static Block lantern;
	public static Block sleepingbag;
	public static Block leaves;
	public static Block tent;
	public static Block bounds;
	public static Block sapling;
	public static Block bearTrap;
	public static Block wireBarbed;
	public static Block throphy;

	public static void init()
	{		
		campfireBase = new BlockCampfireCook(BlockInfo.CAMPFIRE_BASE);	
		campfireDeco = new BlockCampfireDeco(BlockInfo.CAMPFIRE_DECO_NAME);	
		light = new BlockLight(BlockInfo.LIGHT);
		hemp = new BlockFlowerHemp(BlockInfo.HEMP);
		log = new BlockLog(BlockInfo.LOG);	
		lantern = new BlockLantern(BlockInfo.LANTERN);
		sleepingbag = new BlockSleepingBag(BlockInfo.SLEEPING);
		leaves = new BlockBerryLeaves(BlockInfo.BERRY);
		tent = new BlockTent(BlockInfo.TENT);	
		bounds = new BlockBoundsHelper(BlockInfo.BOUNDS);
		sapling = new BlockSapling(BlockInfo.SAPLING);
		bearTrap = new BlockTrap(BlockInfo.BEARTRAP);
		wireBarbed = new BlockWireBarbed(BlockInfo.WIRE);
		throphy = new BlockAntlerThrophy(BlockInfo.THROPHY);
	}
	
	public static void register(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		LanguageRegistry.addName(block, BlockInfo.name(name));
	}

	public static void registerWithMeta(Block block, String name, Class<? extends ItemBlock> itemBlock)
	{
		GameRegistry.registerBlock(block, itemBlock, name);
	}
}
