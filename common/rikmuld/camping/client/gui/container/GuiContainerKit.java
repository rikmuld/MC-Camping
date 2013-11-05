package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.inventory.container.ContainerKit;

public class GuiContainerKit extends GuiContainer {

	public GuiContainerKit(InventoryPlayer playerInv, ItemStack item)
	{
		super(new ContainerKit(playerInv, item));
		this.ySize = 181;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_KIT));
		int var5 = (width-xSize)/2;
		int var6 = (height-ySize)/2;
		this.drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
	}
}
