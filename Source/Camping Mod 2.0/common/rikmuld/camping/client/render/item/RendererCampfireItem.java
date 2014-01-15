
package rikmuld.camping.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;

public class RendererCampfireItem implements IItemRenderer {

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
		if(type!=ItemRenderType.ENTITY)GL11.glTranslatef(0, -0.35F, 0);
		if(type==ItemRenderType.EQUIPPED)GL11.glTranslatef(0.6F, 0.6F, 0.6F);
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)GL11.glTranslatef(0, 1F, 0.7F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		GL11.glScalef(1.4F, 1.4F, 1.4F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_CAMPFIRE_DECO));
		ModModels.campfire.renderAll();
		GL11.glPopMatrix();
	}
}