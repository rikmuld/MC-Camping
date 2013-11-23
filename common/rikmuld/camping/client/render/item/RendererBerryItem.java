package rikmuld.camping.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.client.render.model.AbstractBox;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;

public class RendererBerryItem implements IItemRenderer {

	AbstractBox	block = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 16, 16, 16, 0.0625F, 0, 0, 0);

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1F, -1F);
		if(type!=ItemRenderType.ENTITY)GL11.glTranslatef((float) 0, (float) -1+0.0625F, (float) -1F);
		else GL11.glTranslatef((float) -0.5F, (float) -1F+0.25F, (float) -0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_BERRY_EMPTY));
		block.render(Tessellator.instance);	
		GL11.glPopMatrix();
	}
}