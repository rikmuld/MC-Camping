package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntityBearTrap;
import rikmuld.camping.inventory.container.ContainerTrap;

public class GuiContainerTrap extends GuiContainer {

	TileEntityBearTrap trap;

	public GuiContainerTrap(InventoryPlayer playerInv, IInventory inv)
	{
		super(new ContainerTrap(playerInv, inv));
		this.ySize = 120;
		trap = (TileEntityBearTrap) inv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TRAP));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
