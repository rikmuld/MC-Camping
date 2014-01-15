package rikmuld.camping.core.register;

import java.io.File;
import java.util.Iterator;
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
	
			loadBlockIds();
			loadItemIds();
			loadBooleanOptions();
			loadIntegerOptions();
			loadDoubleOptions();
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
	
	public static void loadBooleanOptions()
	{
		Iterator<String> nameIDs = ConfigInfoBoolean.names.keySet().iterator();
		Iterator<String> gameNames = ConfigInfoBoolean.names.values().iterator();
		Iterator<Boolean> values = ConfigInfoBoolean.devValues.values().iterator();
		Iterator<String> types = ConfigInfoBoolean.catagories.values().iterator();
		
		while(nameIDs.hasNext())
		{
			String type = types.next();
			boolean value = values.next();
			String name = nameIDs.next();
			String gameName = gameNames.next();
			
			ConfigInfoBoolean.values.put(name, config.get(type, gameName, value).getBoolean(value));
		}
	}
	
	public static void loadIntegerOptions()
	{
		Iterator<String> nameIDs = ConfigInfoInteger.names.keySet().iterator();
		Iterator<String> gameNames = ConfigInfoInteger.names.values().iterator();
		Iterator<Integer> values = ConfigInfoInteger.devValues.values().iterator();
		Iterator<String> types = ConfigInfoInteger.catagories.values().iterator();
		
		while(nameIDs.hasNext())
		{
			String type = types.next();
			int value = values.next();
			String name = nameIDs.next();
			String gameName = gameNames.next();
			
			ConfigInfoInteger.values.put(name, config.get(type, gameName, value).getInt(value));
		}
	}
	
	public static void loadDoubleOptions()
	{
		Iterator<String> nameIDs = ConfigInfoDouble.names.keySet().iterator();
		Iterator<String> gameNames = ConfigInfoDouble.names.values().iterator();
		Iterator<Double> values = ConfigInfoDouble.devValues.values().iterator();
		Iterator<String> types = ConfigInfoDouble.catagories.values().iterator();

		while(nameIDs.hasNext())
		{
			String type = types.next();
			double value = values.next();
			String name = nameIDs.next();
			String gameName = gameNames.next();
			
			ConfigInfoDouble.values.put(name, config.get(type, gameName, value).getDouble(value));
		}
	}
	
	public static void loadBlockIds()
	{
		Iterator<String> nameIDs = BlockInfo.names.keySet().iterator();
		Iterator<String> gameNames = BlockInfo.names.values().iterator();
		Iterator<Integer> IDs = BlockInfo.devIDs.values().iterator();
		
		while(nameIDs.hasNext())
		{
			int ID = IDs.next();
			String name = nameIDs.next();
			String gameName = gameNames.next();
			
			BlockInfo.IDs.put(name, config.getBlock(gameName, ID).getInt(ID));
		}
	}
	
	public static void loadItemIds()
	{
		Iterator<String> nameIDs = ItemInfo.names.keySet().iterator();
		Iterator<String> gameNames = ItemInfo.names.values().iterator();
		Iterator<Integer> IDs = ItemInfo.devIDs.values().iterator();

		while(nameIDs.hasNext())
		{
			int ID = IDs.next();
			String name = nameIDs.next();
			String gameName = gameNames.next();
			
			ItemInfo.IDs.put(name, config.getItem(gameName, ID).getInt(ID));
		}
	}
}
