package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class ItemInfo {

	static int startId = 5000;
	
	public static Map<String, Integer> devIDs = new HashMap<String, Integer>();
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();
	
	public static final String KNIFE = "knife";
	public static final String BACKPACK = "backpack";
	public static final String HEMP = "hemp";
	public static final String KIT = "kit";
	public static final String PARTS = "parts";
	public static final String ANIMAL_STUFF = "animalStuff";
	public static final String MARSHSTICK_COOKED = "marshmallowStickCooked";
	public static final String BERRIES = "berries";
	public static final String VANISON_RAW = "vanisonRaw";
	public static final String VANISON_COOKED = "vanisonCooked";
	public static final String HARE_RAW = "rabbitRaw";
	public static final String HARE_COOKED = "rabbitCooked";
	public static final String ARMOR_FUR_HELM = "armorFurHelm";
	public static final String ARMOR_FUR_CHEST = "armorFurChest";
	public static final String ARMOR_FUR_LEG = "armorFurLeg";
	public static final String ARMOR_FUR_BOOTS = "armorFurBoots";

	public static void putAll()
	{
		put(KNIFE, "Pocket Knife");
		put(BACKPACK, "Hiking Bag");
		put(KIT, "Campfire Kit");
		put(PARTS, "Miscellaneous Items");
		put(MARSHSTICK_COOKED, "Cooked Marshmallow on a Stick");
		put(ARMOR_FUR_HELM, "Fur Helmet");
		put(ARMOR_FUR_CHEST, "Fur Chestplate");
		put(ARMOR_FUR_LEG, "Fur Leggings");
		put(ARMOR_FUR_BOOTS, "Fur Boots");
		put(BERRIES, "Berries");
		put(HEMP, "Hemp");
		put(VANISON_RAW, "Raw Vanison");
		put(VANISON_COOKED, "Cooked Vanison");
		put(HARE_RAW, "Raw Hare");
		put(HARE_COOKED, "Cooked Hare");
		put(ANIMAL_STUFF, "Animal Stuff");
	}
	
	public static void put(String id, String name)
	{
		devIDs.put(id, startId++);
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
