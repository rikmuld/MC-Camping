package com.rikmuld.camping.features.general.keys

import com.rikmuld.camping.Library.{GuiInfo, KeyInfo}
import com.rikmuld.corerm.gui.GuiHelper
import net.minecraft.entity.player.EntityPlayer

object EventsServer {
  def keyPressed(player: EntityPlayer, id: Int): Unit = id match {
    case KeyInfo.INVENTORY_KEY =>
      GuiHelper.openGui(GuiInfo.CAMPING, player)
    case _ =>
  }
}
