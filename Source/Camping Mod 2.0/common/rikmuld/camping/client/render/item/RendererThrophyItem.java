package rikmuld.camping.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;

public class RendererThrophyItem implements IItemRenderer {

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
		if(type==ItemRenderType.INVENTORY)
		{
			GL11.glTranslatef(0, 0.2F, 0);
			GL11.glScalef(1.1F, 1.1F, 1.1F);
		}
		if(type==ItemRenderType.EQUIPPED)
		{
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glRotatef(35, 1, 0, 0);
			GL11.glTranslatef(0.0F, 0.85F, 0.05F);
		}
		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotatef(45, 0, -1, 0);
			GL11.glTranslatef(0.4F, 1F, 0.7F);
		}
		GL11.glTranslatef(0.6F, -0.6F, 0);
		
		GL11.glRotatef(180, 0, 1, 0);
		
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		GL11.glScalef(1.4F, 1.4F, 1.4F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_ANTLER_THROPHY));
		
		ModModels.throphyAntler.renderAll();
		
		GL11.glPopMatrix();
	}
}
