package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.client.gui.screen.GuiScreenInvExtention;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.inventory.container.ContainerCampinvBack;
import rikmuld.camping.inventory.container.ContainerCampinvTool;

public class GuiContainerCampingInvTool extends GuiContainer {

	GuiScreenInvExtention screen;
	EntityPlayer player;
	
	public GuiContainerCampingInvTool(EntityPlayer player)
	{
		super(new ContainerCampinvTool(player));
		this.player = player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xPoint, int yPoint)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPINV_TOOL));
		int var5 = (width-xSize)/2;
		int var6 = (height-ySize)/2;
		this.drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
    	screen.drawScreen(xPoint, yPoint, par1);
	}
	
	@Override
    public void initGui()
    {
    	super.initGui();
    	screen = new GuiScreenInvExtention(guiTop, guiLeft, xSize, ySize, width, height, GuiInfo.GUI_CAMPINV_TOOL, player);
    }
}
