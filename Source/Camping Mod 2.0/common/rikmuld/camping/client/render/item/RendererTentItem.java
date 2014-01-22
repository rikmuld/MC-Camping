package rikmuld.camping.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.core.util.TentUtil;

public class RendererTentItem implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0.1F, 0.45F);
		if(type == ItemRenderType.ENTITY)
		{
			GL11.glTranslatef(-0.375F, 0, -0.5F);
		}
		if(type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotatef(45, 0, -1, 0);
			GL11.glTranslatef(0.0F, 0.0F, -0.3F);
		}
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glTranslatef(0.0F, 0.3F, 0.6F);
		}
		GL11.glScalef(0.02625F, -0.02625F, -0.02625F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TentUtil.getTentTexture(item.hasTagCompound()? item.getTagCompound().getInteger("color"):15));
		ModModels.tent.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7");
		GL11.glPopMatrix();
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
}
