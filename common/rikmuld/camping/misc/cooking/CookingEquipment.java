package rikmuld.camping.misc.cooking;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rikmuld.camping.client.gui.container.GuiContainerCampfireCook;
import rikmuld.camping.core.register.ModCookingEquipment;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemParts;

public abstract class CookingEquipment {

	public int[][] slots;
	public int cookTime;
	public HashMap<Integer, ItemStack> cookableFoood;
	protected ItemRenderer renderer;
	public int maxFood;
	public ItemStack itemInfo;
	
	public CookingEquipment(int cookTime, HashMap<Integer, ItemStack> cookableFoood, int maxFood, ItemStack item)
	{
		this.cookTime = cookTime;
		this.cookableFoood = cookableFoood;
		renderer = new ItemRenderer(Minecraft.getMinecraft());
		this.maxFood = maxFood;
		slots = new int[2][maxFood];
		this.setSlots();
		this.itemInfo = item;
		ModCookingEquipment.register(this, item);
	}
	
	public abstract void setSlots();
	public abstract void renderModel();
	protected abstract void doRenderFood(int foodIndex, ItemStack stack, EntityLivingBase entity);
	public abstract void drawGuiTexture(GuiContainerCampfireCook container);
	
	public int getBaseCookTime()
	{
		return this.cookTime;
	}
	
	public boolean canCook(int id)
	{
		return cookableFoood.containsKey(id);
	}
	
	public ItemStack getCookedFood(int id)
	{
		return cookableFoood.get(id);
	}
	
	public void renderFood(int foodIndex, ItemStack stack, EntityLivingBase entity)
	{
		if(foodIndex<maxFood&&stack.itemID!=ModItems.parts.itemID&&stack.getItemDamage()!=new ItemStack(ModItems.parts, 1, ItemParts.ASH).getItemDamage())
		{
			this.doRenderFood(foodIndex, stack, entity);
		}
	}
}
