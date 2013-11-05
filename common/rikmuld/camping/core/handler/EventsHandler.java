package rikmuld.camping.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import rikmuld.camping.block.plant.BlockFlowerHemp;
import rikmuld.camping.client.gui.screen.GuiScreenMapHUD;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.CampingInvUtil;
import rikmuld.camping.inventory.player.InventoryCampingInvBack;
import rikmuld.camping.inventory.player.InventoryCampingInvTool;

public class EventsHandler{

	Minecraft mc = Minecraft.getMinecraft();
	public static GuiScreenMapHUD map = new GuiScreenMapHUD();
	
    @ForgeSubscribe
    public void onUseBonemeal(BonemealEvent event)
    {
        if (event.ID == ModBlocks.hemp.blockID)
        {
        	((BlockFlowerHemp)ModBlocks.hemp).Grow(event.world, event.X, event.Y, event.Z, event);
        }
    }
    
	@ForgeSubscribe
	public void onPlayerDead(PlayerDropsEvent event)
	{
		InventoryCampingInvBack.dropItems(event.entityPlayer);
		InventoryCampingInvTool.dropItems(event.entityPlayer);
	}
	
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		  if(event.isCancelable() || event.type != ElementType.EXPERIENCE) return;
		  if(CampingInvUtil.hasMap(mc.thePlayer))
		  {			 
			  ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			  int i = scaledResolution.getScaledWidth();
			  int j = scaledResolution.getScaledHeight(); 
			  
			  map.setWorldAndResolution(mc, i, j);
			  map.drawScreen(event.mouseX, event.mouseY, event.partialTicks);
		  }
	}
}