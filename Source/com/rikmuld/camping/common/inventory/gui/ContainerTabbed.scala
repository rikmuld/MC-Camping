package com.rikmuld.camping.common.inventory.gui

import net.minecraft.inventory.Container
import net.minecraft.entity.player.EntityPlayer
import scala.collection.JavaConversions._
import com.rikmuld.camping.common.inventory.SlotTabbed
import com.rikmuld.camping.common.inventory.SlotWithTabs

trait ContainerTabbed extends Container {
  def updateTab(player:EntityPlayer, idLeft:Int, idTop:Int){
    inventorySlots.foreach(slot => {if(slot.isInstanceOf[SlotWithTabs])slot.asInstanceOf[SlotWithTabs].updateTab(player, idLeft, idTop)})
  }
}