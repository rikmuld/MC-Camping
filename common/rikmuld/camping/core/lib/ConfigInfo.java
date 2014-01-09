package rikmuld.camping.core.lib;

import java.util.HashMap;
import java.util.Map;

public class ConfigInfo {

	public static final String USE_BEARS = "useBear";
	public static final String USE_DEERS = "useDeer";
	public static final String USE_HARES = "useHare";
	public static final String USE_CAMPERS = "useCamper";
	public static final String USE_WORLDGEN = "useWorldgen";
	public static final String USE_WORLDGEN_TREE = "useTreeGen";
	public static final String USE_WORLDGEN_CAMP = "useCampGen";
	public static final String USE_WORLDGEN_HEMP = "useHempGen";
	public static final String ENBLED_SPEEDUP = "enabledSpeedUp";
	public static final String ENBLED_PLAYER_TRAP = "enablePlayerTrap";
	public static final String ENABLE_VERSION = "enableGuideVersion";
	public static final String RARENESS_TREE = "rarenessTree";
	public static final String RARENESS_HEMP = "rarenessHemp";
	public static final String RARENESS_CAMP = "rarenessCamp";
	public static final String TOOL_DAMAGE = "toolDamage";
	public static final String HEAL_BERRY = "berryHeal";
	public static final String HEAL_MARSH = "marshHeal";
	public static final String HEAL_HARE_RAW = "hareHealRaw";
	public static final String HEAL_HARE_COOKED = "hareHealCooked";
	public static final String HEAL_VANISON_RAW = "vanisonHealRaw";
	public static final String HEAL_VANISON_COOKED = "vanisonHealCooked";
	public static final String SPEEDBOOST = "speedBoost";

	public static void putAll()
	{
		ConfigInfoBoolean.put(USE_BEARS, "Use Bears", true, "Animals");
		ConfigInfoBoolean.put(USE_DEERS, "Use Dears", true, "Animals");
		ConfigInfoBoolean.put(USE_HARES, "Use Hares", true, "Animals");
		ConfigInfoBoolean.put(USE_CAMPERS, "Use Campers", true, "Animals");
		ConfigInfoBoolean.put(USE_WORLDGEN, "Use World Genaration", true, "World Generation");
		ConfigInfoBoolean.put(USE_WORLDGEN_TREE, "Use Berry Tree Generation", true, "World Generation");
		ConfigInfoBoolean.put(USE_WORLDGEN_CAMP, "Use Camp Generation", true, "World Generation");
		ConfigInfoBoolean.put(USE_WORLDGEN_HEMP, "Use Hemp Generation", true, "World Generation");
		ConfigInfoBoolean.put(ENBLED_SPEEDUP, "Enable Fur Armor Speed Boost", true, "Tools");
		ConfigInfoBoolean.put(ENABLE_VERSION, "Enable Camping Guide Version Checker", true, "General");
		ConfigInfoBoolean.put(ENBLED_PLAYER_TRAP, "Trap Can Hurt The Player", true, "General");
		ConfigInfoDouble.put(SPEEDBOOST, "Fur Armor Speed Boost", 0.0625, "Tools");
		ConfigInfoInteger.put(RARENESS_TREE, "Berry Tree Spawn Rareness", 25, "World Generation");
		ConfigInfoInteger.put(RARENESS_HEMP, "Hemp Spawn Rareness", 45, "World Generation");
		ConfigInfoInteger.put(RARENESS_CAMP, "Camp Spawn Rareness", 50, "World Generation");
		ConfigInfoInteger.put(HEAL_BERRY, "Marshmellow Heal", 4, "Food");
		ConfigInfoInteger.put(HEAL_MARSH, "Berry Heal", 2, "Food");
		ConfigInfoInteger.put(HEAL_VANISON_RAW, "Raw Vanison Heal", 3, "Food");
		ConfigInfoInteger.put(HEAL_VANISON_COOKED, "Cooked Venison Heal", 8, "Food");
		ConfigInfoInteger.put(HEAL_HARE_RAW, "Raw Hare Heal", 2, "Food");
		ConfigInfoInteger.put(HEAL_HARE_COOKED, "Cooked Hare Heal", 6, "Food");
		ConfigInfoInteger.put(TOOL_DAMAGE, "Max Knife Damage", 250, "Tools");
	}
	
	public static class ConfigInfoInteger {		
		
		public static Map<String, Integer> devValues = new HashMap<String, Integer>();
		public static Map<String, Integer> values = new HashMap<String, Integer>();
		public static Map<String, String> names = new HashMap<String, String>();
		public static Map<String, String> catagories = new HashMap<String, String>();

		public static void put(String id, String name, int value, String catagory)
		{
			devValues.put(id, value);
			names.put(id, name);
			catagories.put(id, catagory);
		}
		
		public static int value(String name)
		{
			return values.get(name)!=null? values.get(name):-1;
		}
	}
	
	public static class ConfigInfoBoolean {		
		
		public static Map<String, Boolean> devValues = new HashMap<String, Boolean>();
		public static Map<String, Boolean> values = new HashMap<String, Boolean>();
		public static Map<String, String> names = new HashMap<String, String>();
		public static Map<String, String> catagories = new HashMap<String, String>();

		public static void put(String id, String name, boolean value, String catagory)
		{
			devValues.put(id, value);
			names.put(id, name);
			catagories.put(id, catagory);
		}
		
		public static boolean value(String name)
		{
			return values.get(name);
		}
	}
	
	public static class ConfigInfoDouble {		
		
		public static Map<String, Double> devValues = new HashMap<String, Double>();
		public static Map<String, Double> values = new HashMap<String, Double>();
		public static Map<String, String> names = new HashMap<String, String>();
		public static Map<String, String> catagories = new HashMap<String, String>();

		public static void put(String id, String name, double value, String catagory)
		{
			devValues.put(id, value);
			names.put(id, name);
			catagories.put(id, catagory);
		}
		
		public static double value(String name)
		{
			return values.get(name)!=null? values.get(name):-1;
		}
	}
}
