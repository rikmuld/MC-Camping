package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.inventory.container.ContainerCampfireCook;

public class GuiContainerCampfireCook extends GuiContainer {

	TileEntityCampfireCook fire;

	public GuiContainerCampfireCook(InventoryPlayer playerInv, IInventory inv)
	{
		super(new ContainerCampfireCook(playerInv, inv));
		ySize = 188;
		fire = (TileEntityCampfireCook)inv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float mouseX, int mouseY, int partTicks)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPFIRE_COOK));

		int scale = (int)fire.getScaledCoal(40);
		scale++;

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if(fire.equipment != null)
		{
			fire.equipment.drawGuiTexture(this);
		}
		drawTexturedModalRect(guiLeft + 66, (guiTop + 94) - scale, 176, 40 - scale, 44, scale);
		drawTexturedModalRect(guiLeft + 79, guiTop + 83, 79, 105, 18, 18);

		if(fire.equipment != null)
		{
			for(int i = 0; i < fire.equipment.maxFood; i++)
			{
				int scale2 = (int)fire.getScaledcookProgress(10, i);

				boolean isNotCooked = fire.getStackInSlot(i + 2) != null? fire.equipment.canCook(fire.getStackInSlot(i + 2).itemID, fire.getStackInSlot(i + 2).getItemDamage()):false;
				drawTexturedModalRect(guiLeft + fire.equipment.slots[0][i] + 16, guiTop + fire.equipment.slots[1][i] + 2, 223, 0, 3, 12);
				drawTexturedModalRect(guiLeft + fire.equipment.slots[0][i] + 17, (guiTop + fire.equipment.slots[1][i] + 13) - scale2, isNotCooked? 226:227, 11 - scale2, 1, scale2);
			}
		}
	}

	public int getGuiLeft()
	{
		return guiLeft;
	}

	public int getGuiTop()
	{
		return guiTop;
	}
}
