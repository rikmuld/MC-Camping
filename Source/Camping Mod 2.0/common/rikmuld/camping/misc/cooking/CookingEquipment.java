package rikmuld.camping.misc.cooking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import rikmuld.camping.client.gui.container.GuiContainerCampfireCook;
import rikmuld.camping.core.register.ModCookingEquipment;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.item.food.ItemFoodStew;

public abstract class CookingEquipment {

	public int[][] slots;
	public int cookTime;
	public HashMap<List<Integer>, ItemStack> cookableFoood;
	protected ItemRenderer renderer;
	public int maxFood;
	public ItemStack itemInfo;

	public CookingEquipment(int cookTime, HashMap<List<Integer>, ItemStack> cookableFoood, int maxFood, ItemStack item)
	{
		this.cookTime = cookTime;
		this.cookableFoood = cookableFoood;
		this.maxFood = maxFood;
		slots = new int[2][maxFood];
		setSlots();
		itemInfo = item;
		ModCookingEquipment.register(this, item);
	}

	public boolean canCook(int id, int meta)
	{
		return cookableFoood.containsKey(Arrays.asList(id, meta));
	}

	protected abstract void doRenderFood(int foodIndex, ItemStack stack, EntityLivingBase entity);

	public abstract void drawGuiTexture(GuiContainerCampfireCook container);

	public int getBaseCookTime()
	{
		return cookTime;
	}

	public ItemStack getCookedFood(int id, int meta)
	{
		return cookableFoood.get(Arrays.asList(id, meta));
	}

	public void renderFood(int foodIndex, ItemStack stack, EntityLivingBase entity)
	{
		if(renderer == null)
		{
			renderer = new ItemRenderer(Minecraft.getMinecraft());
		}
		if((foodIndex < maxFood) && ((stack.itemID != ModItems.parts.itemID) || (stack.getItemDamage() != ItemParts.ASH)))
		{
			doRenderFood(foodIndex, stack, entity);
		}
	}
	
	public ItemStack getSoup(int id, int meta)
	{
		if(!(this instanceof Pan))return null;
		return CookingEquipmentList.soupFood.containsKey(Arrays.asList(id, meta))? CookingEquipmentList.soupFood.get(Arrays.asList(id, meta)):null;
	}


	public abstract void renderModel();

	public abstract void setSlots();
}
