package rikmuld.camping.core.handler;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import rikmuld.camping.block.BlockSapling;
import rikmuld.camping.block.plant.BlockFlowerHemp;
import rikmuld.camping.client.gui.screen.GuiScreenMapHUD;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.CampingInvUtil;
import rikmuld.camping.inventory.player.InventoryCampingInvBack;
import rikmuld.camping.inventory.player.InventoryCampingInvTool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventsHandler {

	Minecraft mc;
	public static GuiScreenMapHUD map;
	Random random = new Random();

	@ForgeSubscribe
	public void onPlayerDead(PlayerDropsEvent event)
	{
		InventoryCampingInvBack.dropItems(event.entityPlayer);
		InventoryCampingInvTool.dropItems(event.entityPlayer);
	}

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		if(mc == null)
		{
			mc = Minecraft.getMinecraft();
		}
		if(map == null)
		{
			map = new GuiScreenMapHUD();
		}

		if(event.isCancelable() || (event.type != ElementType.EXPERIENCE)) return;
		if(CampingInvUtil.hasMap(mc.thePlayer))
		{
			map.setWorldAndResolution(mc, event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
			map.drawScreen(event.mouseX, event.mouseY, event.partialTicks);
		}
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void onUseBonemeal(BonemealEvent event)
	{
		if(event.ID == ModBlocks.hemp.blockID)
		{
			((BlockFlowerHemp)ModBlocks.hemp).Grow(event.world, event.X, event.Y, event.Z, event);
		}
		if(event.ID == ModBlocks.sapling.blockID)
		{
			((BlockSapling)ModBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, random, event);
		}
	}
}