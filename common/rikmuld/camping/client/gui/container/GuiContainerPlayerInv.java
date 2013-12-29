package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.client.gui.screen.GuiScreenInvExtention;
import rikmuld.camping.core.lib.GuiInfo;

public class GuiContainerPlayerInv extends GuiInventory{

	GuiScreenInvExtention screen;
	EntityPlayer player;
	
	public GuiContainerPlayerInv(EntityPlayer player)
	{
		super(player);
		this.player = player;
	}
	
	@Override
    public void initGui()
    {
    	super.initGui();
    	screen = new GuiScreenInvExtention(guiTop, guiLeft, xSize, ySize, width, height, GuiInfo.GUI_INV_PLAYER, player);
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(float par1, int xPoint, int yPoint)
    {
    	super.drawGuiContainerBackgroundLayer(par1, xPoint, yPoint);
    	screen.drawScreen(xPoint, yPoint, par1);
    }
}
