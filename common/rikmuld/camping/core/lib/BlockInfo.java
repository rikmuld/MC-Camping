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
	public static final String LOG = "logSeat";
	public static final String LANTERN = "lantern1";

	public static void putAll()
	{
		devIDs.put(CAMPFIRE_BASE, 2000);
		devIDs.put(CAMPFIRE_DECO_NAME, 2001);
		devIDs.put(LIGHT, 2002);
		devIDs.put(HEMP, 2003);
		devIDs.put(LOG, 2004);
		devIDs.put(LANTERN, 2005);

		names.put(CAMPFIRE_BASE, "Campfire Base");
		names.put(CAMPFIRE_DECO_NAME, "Decoration Campfire");
		names.put(LIGHT, "Light Block");
		names.put(HEMP, "Hemp");
		names.put(LOG, "Log Seat");
		names.put(LANTERN, "Lantern");
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
