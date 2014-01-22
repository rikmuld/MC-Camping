package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

public class KeyInfo {

	public static Map<String, Integer> keys = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();

	public static String INV = "campInventory";

	public static int getKey(String nameID)
	{
		return keys.get(nameID) != null? keys.get(nameID):-1;
	}

	public static String getKeyGameName(String nameID)
	{
		return names.get(nameID);
	}

	public static boolean put(String nameID, String gameName, int key)
	{
		if(!keys.values().contains(key))
		{
			names.put(nameID, gameName);
			keys.put(nameID, key);
			return true;
		}
		else return false;
	}

	public static void putAll()
	{
		put(INV, "Open Camping Inventory", Keyboard.KEY_C);
	}
}
