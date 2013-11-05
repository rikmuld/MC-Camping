package rikmuld.camping.core.register;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.lib.ModInfo;

public class ModConfig {

	public static Configuration config;

	public static void init(File file)
	{
		config = new Configuration(file);
		
		loadBlockIds();
		loadItemIds();
		
		try
		{
			config.load();
		}
		catch(Exception e)
		{
			ModLogger.log(Level.SEVERE, ModInfo.MOD_NAME+" could not load its configuration properly.");
		}
		finally
		{
			config.save();
		}
	}
	
	public static void loadBlockIds()
	{
		BlockInfo.putAll();

		for(int i = 0; i<BlockInfo.devIDs.size(); i++)
		{
			BlockInfo.IDs.put((String)BlockInfo.names.keySet().toArray()[i], config.getBlock((String)BlockInfo.names.values().toArray()[i], (int)BlockInfo.devIDs.values().toArray()[i]).getInt((int)BlockInfo.devIDs.values().toArray()[i]));
		}
	}
	
	public static void loadItemIds()
	{
		ItemInfo.putAll();

		for(int i = 0; i<ItemInfo.devIDs.size(); i++)
		{
			ItemInfo.IDs.put((String)ItemInfo.names.keySet().toArray()[i], config.getItem((String)ItemInfo.names.values().toArray()[i], (int)ItemInfo.devIDs.values().toArray()[i]).getInt((int)ItemInfo.devIDs.values().toArray()[i]));
		}
	}
}
