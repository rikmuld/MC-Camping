package rikmuld.camping.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;

public class RendererThrophyItem implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		for(int i = 0; i < 3; i++)
		{
			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();

			GL11.glScalef(1.25F, -1.25F, -1.25F);
			GL11.glTranslatef(-0.325F, 0.5F, 0);

			if(type.equals(ItemRenderType.EQUIPPED) || type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON) || type.equals(ItemRenderType.FIRST_PERSON_MAP))
			{
				GL11.glRotatef(45, 0, 1, 0);
				GL11.glTranslatef(0.6F, -0.7F, 0.25F);
			}

			if(type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON) || type.equals(ItemRenderType.FIRST_PERSON_MAP))
			{
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-0.3F, 0, 0.2F);
			}

			if(i == 1)
			{
				GL11.glTranslatef(0.465F, 0.1F, -0.2F);
				GL11.glRotatef(180, -0.1944F, 0.3888F, -0.8888F);
			}
			if(i == 2)
			{
				GL11.glTranslatef(1.1F, -0.45F, 0.3F);
				GL11.glRotatef(180, 0.1944F, -0.3888F, -0.8888F);
			}

			GL11.glScalef(0.0625F, 0.0625F, 0.0625F);

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_ANTLER_THROPHY));

			if(i == 0)
			{
				ModModels.throphyAntler.renderAllExcept("shape6", "shape7");
			}
			else if(i == 1)
			{
				ModModels.throphyAntler.renderOnly("shape6");
			}
			else
			{
				ModModels.throphyAntler.renderOnly("shape7");
			}

			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
}
