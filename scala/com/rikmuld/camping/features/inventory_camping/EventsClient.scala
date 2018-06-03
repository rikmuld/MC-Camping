package com.rikmuld.camping.features.inventory_camping

import com.rikmuld.camping.CampingMod.CONFIG
import com.rikmuld.camping.Library.GuiInfo
import com.rikmuld.camping.features.inventory_camping.GuiMap.MapData
import com.rikmuld.corerm.gui.GuiHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.client.event.{GuiOpenEvent, RenderGameOverlayEvent}
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(Array(Side.CLIENT))
object EventsClient {

  var map: Option[GuiMap] =
    None

  var updateMap: Boolean =
    false

  var mapData: MapData =
    _

  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent): Unit = {
    if (updateMap) {
      map =
        if (mapData.colors.isEmpty) None
        else Some(new GuiMap(mapData))

      updateMap = false
    }

    map.foreach(map =>
      if (event.getType == ElementType.HOTBAR) {
        val mc = FMLClientHandler.instance().getClient
        val inv = InventoryCamping.getInventory(mc.player)

        if (!inv(InventoryCamping.SLOT_MAP.toByte).isEmpty)
          map.drawMap(mc, event.getResolution.getScaledWidth, event.getResolution.getScaledHeight)
      }
    )
  }

  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if(event.getGui.isInstanceOf[GuiInventory] && CONFIG.alwaysCampingInv){
      if(Minecraft.getMinecraft.player.capabilities.isCreativeMode) return;
      event.setCanceled(true)
      GuiHelper.forceOpenGui(GuiInfo.CAMPING, Minecraft.getMinecraft.player)
    }
  }
}
