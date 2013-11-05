package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class BlockInfo {

	public static Map<String, Integer> devIDs = new HashMap<String, Integer>();
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();

	public static final String CAMPFIRE_BASE = "campfireBase";
	public static final String CAMPFIRE_DECO_NAME = "campfireDeco";
	public static final String LIGHT = "lightBlock";
	public static final String HEMP = "plantHemp";

	public static void putAll()
	{
		devIDs.put(CAMPFIRE_BASE, 2000);
		devIDs.put(CAMPFIRE_DECO_NAME, 2001);
		devIDs.put(LIGHT, 2002);
		devIDs.put(HEMP, 2003);

		names.put(CAMPFIRE_BASE, "Campfire Base");
		names.put(CAMPFIRE_DECO_NAME, "Decoration Campfire");
		names.put(LIGHT, "Light Block");
		names.put(HEMP, "Hemp");
	}
	
	public static int id(String name)
	{
		return IDs.get(name);
	}
	
	public static String name(String name)
	{
		return names.get(name);
	}
}
