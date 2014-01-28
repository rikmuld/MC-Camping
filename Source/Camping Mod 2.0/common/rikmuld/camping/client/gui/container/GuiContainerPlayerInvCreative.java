package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.client.gui.screen.GuiScreenInvExtentionCreative;
import rikmuld.camping.core.lib.GuiInfo;

public class GuiContainerPlayerInvCreative extends GuiContainerCreative {

	GuiScreenInvExtentionCreative screen;
	EntityPlayer player;

	public GuiContainerPlayerInvCreative(EntityPlayer player)
	{
		super(player);
		this.player = player;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float par1, int xPoint, int yPoint)
	{
		super.drawGuiContainerBackgroundLayer(par1, xPoint, yPoint);
		
		screen.hasRightTab = this.getCurrentTabIndex()==CreativeTabs.tabInventory.getTabIndex();
		
		screen.drawScreen(xPoint, yPoint, par1);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		screen = new GuiScreenInvExtentionCreative(guiTop, guiLeft, xSize, ySize, width, height, GuiInfo.GUI_INV_CREATIVE, player);
	}

	@Override
	public void onGuiClosed()
	{
		if(screen != null)
		{
			screen.onGuiClosed();
		}
	}
}
