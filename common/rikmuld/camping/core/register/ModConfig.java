package rikmuld.camping.core.register;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoDouble;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoInteger;
import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.lib.ModInfo;

public class ModConfig {

	public static Configuration config;

	public static void init(File file)
	{
		config = new Configuration(file);
		
		ConfigInfo.putAll();
		BlockInfo.putAll();
		ItemInfo.putAll();
		
		try
		{
			config.load();
			
			for(int i = 0; i<BlockInfo.devIDs.size(); i++)BlockInfo.IDs.put((String)BlockInfo.names.keySet().toArray()[i], config.getBlock((String)BlockInfo.names.values().toArray()[i], (int)BlockInfo.devIDs.values().toArray()[i]).getInt((int)BlockInfo.devIDs.values().toArray()[i]));
			for(int i = 0; i<ItemInfo.devIDs.size(); i++)ItemInfo.IDs.put((String)ItemInfo.names.keySet().toArray()[i], config.getItem((String)ItemInfo.names.values().toArray()[i], (int)ItemInfo.devIDs.values().toArray()[i]).getInt((int)ItemInfo.devIDs.values().toArray()[i]));			
			for(int i = 0; i<ConfigInfoBoolean.devValues.size(); i++)ConfigInfoBoolean.values.put((String)ConfigInfoBoolean.names.keySet().toArray()[i], config.get((String)ConfigInfoBoolean.catagories.values().toArray()[i], (String)ConfigInfoBoolean.names.values().toArray()[i], (boolean)ConfigInfoBoolean.devValues.values().toArray()[i]).getBoolean((boolean)ConfigInfoBoolean.devValues.values().toArray()[i]));
			for(int i = 0; i<ConfigInfoInteger.devValues.size(); i++)ConfigInfoInteger.values.put((String)ConfigInfoInteger.names.keySet().toArray()[i], config.get((String)ConfigInfoInteger.catagories.values().toArray()[i], (String)ConfigInfoInteger.names.values().toArray()[i], (int)ConfigInfoInteger.devValues.values().toArray()[i]).getInt((int)ConfigInfoInteger.devValues.values().toArray()[i]));
			for(int i = 0; i<ConfigInfoDouble.devValues.size(); i++)ConfigInfoDouble.values.put((String)ConfigInfoDouble.names.keySet().toArray()[i], config.get((String)ConfigInfoDouble.catagories.values().toArray()[i], (String)ConfigInfoDouble.names.values().toArray()[i], (float)ConfigInfoDouble.devValues.values().toArray()[i]).getDouble((double)ConfigInfoDouble.devValues.values().toArray()[i]));
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

	}
	
	public static void loadItemIds()
	{

	}
}
