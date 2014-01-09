package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.potion.Potion;

public class PotionInfo {

	public static int getNextFreePotionID()
	{
		for(int i = 0; i<Potion.potionTypes.length; i++) if(Potion.potionTypes[i]==null)return i;
		return -1;
	}
		
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();

	public static final String BLEEDING = "bleeding";

	public static void putAll()
	{
		put(BLEEDING, "Bleeding");
	}
	
	public static void put(String id, String name)
	{
		IDs.put(id, getNextFreePotionID());
		names.put(id, name);
	}
	
	public static int id(String name)
	{
		return IDs.get(name)!=null? IDs.get(name):-1;
	}
	
	public static String name(String name)
	{
		return names.get(name);
	}
}
