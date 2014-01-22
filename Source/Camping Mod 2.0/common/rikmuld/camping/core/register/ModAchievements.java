package rikmuld.camping.core.register;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import rikmuld.camping.core.lib.AchievementInfo;
import rikmuld.camping.item.ItemAnimalStuff;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.item.food.ItemFoodBerry;
import rikmuld.camping.misc.achievements.AchievementMain;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModAchievements {

	public static ArrayList<AchievementMain> achievements = new ArrayList<AchievementMain>();
	
	public static AchievementMain guide;
	public static AchievementMain knife;
	public static AchievementMain trap;
	public static AchievementMain trapLuckey;
	public static AchievementMain trapBait;
	public static AchievementMain deer;
	public static AchievementMain hare;
	public static AchievementMain bear;
	public static AchievementMain throphy;
	public static AchievementMain armor;
	public static AchievementMain hunter;
	public static AchievementMain encounterCamper;
	public static AchievementMain berry1;
	public static AchievementMain berry2;
	public static AchievementMain hemp;
	public static AchievementMain bag;
	public static AchievementMain base;
	public static AchievementMain canvas;
	public static AchievementMain tent;
	public static AchievementMain campfireDeco;
	public static AchievementMain effect;
	public static AchievementMain campfireCook;
	public static AchievementMain log;
	public static AchievementMain bagSleeping;
	public static AchievementMain lantern;
	public static AchievementMain tentSleep;
	public static AchievementMain tentStore;
	public static AchievementMain tentLight;
	public static AchievementMain kit;
	public static AchievementMain spit;
	public static AchievementMain pan;
	public static AchievementMain grill;
	public static AchievementMain camper;
	public static AchievementMain basic;
	public static AchievementMain legend;

	public static void init() 
	{		
		guide = new AchievementMain(AchievementInfo.GUIDE, 0, 0, Item.book, null, false);
		knife = new AchievementMain(AchievementInfo.KNIVE, 3, 0, ModItems.knife, guide, false);
		trap = new AchievementMain(AchievementInfo.TRAP, -2, 1, ModBlocks.bearTrap, guide, false);
		trapBait = new AchievementMain(AchievementInfo.TRAP_BAIT, -3, 2, Item.seeds, trap, false);
		trapLuckey = new AchievementMain(AchievementInfo.TRAP_LUCKEY, -3, 3, ModItems.venisonRaw, trap, false);
		deer = new AchievementMain(AchievementInfo.DEER, -2, -3, new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.ANTLER), guide, false);
		hare = new AchievementMain(AchievementInfo.HARE, -2, -2, new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_BROWN), guide, false);
		bear = new AchievementMain(AchievementInfo.BEAR, -2, -1, ModItems.venisonRaw, guide, false);
		throphy = new AchievementMain(AchievementInfo.DEER_THROPHY, -3, -3, new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.ANTLER), deer, false);
		armor = new AchievementMain(AchievementInfo.HARE_ARMOR, -3, -2, ModItems.armorFurChest, hare, false);
		hunter = new AchievementMain(AchievementInfo.HUNTER, -4, 0, ModItems.venisonCooked, guide, true);
		encounterCamper = new AchievementMain(AchievementInfo.ENCOUNTER_CAMPER, 0, 5, Item.emerald, guide, false);
		berry1 = new AchievementMain(AchievementInfo.BERRY1, 1, 2, new ItemStack(ModItems.berries.itemID, 1, ItemFoodBerry.BERRY_RED), guide, false);
		berry2 = new AchievementMain(AchievementInfo.BERRY2, 1, 3, new ItemStack(ModItems.berries.itemID, 1, ItemFoodBerry.BERRY_BLACK), guide, false);
		hemp = new AchievementMain(AchievementInfo.HEMP, 3, 2, ModItems.hemp, knife, false);
		canvas = new AchievementMain(AchievementInfo.CANVAS, 3, 3, new ItemStack(ModItems.parts.itemID, 1, ItemParts.CANVAS), hemp, false);
		bag = new AchievementMain(AchievementInfo.BAG, 3, 4, ModItems.backpack, canvas, false);
		base = new AchievementMain(AchievementInfo.BASE, 3, -2, ModBlocks.campfireBase, knife, false);
		tent = new AchievementMain(AchievementInfo.TENT, 5, 3, ModBlocks.tent, canvas, false);
		campfireDeco = new AchievementMain(AchievementInfo.CAMPFIRE_DECO, 3, -3, ModBlocks.campfireDeco, base, false);
		effect = new AchievementMain(AchievementInfo.CAMPFIRE_DECO_EFFECT, 3, -4, new ItemStack(Item.dyePowder.itemID, 1, 11), campfireDeco, false);
		campfireCook = new AchievementMain(AchievementInfo.CAMPFIRE_COOK, 5, -2, Item.coal, base, false);
		log = new AchievementMain(AchievementInfo.LOG, 5, 1, ModBlocks.log, tent, false);
		bagSleeping = new AchievementMain(AchievementInfo.BAG_SLEEP, 7, 2, ModBlocks.sleepingbag, tent, false);
		lantern = new AchievementMain(AchievementInfo.LIGHT, 7, 4, ModBlocks.lantern, tent, false);
		tentSleep = new AchievementMain(AchievementInfo.TENT_SLEEP, 8, 2, ModBlocks.sleepingbag, bagSleeping, false);
		tentStore = new AchievementMain(AchievementInfo.TENT_STORE, 8, 3, Block.chest, tent, false);
		tentLight = new AchievementMain(AchievementInfo.TENT_LIGHT, 8, 4, ModBlocks.lantern, lantern, false);
		kit = new AchievementMain(AchievementInfo.KIT, 6, -2, ModItems.kit, campfireCook, false);
		grill = new AchievementMain(AchievementInfo.GRILL, 8, -3, Block.fenceIron, kit, false);
		spit = new AchievementMain(AchievementInfo.SPIT, 8, -2, new ItemStack(ModItems.parts.itemID, 1, ItemParts.STICK), kit, false);
		pan = new AchievementMain(AchievementInfo.PAN, 8, -1, new ItemStack(ModItems.parts.itemID, 1, ItemParts.PAN), kit, false);
		basic = new AchievementMain(AchievementInfo.GENERAL, 0, 7, Item.paper, guide, true);
		camper = new AchievementMain(AchievementInfo.CAMPER, 10, 0, ModBlocks.campfireDeco, guide, true);
		legend = new AchievementMain(AchievementInfo.LEGEND, 0, -5, new ItemStack(Item.skull.itemID, 1, 3), guide, true);
	}

	public static void register(AchievementMain achievement, String name, String description)
	{
		achievement.registerAchievement();
		achievements.add(achievement);
		
		LanguageRegistry.instance().addStringLocalization(achievement.statName, "en_US", name);
		LanguageRegistry.instance().addStringLocalization(achievement.statName+".desc", "en_US", description);
	}

	public static Achievement[] getAll()
	{
		Achievement[] achievementList = new Achievement[achievements.size()];
		for(int i = 0; i<achievements.size(); i++)achievementList[i] = achievements.get(i);
		return achievementList;
	}
}
