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

public class Pan extends CookingEquipment {

	AbstractBox pilar = new AbstractBox(64, 32, false, 0, 2, 0, 0, 0, 1, 28, 1, 0.03125F, 0.0F, 0.0F, 0.0F);
	AbstractBox line = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F);
	AbstractBox cable = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 1, 45, 1, 0.0078625F, 0.0F, 0.0F, 0.0F);
	AbstractBox pan = new AbstractBox(32, 16, false, 2, 1, 0, 0, 0, 5, 3, 5, 0.0625F, 0.0F, 0.0F, 0.0F);
	AbstractBox panCover = new AbstractBox(64, 32, false, 4, 2, 0, 0, 0, 3, 1, 3, 0.0625F, 0.0F, 0.0F, 0.0F);
	AbstractBox panHandle = new AbstractBox(64, 32, false, 4, 13, 0, 0, 0, 1, 1, 1, 0.03125F, 0.0F, 0.0F, 0.0F);

	public Pan(ItemStack item)
	{
		super(1000, CookingEquipmentList.panFood, 8, item);
	}

	@Override
	public void doRenderFood(int foodIndex, ItemStack stack, EntityLivingBase entity)
	{}

	@Override
	public void drawGuiTexture(GuiContainerCampfireCook container)
	{
		container.drawTexturedModalRect(container.getGuiLeft() + 24, container.getGuiTop() + 77, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 131, container.getGuiTop() + 77, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 32, container.getGuiTop() + 54, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 123, container.getGuiTop() + 54, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 40, container.getGuiTop() + 30, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 113, container.getGuiTop() + 30, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 65, container.getGuiTop() + 21, 7, 105, 18, 18);
		container.drawTexturedModalRect(container.getGuiLeft() + 90, container.getGuiTop() + 21, 7, 105, 18, 18);
	}

	@Override
	public void renderModel()
	{
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_PAN));

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.4375F, -0.875F, -0.015625F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(0.40625F, -0.875F, -0.015625F);
		pilar.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.46875F, -0.859375F, -0.0078125F);
		line.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.15625F, -0.53125F, -0.15625F);
		pan.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.09375F, -0.5625F, -0.09375F);
		panCover.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.015625F, -0.578125F, -0.015625F);
		panHandle.render(Tessellator.instance);
		GL11.glPopMatrix();

		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL));

		GL11.glPushMatrix();
		GL11.glTranslatef(-0.00393125F, -0.8515125F, -0.00393125F);
		cable.render(Tessellator.instance);
		GL11.glPopMatrix();

		GL11.glPopMatrix();
	}

	@Override
	public void setSlots()
	{
		slots[0][0] = 25;
		slots[1][0] = 78;
		slots[0][1] = 132;
		slots[1][1] = 78;
		slots[0][2] = 33;
		slots[1][2] = 55;
		slots[0][3] = 124;
		slots[1][3] = 55;
		slots[0][4] = 41;
		slots[1][4] = 31;
		slots[0][5] = 114;
		slots[1][5] = 31;
		slots[0][6] = 66;
		slots[1][6] = 22;
		slots[0][7] = 91;
		slots[1][7] = 22;
	}
}
