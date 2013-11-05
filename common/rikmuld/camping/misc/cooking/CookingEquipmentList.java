package rikmuld.camping.misc.cooking;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rikmuld.camping.core.register.ModLogger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CookingEquipmentList {

	public static HashMap<Integer, ItemStack> grillFood = new HashMap<Integer, ItemStack>();
	public static HashMap<Integer, ItemStack> spitFood = new HashMap<Integer, ItemStack>();
	public static HashMap<Integer, ItemStack> panFood = new HashMap<Integer, ItemStack>();

	public static HashMap<List<Integer>, CookingEquipment> equipment = new HashMap<List<Integer>, CookingEquipment>();
	public static HashMap<ArrayList<List<Integer>>, CookingEquipment> equipmentRecipes = new HashMap<ArrayList<List<Integer>>, CookingEquipment>();

	public static void addCooking(ItemStack item, CookingEquipment cookEquipment)
	{
		equipment.put(Arrays.asList(item.itemID, item.getItemDamage()), cookEquipment);
	}
	
	public static CookingEquipment getCooking(ItemStack item)
	{
		return equipment.get(Arrays.asList(item.itemID, item.getItemDamage()));
	}
	
	public static CookingEquipment getCookingForRecipe(ArrayList<List<Integer>> items)
	{
		int i = 0;
		boolean flag = false;

		for(ArrayList<List<Integer>> list:equipmentRecipes.keySet())
		{
			int cound = 0;
			ArrayList<List<Integer>> copyItems = new ArrayList<List<Integer>>(items);
			for(List<Integer> itemInfo:list)
			{
				if(copyItems.contains(Arrays.asList(itemInfo.get(0), itemInfo.get(1))))
				{
					copyItems.remove(Arrays.asList(itemInfo.get(0), itemInfo.get(1)));
					cound++;
				}
				flag = cound==list.size()&&items.size()==list.size();
			}
			if(flag)break;
			i++;
		}

		if(flag) return (CookingEquipment)equipmentRecipes.values().toArray()[i];
		else return null;
	}
	
	public static void addGrillFood(int id, ItemStack item)
	{
		grillFood.put(id, item);
	}
	
	public static void addSpitFood(int id, ItemStack item)
	{
		spitFood.put(id, item);
	}
	
	public static void addPanFood(int id, ItemStack item)
	{
		panFood.put(id, item);
	}
	
	public static void addEquipmentRecipe(CookingEquipment equipment, ItemStack... items)
	{
		ArrayList<List<Integer>> key = new ArrayList<List<Integer>>();
		for(ItemStack item:items)key.add(Arrays.asList(item.itemID, item.getItemDamage()));
		equipmentRecipes.put(key, equipment);
	}
}
