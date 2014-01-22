package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class BlockInfo {

	static int startId = 2120;

	public static Map<String, Integer> devIDs = new HashMap<String, Integer>();
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();

	public static final String CAMPFIRE_BASE = "campfireBase";
	public static final String CAMPFIRE_DECO_NAME = "campfireDeco";
	public static final String LIGHT = "lightBlock";
	public static final String HEMP = "plantHemp";
	public static final String LOG = "logSeat";
	public static final String LANTERN = "lantern";
	public static final String SLEEPING = "sleepingBag";
	public static final String BERRY = "berry";
	public static final String TENT = "tent";
	public static final String BOUNDS = "bounds";
	public static final String SAPLING = "berrySapling";
	public static final String BEARTRAP = "trapBear";
	public static final String WIRE = "barbedWire";
	public static final String THROPHY = "throphyAntler";

	public static int id(String name)
	{
		return IDs.get(name) != null? IDs.get(name):-1;
	}

	public static String name(String name)
	{
		return names.get(name);
	}

	public static void put(String id, String name)
	{
		devIDs.put(id, startId++);
		names.put(id, name);
	}

	public static void putAll()
	{
		put(BERRY, "Berry Leaves");
		put(SAPLING, "Berry Tree Sapling");
		put(CAMPFIRE_BASE, "Campfire Base");
		put(CAMPFIRE_DECO_NAME, "Decoration Campfire");
		put(LIGHT, "Light Block");
		put(HEMP, "Hemp");
		put(LOG, "Log Seat");
		put(LANTERN, "Lantern");
		put(SLEEPING, "Sleeping Bag");
		put(TENT, "Tent");
		put(BOUNDS, "Bounds Helper Block");
		put(BEARTRAP, "Bear Trap");
		put(WIRE, "Barbed Wire");
		put(THROPHY, "Antler Throphy");
	}
}
