package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.inventory.container.ContainerCampfireDeco;

public class GuiContainerCampfireDeco extends GuiContainer {

	TileEntityCampfireDeco fire;

	public GuiContainerCampfireDeco(InventoryPlayer playerInv, IInventory inv)
	{
		super(new ContainerCampfireDeco(playerInv, inv));
		ySize = 120;
		fire = (TileEntityCampfireDeco)inv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPFIRE_DECO));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int time = fire.time;
		String timeLeft = (fire.color == 16? "":("\u00a7" + TextInfo.dyeColors[fire.color])) + Integer.toString(time / 1200) + ":" + (Integer.toString((time % 1200) / 20).length() == 1? ("0" + Integer.toString((time % 1200) / 20)):(Integer.toString((time % 1200) / 20)));
		fontRenderer.drawString(StatCollector.translateToLocal(timeLeft), 92, 16, 4210752);
	}
}
