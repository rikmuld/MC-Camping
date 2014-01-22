package rikmuld.camping.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeUtil {

	public static void addRecipe(Object output, int count, Object... input)
	{
		ItemStack outputStack = null;
		if(output instanceof Block)
		{
			Block outputBlock = (Block)output;
			outputStack = new ItemStack(outputBlock);
		}
		if(output instanceof Item)
		{
			Item outputItem = (Item)output;
			outputStack = new ItemStack(outputItem);
		}
		if(output instanceof ItemStack)
		{
			outputStack = (ItemStack)output;
		}
		outputStack.stackSize = count;
		GameRegistry.addRecipe(outputStack, input);
	}

	public static void addShapedOreRecipie(Object output, int count, Object... input)
	{
		ItemStack outputStack = null;
		if(output instanceof Block)
		{
			Block outputBlock = (Block)output;
			outputStack = new ItemStack(outputBlock);
		}
		if(output instanceof Item)
		{
			Item outputItem = (Item)output;
			outputStack = new ItemStack(outputItem);
		}
		if(output instanceof ItemStack)
		{
			outputStack = (ItemStack)output;
		}
		outputStack.stackSize = count;
		GameRegistry.addRecipe(new ShapedOreRecipe(outputStack, true, input));
	}

	public static void addShapelessOreRecipie(Object output, int count, Object... input)
	{
		ItemStack outputStack = null;
		if(output instanceof Block)
		{
			Block outputBlock = (Block)output;
			outputStack = new ItemStack(outputBlock);
		}
		if(output instanceof Item)
		{
			Item outputItem = (Item)output;
			outputStack = new ItemStack(outputItem);
		}
		if(output instanceof ItemStack)
		{
			outputStack = (ItemStack)output;
		}
		outputStack.stackSize = count;
		GameRegistry.addRecipe(new ShapelessOreRecipe(outputStack, input));
	}

	public static void addShapelessRecipe(Object output, int count, Object... input)
	{
		ItemStack outputStack = null;
		if(output instanceof Block)
		{
			Block outputBlock = (Block)output;
			outputStack = new ItemStack(outputBlock);
		}
		if(output instanceof Item)
		{
			Item outputItem = (Item)output;
			outputStack = new ItemStack(outputItem);
		}
		if(output instanceof ItemStack)
		{
			outputStack = (ItemStack)output;
		}
		outputStack.stackSize = count;
		GameRegistry.addShapelessRecipe(outputStack, input);
	}

	public static void addSmeltingRecipe(Object output, int count, float xp, int inputId, int inputMeta)
	{
		ItemStack outputStack = null;
		if(output instanceof Block)
		{
			Block outputBlock = (Block)output;
			outputStack = new ItemStack(outputBlock);
		}
		if(output instanceof Item)
		{
			Item outputItem = (Item)output;
			outputStack = new ItemStack(outputItem);
		}
		if(output instanceof ItemStack)
		{
			outputStack = (ItemStack)output;
		}
		outputStack.stackSize = count;
		FurnaceRecipes.smelting().addSmelting(inputId, inputMeta, outputStack, xp);
	}
}
