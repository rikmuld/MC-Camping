package com.rikmuld.camping.features.general.keys

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Library.KeyInfo
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
object EventsClient {

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent) {
    if (!FMLClientHandler.instance.isGUIOpen(classOf[GuiChat])) {
      if (CampingMod.MISC.keyOpenCamping.isPressed) {
        keyPressed(KeyInfo.INVENTORY_KEY)
        PacketSender.sendToServer(new PacketKeyData(KeyInfo.INVENTORY_KEY))
      }
    }
  }

  def keyPressed(id: Int): Unit = id match {
    case _ =>
  }
}
