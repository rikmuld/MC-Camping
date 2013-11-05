package rikmuld.camping.misc.cooking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.client.gui.container.GuiContainerCampfireCook;
import rikmuld.camping.client.render.model.AbstractBox;
import rikmuld.camping.core.lib.TextureInfo;

public class Spit extends CookingEquipment {
	
	AbstractBox	pilar = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F);
	AbstractBox	line = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F);

	public Spit(ItemStack item)
	{
		super(250, CookingEquipmentList.spitFood, 2, item);
	}

	@Override
	public void renderModel()
	{		
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT));
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.40625F, -0.5F, -0.015625F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.46875F, -0.484375F, -0.0078125F);
		line.render(Tessellator.instance);
		GL11.glPopMatrix();
	}

	@Override
	public void doRenderFood(int foodIndex, ItemStack stack, EntityLivingBase entity)
	{
		ItemStack item = new ItemStack(stack.itemID, 1, stack.getItemDamage());
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.09F, -0.425F, 0.01525F);

		switch(foodIndex)
		{
			case 0:
			{
				GL11.glTranslatef(-0.125F, 0F, 0F);
				break;
			}
			case 1:
			{
				GL11.glTranslatef(0.125F, 0F, 0);
				break;
			}
		}
		
		GL11.glScalef(0.15F, 0.15F, 0.25F);
		GL11.glRotatef(41, 0, -1, 0);
		GL11.glRotatef(-150, 1, 0, 1);
		renderer.renderItem(entity, item, 0);
		GL11.glPopMatrix();
	}

	@Override
	public void drawGuiTexture(GuiContainerCampfireCook container)
	{
		container.drawTexturedModalRect(container.getGuiLeft()+48, container.getGuiTop()+26, 176, 44, 80, 68);
	}

	@Override
	public void setSlots()
	{
		this.slots[0][0] = 66;
		this.slots[1][0] = 27;
		this.slots[0][1] = 94;
		this.slots[1][1] = 27;
	}
}

