package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class AchievementInfo {

	static int startId = 2120;
	
	public static Map<String, Integer> IDs = new HashMap<String, Integer>();
	public static Map<String, Integer> devIDs = new HashMap<String, Integer>();
	public static Map<String, String> names = new HashMap<String, String>();
	public static Map<String, String> desc = new HashMap<String, String>();

	public static final String ENCOUNTER_CAMPER = "general.camper";
	public static final String BERRY1 = "general.berry1";
	public static final String BERRY2 = "general.berry2";
	public static final String GUIDE = "general.guide";

	public static final String KNIVE = "camper.knive";
	public static final String HEMP = "camper.hemp";
	public static final String BAG = "camper.bag";
	public static final String BASE = "camper.base";
	public static final String CANVAS = "camper.canvas";
	public static final String CAMPFIRE_COOK = "camper.cook";
	public static final String CAMPFIRE_DECO = "camper.deco";
	public static final String CAMPFIRE_DECO_EFFECT = "camper.effect";
	public static final String TENT = "camper.tent";
	public static final String TENT_STORE = "camper.store";
	public static final String TENT_SLEEP = "camper.sleep";
	public static final String TENT_LIGHT = "camper.tentLight";
	public static final String LOG = "camper.log";
	public static final String LIGHT = "camper.light";
	public static final String BAG_SLEEP = "camper.bagSleep";
	public static final String KIT = "camper.kit";
	public static final String SPIT = "camper.spit";
	public static final String GRILL = "camper.grill";
	public static final String PAN = "camper.pan";

	public static final String BEAR = "hunting.bear";
	public static final String DEER = "hunting.deer";
	public static final String HARE = "hunting.hare";
	public static final String HARE_ARMOR = "hunting.hareArmor";
	public static final String DEER_THROPHY = "hunting.deerThrophy";
	public static final String TRAP_LUCKEY = "hunting.trapLuckey";
	public static final String TRAP_BAIT = "hunting.trapBait";
	public static final String TRAP = "hunting.trap";
	
	public static final String CAMPER = "master.camper";
	public static final String HUNTER = "master.hunter";
	public static final String GENERAL = "master.general";
	public static final String LEGEND = "master.legend";

	public static void putAll()
	{
		put(GUIDE, "G: First Things First!", "Read In The Camping Guide!");
		put(ENCOUNTER_CAMPER, "G: Fateful Encounter!", "Talk With A Camper!");
		put(BERRY1, "G: Red Sweetness!", "Pluck a Raspberry!");
		put(BERRY2, "G: Black Sweetness!", "Pluck a Blackberry!");

		put(KNIVE, "C: Getting Started!", "Create a Pocket Knife!");
		put(HEMP, "C: And This Is For?!", "Find Some Hemp!");
		put(CANVAS, "C: So Thats Were It's Used For!", "Create Some Canvas!");
		put(BAG, "C: Hiking!", " Create a Backpack!");
		put(BASE, "C: Almost There!", "Create a Campfire Base!");
		put(TENT, "C: Tentastic!", "Create a Tent!");
		put(CAMPFIRE_COOK, "C: We Made It!", "Warm Yourself With A Cooking Fire!");
		put(CAMPFIRE_DECO, "C: Decoration Needs!", "Create a Decoration Campfire!");
		put(CAMPFIRE_DECO_EFFECT, "C: Unexpected Effects!", "Burn Some Dye In A Decoration Campfire!");		
		put(TENT_STORE, "C: Store More!", "Put a Chest Inside a Tent!");
		put(BAG_SLEEP, "C: Outside Sleeper!", "Sleep in a Sleeping Bag!");
		put(TENT_SLEEP, "C: Inside Outside Sleeper!", "Put a Sleeping Bag Inside a Tent!");
		put(LIGHT, "C: Lighten Up The Dark!", "Create a Lantern!");
		put(TENT_LIGHT, "C: Night-Lamp!", "Put a Lantern Inside a Tent!");
		put(LOG, "C: It's a Real Campsite Now!", "Create a Log Seat!");
		put(KIT, "C: Cooking Equipment!", "Create a Campfire Kit!");
		put(SPIT, "C: Spit Cooking!", "Create a Spit Campfire!");
		put(GRILL, "C: Grill Cooking!", "Create a Grill Campfire!");
		put(PAN, "C: Pan Cooking!", "Create a Pan Campfire!");

		put(BEAR, "H: Bear Killer!", "Kill 5 Bears!");
		put(DEER, "H: Deer Killer!", "Kill 10 Deers!");
		put(HARE, "H: Hare Killer!", "Kill 15 Hares!");
		put(DEER_THROPHY, "H: Proud Hunter!", "Create a Antler Throphy!");
		put(HARE_ARMOR, "H: Fur Maniac!", "Create a Complete Set of Fur Armor!");
		put(TRAP_LUCKEY, "H: Luckey Hunter!", "Catch Something in a Trap Without Bait!");
		put(TRAP_BAIT, "H: Thoughtfull Hunter!", "Catch Something in a Trap With Bait!");
		put(TRAP, "H: Trapper!", "Create a Trap!");
	
		put(HUNTER, "Hunter Mastery!", "Complete all The Hunter Achievements!");
		put(CAMPER, "Camper Mastery!", "Complete all The Camper Achievements!");
		put(GENERAL, "Basic Mastery!", "Complete all The General Achievements!");
		put(LEGEND, "Legendary Camper!", "Complete all The Masteries!");
	}
	
	public static void put(String id, String name, String description)
	{
		devIDs.put(id, startId++);
		names.put(id, name);
		desc.put(id, description);
	}
	
	public static int id(String name)
	{
		return IDs.get(name)!=null? IDs.get(name):-1;
	}
	
	public static String name(String name)
	{
		return names.get(name);
	}
	
	public static String description(String name)
	{
		return desc.get(name);
	}
}
