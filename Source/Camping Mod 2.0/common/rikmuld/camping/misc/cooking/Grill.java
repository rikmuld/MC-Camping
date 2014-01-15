package rikmuld.camping.misc.cooking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.client.gui.container.GuiContainerCampfireCook;
import rikmuld.camping.client.render.model.AbstractBox;
import rikmuld.camping.core.lib.TextureInfo;

public class Grill extends CookingEquipment {
	
	AbstractBox	pilar = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F);
	AbstractBox	line = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F);
	AbstractBox	line2 = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 1, 1, 60, 0.015625F, 0.0F, 0.0F, 0.0F);
	AbstractBox	sLine = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 29, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F);
	AbstractBox	sLine2 = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 1, 1, 29, 0.015625F, 0.0F, 0.0F, 0.0F);
	
	public Grill(ItemStack item)
	{
		super(600, CookingEquipmentList.grillFood, 4, item);
	}

	@Override
	public void renderModel()
	{
		GL11.glPushMatrix();	
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
		GL11.glTranslatef(-0.0234375F, -0.5F, -0.4375F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.0234375F, -0.5F, 0.40625F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.46875F, -0.4375F, -0.0078125F);
		line.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.015625F, -0.4375F, -0.46875F);
		line2.render(Tessellator.instance);
		GL11.glPopMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL));
		
		for(int i = 0; i<15; i++)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.2343675F, -0.4453125F, -0.2265625F+i*0.03125F);
			sLine.render(Tessellator.instance);
			GL11.glPopMatrix();
		}
		
		for(int i = 0; i<15; i++)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.2343675F+i*0.03125F, -0.4453125F, -0.2265625F);
			sLine2.render(Tessellator.instance);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}

	@Override
	public void doRenderFood(int foodIndex, ItemStack stack, EntityLivingBase entity)
	{
		ItemStack item = new ItemStack(stack.itemID, 1, stack.getItemDamage());
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.09F, -0.4375F, 0.01525F);

		switch(foodIndex)
		{
			case 0:
			{
				GL11.glTranslatef(-0.109375F, 0F, 0.046875F);
				break;
			}
			case 1:
			{
				GL11.glTranslatef(0.109375F, 0F, 0.046875F);
				break;
			}
			case 2:
			{
				GL11.glTranslatef(-0.109375F, 0F, -0.171875F);
				break;
			}
			case 3:
			{
				GL11.glTranslatef(0.109375F, 0F, -0.171875F);
				break;
			}
		}
		
		GL11.glScalef(0.15F, 0.25F, 0.15F);
		GL11.glRotatef(-90, 1, 0, 0);
		GL11.glRotatef(41, 0, -1, 0);
		GL11.glRotatef(-155, 1, 0, 1);
		renderer.renderItem(entity, item, 0);
		GL11.glPopMatrix();
	}

	@Override
	public void drawGuiTexture(GuiContainerCampfireCook container)
	{
		container.drawTexturedModalRect(container.getGuiLeft()+67, container.getGuiTop()+7, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft()+89, container.getGuiTop()+7, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft()+67, container.getGuiTop()+27, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft()+89, container.getGuiTop()+27, 7, 105, 18, 18);
		
		container.drawTexturedModalRect(container.getGuiLeft()+47, container.getGuiTop()+39, 176, 115, 80, 63);
	}

	@Override
	public void setSlots()
	{
		this.slots[0][0] = 68;
		this.slots[1][0] = 8;
		this.slots[0][1] = 90;
		this.slots[1][1] = 8;
		this.slots[0][2] = 68;
		this.slots[1][2] = 28;
		this.slots[0][3] = 90;
		this.slots[1][3] = 28;
	}
}
