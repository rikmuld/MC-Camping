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
		NO KNOWN BUGS!!!!!!!!!!!!!!!!!!
	}
	UNFINISHED
	{
		BERRY TREES -- SAPLINGS
		CAMPINGGUIDE (MANY THINGS ALSO UPDATE THE LANTERN PART)
	}
	NEXT UP
	{
		MOB TRAPS
		MORE MODS
		
		MORE FOOD
		
		TENTS
		
		FINISH UNFINISHED
		
		CAMPING ARMOR
	}
	
	More food
	{

	- more vegtreables to cook in pan
		{
			-corn (mais plant -harvest> corn -campers tool> mais -pan> popcorn)
			-radish -pan> baked radish
		}
	- berries
	- more flesh
		{
		- more animals
					denk aan: herten, konijnen, beeren, vossen
		- traps to catch animals
			{
			zouden traps leuk zijn, je stopt er wat food in en als er geen player in een reach van 40 blockjes is heeft de trap elke 25 seconden 10% kan voor een random animal of er in vast te komen. 
			en de trap gaat dicht als er een entity op staat
			}
		}
	- marshmallows

	}

	Camp
	{

	-Tents 
		{
		- storage tents
		- sleeping tents
		- master tent with many uses
		}
	}
	
	
	marshmallow
	marshmallowstick
	cooked marshmallows
	
	radish
	backed radish
	backed carots
	backed pumpkin
	
	Beer meat
	deer meet
	vos meet
	rabit meet
	
	rabits food -> for luck
	vos fur for fur armor
	
	trap
	
	sitable log
	sleeping bag
	tent
	
	berry 1
	berry 2
	berry 3
	
	camping armor 1
	camping armor 2
	camping armor 3
	camping armor 4
	 */
}
