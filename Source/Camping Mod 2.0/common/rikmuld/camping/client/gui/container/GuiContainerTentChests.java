package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.container.ContainerTentChests;

public class GuiContainerTentChests extends GuiContainer {

	TileEntityTent tent;
	public int slideState;
	public int oldSLideState;
	int xBegin = -1;
	private int slideBegin;
	boolean mouseMouse;
	boolean[] canClick = new boolean[]{false, false};

	public GuiContainerTentChests(InventoryPlayer invPlayer, IInventory inv)
	{
		super(new ContainerTentChests(invPlayer, inv));

		tent = (TileEntityTent)inv;
		ySize = 228;
		xSize = 214;

		slideState = tent.slide;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partTicks, int mouseX, int mouseY)
	{
		oldSLideState = slideState;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_2));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if(isPointInRegion(15, 8, 20, 20, mouseX, mouseY))
		{
			drawTexturedModalRect(guiLeft + 15, guiTop + 8, 214, 0, 20, 20);
			if(Mouse.isButtonDown(0) && canClick[0])
			{
				mc.thePlayer.openGui(CampingMod.instance, GuiInfo.GUI_TENT, tent.worldObj, tent.xCoord, tent.yCoord, tent.zCoord);
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick[0] = true;
			}
		}
		else
		{
			canClick[0] = false;
		}

		if(isPointInRegion(39 + slideState, 12, 15, 12, mouseX, mouseY))
		{
			if(Mouse.isButtonDown(0) && canClick[1])
			{
				mouseMouse = true;
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick[1] = true;
			}
		}
		else
		{
			canClick[1] = false;
		}

		if(mouseMouse && (tent.chests > 2))
		{
			if(xBegin == -1)
			{
				xBegin = mouseX;
				slideBegin = slideState;
			}

			slideState = (slideBegin + mouseX) - xBegin;

			if(slideState < 0)
			{
				slideState = 0;
			}
			if(slideState > 144)
			{
				slideState = 144;
			}
		}
		else
		{
			xBegin = -1;
		}

		if(!Mouse.isButtonDown(0))
		{
			mouseMouse = false;
		}

		drawTexturedModalRect(guiLeft + 39 + slideState, guiTop + 12, 234, tent.chests > 2? 12:0, 15, 12);

		if(slideState != oldSLideState)
		{
			tent.setSlideState(slideState);
		}
	}
}
