package rikmuld.camping.client.gui.screen;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntityTent;

public class GuiScreenTentSleeping extends GuiScreen {

	TileEntityTent tent;
	boolean canClick;

	public GuiScreenTentSleeping(TileEntity tile)
	{
		tent = (TileEntityTent)tile;
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0:
				tent.sleep(mc.thePlayer);
				break;
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public void drawCenteredString(FontRenderer fontRender, String text, int x, int y, int color)
	{
		fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partitialTicks)
	{
		drawDefaultBackground();

		int guiLeft = (width - 97) / 2;
		int guiTop = (height - 30) / 2;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1));
		drawTexturedModalRect(guiLeft, guiTop, 0, 116, 97, 30);

		if(isPointInRegion(5, 5, 20, 20, mouseX, mouseY, guiLeft, guiTop))
		{
			drawTexturedModalRect(guiLeft + 5, guiTop + 5, 75, 0, 20, 20);
			if(Mouse.isButtonDown(0) && canClick)
			{
				mc.thePlayer.openGui(CampingMod.instance, GuiInfo.GUI_TENT, tent.worldObj, tent.xCoord, tent.yCoord, tent.zCoord);
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick = true;
			}
		}
		else
		{
			canClick = false;
		}

		super.drawScreen(mouseX, mouseY, partitialTicks);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int guiTop = (height - 30) / 2;

		buttonList.add(new GuiButton(0, (width / 2) + -20, guiTop + 10, 61, 10, "Sleep"));
	}

	private boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY, int guiLeft, int guiTop)
	{
		pointX -= guiLeft;
		pointY -= guiTop;
		return (pointX >= (x - 1)) && (pointX < (x + width + 1)) && (pointY >= (y - 1)) && (pointY < (y + height + 1));
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if(!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
		{
			mc.thePlayer.closeScreen();
		}
	}
}
