package rikmuld.camping.client.gui.container;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.container.ContainerTent;

public class GuiContainerTent extends GuiContainer {

	TileEntityTent tent;

	public GuiContainerTent(InventoryPlayer playerInv, IInventory inv)
	{
		super(new ContainerTent(playerInv, inv));
		tent = (TileEntityTent) inv;
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0: tent.removeAll(); break;
			case 1: tent.removeBed(); break;
			case 2: tent.removeLantern(); break;
			case 3: tent.removeChest(); break;
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.add(new GuiButton(0, this.width/2 + 15, guiTop+0-22, 85, 10, "Clear All"));
		this.buttonList.add(new GuiButton(1, this.width/2 + 15, guiTop+20-22, 85, 10, "Remove Bed"));
		this.buttonList.add(new GuiButton(2, this.width/2 + 15, guiTop+30-22, 85, 10, "Remove Lantern"));
		this.buttonList.add(new GuiButton(3, this.width/2 + 15, guiTop+40-22, 85, 10, "Remove Chest"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float mouseX, int mouseY, int partTicks)
	{}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{		
		GL11.glPushMatrix();
		this.drawCenteredString(fontRenderer, TextInfo.COLOR_WHITE+"Space Left: "+tent.contends+"/"+tent.maxContends, -this.guiLeft+this.width/2-40, -20, 0);
		this.drawCenteredString(fontRenderer, TextInfo.COLOR_WHITE+"Beds: "+tent.beds+"/"+tent.maxBeds, -this.guiLeft+this.width/2-40, 0, 0);
		this.drawCenteredString(fontRenderer, TextInfo.COLOR_WHITE+"Lanterns: "+tent.lanterns+"/"+tent.maxLanterns, -this.guiLeft+this.width/2-40, 10, 0);
		this.drawCenteredString(fontRenderer, TextInfo.COLOR_WHITE+"Chests: "+tent.chests+"/"+tent.maxChests, -this.guiLeft+this.width/2-40, 20, 0);
		GL11.glPopMatrix();
	}
	
	public void drawCenteredString(FontRenderer fontRender, String text, int x, int y, int color)
    {
        fontRender.drawString(text, x - fontRender.getStringWidth(text) / 2, y, color);
    }
}
