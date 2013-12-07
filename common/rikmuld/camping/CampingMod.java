package rikmuld.camping;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.MinecraftForge;
import rikmuld.camping.core.handler.CraftHandler;
import rikmuld.camping.core.handler.EventsHandler;
import rikmuld.camping.core.handler.PlayerHandler;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.proxys.CommonProxy;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModConfig;
import rikmuld.camping.core.register.ModCookingEquipment;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.core.register.ModRecipes;
import rikmuld.camping.core.register.ModStructures;
import rikmuld.camping.core.register.ModTabs;
import rikmuld.camping.core.register.ModTileentitys;
import rikmuld.camping.network.PacketHandler;
import rikmuld.camping.world.WorldGen;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCIES)
@NetworkMod(channels = {ModInfo.PACKET_CHANEL}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class CampingMod {
	
	@Instance(ModInfo.MOD_ID)
	public static CampingMod instance;
	@SidedProxy(clientSide = ModInfo.MOD_CLIENT_PROXY, serverSide = ModInfo.MOD_SERVER_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModLogger.init();
		ModConfig.init(new File(event.getModConfigurationDirectory().getAbsolutePath()+"/The Camping Mod/"+ModInfo.MOD_ID+".cfg"));
		ModBlocks.init();
		ModItems.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerRenderers();
		proxy.registerTickHandler();
		proxy.checkVersion();
		
		NetworkRegistry.instance().registerGuiHandler(this, CampingMod.proxy);
		GameRegistry.registerPlayerTracker(new PlayerHandler());
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
		GameRegistry.registerCraftingHandler(new CraftHandler());
		GameRegistry.registerWorldGenerator(new WorldGen());

		ModTabs.init();
		ModTileentitys.init();
		ModRecipes.init();
		ModModels.init();
		ModCookingEquipment.init();
		ModStructures.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.registerGuide();
		ModLogger.log(Level.INFO, ModInfo.MOD_NAME+" is loaded successfully.");
	}
	
	/*
	
	BUGS
	{
		ON SERVER SIDE YOU SLEEP ABOVE BEDS BAG
		LANTERN RENDER IN TENTS IS WIRED
		BERRY BUSH RENDER AFTER RE_LOGGIN NOT STATE VISABLE
	}
	UNFINISHED
	{
		BERRY TREES -- SAPLINGS
		TENTS -- ASSECABLE CONTENDS, COLORABLE
		CAMPINGGUIDE (MANY THINGS ALSO UPDATE THE LANTERN PART)
		
		FIX BUGS
	}
	NEXT UP
	{
		FINISH UNFINISHED
		
		TRAPS
		MORE FOOD
		MOBS
		CAMPING ARMOR
		ACHIEVEMENTS
		MORE OPTIONS IN THE CONFIG
	}
	
	
	new vegtrables
	
	new mobs (beer, deer, rabit)
	
	fur from new mobs for fur armor
	
	traps to cash mobs
	
	fur armor 

	 */
}
