package rikmuld.camping.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.handler.GuiContendHandler;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.network.packets.PacketOpenGui;

public class GuiScreenInvExtentionCreative extends GuiScreen {

	public GuiContendHandler handler;

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

	boolean[] canClick = new boolean[]{false, false, false};

	EntityPlayer player;
	RenderItem itemRender;

	public boolean hasRightTab;

	public GuiScreenInvExtentionCreative(int guiTop, int guiLeft, int guiWidth, int guiHeight, int width, int height, int id, EntityPlayer player)
	{
		mc = Minecraft.getMinecraft();

		mainGuiHeight = guiHeight;
		mainGuiWidth = guiWidth;
		mainGuiLeft = guiLeft;
		mainGuiTop = guiTop;

		baseLength = 236;
		baseLeft = (width - baseLength) / 2;
		baseTop = guiTop + guiHeight + 28;

		this.player = player;
		this.id = id;

		itemRender = new RenderItem();

		if(GuiContendHandler.readFromNBT(player, this.getClass().getSimpleName()) == null)
		{
			handler = new GuiContendHandler(3, this.getClass().getSimpleName());
		}
		else
		{
			handler = GuiContendHandler.readFromNBT(player, this.getClass().getSimpleName());
		}
	}

	@Override
	public void drawScreen(int pointX, int pointY, float par3)
	{
		super.drawScreen(pointX, pointY, par3);

		if(clicker < 20)
		{
			clicker++;
		}
		if(clicker >= 20)
		{
			clickReady = true;
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_UTILS));

		drawTexturedModalRect(baseLeft + handler.posX(2), baseTop + handler.posY(2), 0, 0, 117, 22);
		drawTexturedModalRect(baseLeft + 120 + handler.posX(1), baseTop + handler.posY(1), 0, 0, 117, 22);

		if(id == GuiInfo.GUI_INV_CREATIVE&&this.hasRightTab)
		{
			drawTexturedModalRect(((mainGuiLeft + mainGuiWidth) - 25) + handler.posX(0), mainGuiTop + 5 + handler.posY(0), 0, 22, 20, 20);
			drawTexturedModalRect(((mainGuiLeft + mainGuiWidth) - 23) + handler.posX(0), mainGuiTop + 8 + handler.posY(0), 34, 22, 16, 16);
		}

		if(id != GuiInfo.GUI_CAMPINV_BACK)
		{
			itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ModItems.backpack.itemID, 1, 0), ((baseLeft + (117 / 2)) - 8) + handler.posX(2), baseTop + 3 + handler.posY(2));
		}
		else
		{
			itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Item.skull, 1, 3), ((baseLeft + (117 / 2)) - 8) + handler.posX(2), baseTop + 3 + handler.posY(2));
		}

		if(id != GuiInfo.GUI_CAMPINV_TOOL)
		{
			itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ModItems.knife.itemID, 1, 0), ((baseLeft + 120 + (117 / 2)) - 8) + handler.posX(1), baseTop + 3 + handler.posY(1));
		}
		else
		{
			itemRender.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Item.skull, 1, 3), ((baseLeft + 120 + (117 / 2)) - 8) + handler.posX(1), baseTop + 3 + handler.posY(1));
		}

		if(isPointInRegion(handler.posX(2), handler.posY(2), 117, 21, pointX, pointY, baseLeft, baseTop))
		{
			if(Mouse.isButtonDown(0) && clickReady && canClick[0])
			{
				clickReady = false;

				if(id != GuiInfo.GUI_CAMPINV_BACK)
				{
					PacketUtil.sendToSever(new PacketOpenGui(GuiInfo.GUI_CAMPINV_BACK));
				}
				else
				{
					player.closeScreen();
					player.openGui(CampingMod.instance, GuiInfo.GUI_INV_CREATIVE, player.worldObj, 0, 0, 0);
				}
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick[0] = true;
			}

			handler.updateContend(2, pointX, pointY, true);
		}
		else
		{
			canClick[0] = false;
		}

		if(isPointInRegion(120 + handler.posX(1), handler.posY(1), 117, 21, pointX, pointY, baseLeft, baseTop))
		{
			if(Mouse.isButtonDown(0) && clickReady && canClick[1])
			{
				clickReady = false;

				if(id != GuiInfo.GUI_CAMPINV_TOOL)
				{
					PacketUtil.sendToSever(new PacketOpenGui(GuiInfo.GUI_CAMPINV_TOOL));
				}
				else
				{
					player.closeScreen();
					player.openGui(CampingMod.instance, GuiInfo.GUI_INV_CREATIVE, player.worldObj, 0, 0, 0);
				}
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick[1] = true;
			}

			handler.updateContend(1, pointX, pointY, true);
		}
		else
		{
			canClick[1] = false;
		}

		if(isPointInRegion((mainGuiWidth - 25) + handler.posX(0), 5 + handler.posY(0), 22, 22, pointX, pointY, mainGuiLeft, mainGuiTop))
		{
			if(Mouse.isButtonDown(0) && clickReady && canClick[2])
			{
				if(id == GuiInfo.GUI_INV_CREATIVE)
				{
					ModAchievements.guide.addStatToPlayer(player);
					clickReady = false;
					player.openGui(CampingMod.instance, GuiInfo.GUI_GUIDE, player.worldObj, 0, 0, 0);
				}
			}
			if(!Mouse.isButtonDown(0))
			{
				canClick[2] = true;
			}
			handler.updateContend(0, pointX, pointY, true);
		}
		else
		{
			canClick[2] = false;
		}

		handler.updateContend(0, pointX, pointY, false);
		handler.updateContend(1, pointX, pointY, false);
		handler.updateContend(2, pointX, pointY, false);
	}

	private boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY, int guiLeft, int guiTop)
	{
		pointX -= guiLeft;
		pointY -= guiTop;
		return (pointX >= (x - 1)) && (pointX < (x + width + 1)) && (pointY >= (y - 1)) && (pointY < (y + height + 1));
	}

	@Override
	public void onGuiClosed()
	{
		handler.writeToNBT(player);
	}
}
