package rikmuld.camping.misc.cooking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

public class CookingEquipmentList {

	public static HashMap<List<Integer>, ItemStack> grillFood = new HashMap<List<Integer>, ItemStack>();
	public static HashMap<List<Integer>, ItemStack> spitFood = new HashMap<List<Integer>, ItemStack>();
	public static HashMap<List<Integer>, ItemStack> panFood = new HashMap<List<Integer>, ItemStack>();

	public static HashMap<List<Integer>, CookingEquipment> equipment = new HashMap<List<Integer>, CookingEquipment>();
	public static HashMap<ArrayList<List<Integer>>, CookingEquipment> equipmentRecipes = new HashMap<ArrayList<List<Integer>>, CookingEquipment>();

	public static void addCooking(ItemStack item, CookingEquipment cookEquipment)
	{
		equipment.put(Arrays.asList(item.itemID, item.getItemDamage()), cookEquipment);
	}

	public static void addEquipmentRecipe(CookingEquipment equipment, ItemStack... items)
	{
		ArrayList<List<Integer>> key = new ArrayList<List<Integer>>();
		for(ItemStack item: items)
		{
			key.add(Arrays.asList(item.itemID, item.getItemDamage()));
		}
		equipmentRecipes.put(key, equipment);
	}

	public static void addGrillFood(int id, int meta, ItemStack item)
	{
		grillFood.put(Arrays.asList(id, meta), item);
	}

	public static void addPanFood(int id, int meta, ItemStack item)
	{
		panFood.put(Arrays.asList(id, meta), item);
	}

	public static void addSpitFood(int id, int meta, ItemStack item)
	{
		spitFood.put(Arrays.asList(id, meta), item);
	}

	public static CookingEquipment getCooking(ItemStack item)
	{
		return equipment.get(Arrays.asList(item.itemID, item.getItemDamage()));
	}

	public static CookingEquipment getCookingForRecipe(ArrayList<List<Integer>> items)
	{
		int i = 0;
		boolean flag = false;

		for(ArrayList<List<Integer>> list: equipmentRecipes.keySet())
		{
			int cound = 0;
			ArrayList<List<Integer>> copyItems = new ArrayList<List<Integer>>(items);
			for(List<Integer> itemInfo: list)
			{
				if(copyItems.contains(Arrays.asList(itemInfo.get(0), itemInfo.get(1))))
				{
					copyItems.remove(Arrays.asList(itemInfo.get(0), itemInfo.get(1)));
					cound++;
				}
				flag = (cound == list.size()) && (items.size() == list.size());
			}
			if(flag)
			{
				break;
			}
			i++;
		}

		if(flag) return (CookingEquipment)equipmentRecipes.values().toArray()[i];
		else return null;
	}
}
