package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class ItemInfo {
	
	public static Map<String, Integer> devIDs = new HashMap<String, Integer>();
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();
	
	public static final String KNIFE = "knife";
	public static final String LANTERN = "lantern";
	public static final String BACKPACK = "backpack";
	public static final String HEMP = "hemp";
	public static final String KIT = "kit";
	public static final String PARTS = "parts";

	public static void putAll()
	{
		devIDs.put(KNIFE, 4000);
		devIDs.put(BACKPACK, 4001);
		devIDs.put(LANTERN, 4002);
		devIDs.put(KIT, 4003);
		devIDs.put(HEMP, 4004);
		devIDs.put(PARTS, 4005);

		names.put(KNIFE, "Pocket Knife");
		names.put(BACKPACK, "Hiking Bag");
		names.put(LANTERN, "Lantern");
		names.put(KIT, "Campfire Kit");
		names.put(HEMP, "Hemp");
		names.put(PARTS, "Miscellaneous Items");
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
