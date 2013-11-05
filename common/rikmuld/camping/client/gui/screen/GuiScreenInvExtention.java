package rikmuld.camping.client.gui.screen;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketOpenGui;

public class GuiScreenInvExtention extends GuiScreen {

	int mainWidth;
	int mainHeight;
	
	int mainGuiWidth;
	int mainGuiHeight;
	int mainGuiLeft;
	int mainGuiTop;
	
	int baseLeft;
	int baseLength;
	int baseTop;
	
	int id;
	
	private boolean clickReady;
	private int clicker;
	
	EntityPlayer player;
	RenderItem itemRender;

	public GuiScreenInvExtention(int guiTop, int guiLeft, int guiWidth, int guiHeight, int width, int height, int id, EntityPlayer player)
	{
		this.mc = Minecraft.getMinecraft();
		
		this.mainGuiHeight = guiHeight;
		this.mainGuiWidth = guiWidth;
		this.mainGuiLeft = guiLeft;
		this.mainGuiTop = guiTop;

		this.baseLength = 236;
		this.baseLeft = (width-baseLength)/2;
		this.baseTop = guiTop+guiHeight+4;
		
		this.player = player;
		this.id = id;
		
		itemRender = new RenderItem();
	}

	public void drawScreen(int pointX, int pointY, float par3)
	{
		super.drawScreen(pointX, pointY, par3);
		
		if(clicker<20)clicker++;
		if(clicker>=20)clickReady = true;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_UTILS));
				
		this.drawTexturedModalRect(baseLeft, baseTop, 0, 0, 117, 22);
		this.drawTexturedModalRect(baseLeft + 120, baseTop, 0, 0, 117, 22);
		
		if(id == GuiInfo.GUI_INV_PLAYER)
		{
			this.drawTexturedModalRect(mainGuiLeft + mainGuiWidth-25, mainGuiTop+5, 0, 22, 20, 20);
			this.drawTexturedModalRect(mainGuiLeft + mainGuiWidth-23, mainGuiTop+8, 34, 22, 16, 16);
		}

		if(id!=GuiInfo.GUI_CAMPINV_BACK)itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ModItems.backpack.itemID, 1, 0), baseLeft+117/2-8, baseTop+3);
		else itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Item.skull, 1, 3), baseLeft+117/2-8, baseTop+3);
		
		if(id!=GuiInfo.GUI_CAMPINV_TOOL)itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ModItems.knife.itemID, 1, 0), baseLeft+120+117/2-8, baseTop+3);
		else itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Item.skull, 1, 3), baseLeft+120+117/2-8, baseTop+3);
		
		if(this.isPointInRegion(0, 0, 117, 21, pointX, pointY, baseLeft, baseTop)&&Mouse.isButtonDown(0)&&clickReady)
		{
			clickReady = false;
			
			if(id!=GuiInfo.GUI_CAMPINV_BACK)PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketOpenGui(GuiInfo.GUI_CAMPINV_BACK)));
			else
			{
				player.closeScreen();
				player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, player.worldObj, 0, 0, 0);
			}
		}
		if(this.isPointInRegion(120, 0, 117, 21, pointX, pointY, baseLeft, baseTop)&&Mouse.isButtonDown(0)&&clickReady)
		{
			clickReady = false;

			if(id!=GuiInfo.GUI_CAMPINV_TOOL)PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketOpenGui(GuiInfo.GUI_CAMPINV_TOOL)));
			else
			{
				player.closeScreen();
				player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, player.worldObj, 0, 0, 0);
			}
		}
		if(this.isPointInRegion(mainGuiWidth-25, 5, 22, 22, pointX, pointY, mainGuiLeft, mainGuiTop)&&Mouse.isButtonDown(0)&&clickReady)
		{
			clickReady = false;
			if(id==GuiInfo.GUI_INV_PLAYER)player.openGui(CampingMod.instance, GuiInfo.GUI_GUIDE, player.worldObj, 0, 0, 0);
		}
	}

	private boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY, int guiLeft, int guiTop)
	{
		pointX -= guiLeft;
		pointY -= guiTop;
		return pointX>=x-1&&pointX<x+width+1&&pointY>=y-1&&pointY<y+height+1;
	}
}
