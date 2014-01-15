package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.util.MathUtil;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.container.ContainerTentLanterns;

public class GuiContainerTentLanterns extends GuiContainer{
	
	TileEntityTent tent;
	boolean canClick;

	public GuiContainerTentLanterns(InventoryPlayer invPlayer, IInventory inv)
	{		
		super(new ContainerTentLanterns(invPlayer, inv));
		
		tent = (TileEntityTent) inv;
		this.ySize = 198;
	}

	public void drawCenteredString(FontRenderer fontRender, String text, int x, int y, int color)
    {
        fontRender.drawString(text, x - fontRender.getStringWidth(text) / 2, y, color);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1));
		this.drawTexturedModalRect(guiLeft+63, guiTop, 0, 0, 50, 107);
		this.drawTexturedModalRect(guiLeft, guiTop+104, 80, 165, xSize, 91);

		if(this.isPointInRegion(63+15, 8, 20, 20, mouseX, mouseY))
		{
			this.drawTexturedModalRect(guiLeft+63+15, guiTop+8, 75, 0, 20, 20);
			if(Mouse.isButtonDown(0)&&canClick)
			{
				mc.thePlayer.openGui(CampingMod.instance, GuiInfo.GUI_TENT, tent.worldObj, tent.xCoord, tent.yCoord, tent.zCoord);
			}
			if(!Mouse.isButtonDown(0))this.canClick = true;
		}
		else this.canClick = false;
		
		int scale = (int) MathUtil.getScaledNumber(tent.time, 1500, 22);
		this.drawTexturedModalRect(guiLeft+13+63, guiTop+83-scale, 50, 22-scale, 25, 22);
	}
}
