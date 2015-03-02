package com.rikmuld.camping.common.inventory

import scala.collection.mutable.ListBuffer
import com.rikmuld.camping.client.gui.GuiTabbed
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.entity.item.EntityPainting
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.SlotCrafting
import com.rikmuld.corerm.common.inventory.SlotWithDisable
import com.rikmuld.corerm.common.inventory.SlotWithItemsNot
import com.rikmuld.corerm.common.inventory.SlotWithNoPickup

class SlotCooking(par1iInventory: IInventory, id: Int, x: Int, y: Int) extends Slot(par1iInventory, id, x, y) {
  var active: Boolean = _
  var equipment: CookingEquipment = _
  var fire: TileEntityCampfireCook = _

  deActivate()

  def activate(x: Int, y: Int, equipment: CookingEquipment, fire: TileEntityCampfireCook) {
    active = true
    this.equipment = equipment
    this.fire = fire
    xDisplayPosition = x
    yDisplayPosition = y
  }
  def deActivate() {
    active = false
    equipment = null
    fire = null
    xDisplayPosition = -1000
    yDisplayPosition = -1000
  }
  override def getSlotStackLimit(): Int = 1
  override def isItemValid(stack: ItemStack): Boolean = if ((equipment != null) && (fire != null)) equipment.canCook(stack) else false
}

class SlotState(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) with SlotWithDisable{
  setDisableSlot(x, y)
  
  var stateX: Int = xFlag
  var stateY: Int = yFlag

  override def enable() {
    xDisplayPosition = stateX
    yDisplayPosition = stateY
  }
  def setStateX(state: Int) = stateX = xFlag - (18 * state)
  def setStateY(state: Int) = stateY = yFlag - (18 * state)
}

trait SlotWithTabs extends SlotWithDisable {
  disable
  var canEnable:Boolean = _
  var tabIdLeft:Int = _
  var tabIdTop:Int = _
  def setTabIds(top:Int, left:Int){
    this.tabIdLeft = left
    this.tabIdTop = top
  }
  def updateTab(player:EntityPlayer, idLeft:Int, idTop:Int){
    if(tabIdLeft==idLeft&&tabIdTop==idTop){
      canEnable = true
      if(!enabled)enable
    } else {
      canEnable = false
      if(enabled)disable
    }
  }
  override def enable = if(canEnable)super.enable
}

class SlotTabbed(inv: IInventory, id: Int, xFlag: Int, yFlag: Int, tabIdTop:Int, tabIdLeft:Int) extends Slot(inv, id, xFlag, yFlag) with SlotWithTabs {
  setTabIds(tabIdTop, tabIdLeft)
  setDisableSlot(xFlag, yFlag)
}

class SlotTabbedItemsNot(inv: IInventory, id: Int, xFlag: Int, yFlag: Int, tabIdTop:Int, tabIdLeft:Int, stacks: AnyRef*) extends Slot(inv, id, xFlag, yFlag) with SlotWithItemsNot with SlotWithTabs {
  setTabIds(tabIdTop, tabIdLeft)
  setDisableSlot(xFlag, yFlag)
  setStacks(stacks)
}

class SlotTabbedCrafting(player:EntityPlayer, craft:IInventory, inv: IInventory, id: Int, x: Int, y: Int, tabIdTop:Int, tabIdLeft:Int) extends SlotCrafting(player, craft, inv, id, x, y) with SlotWithTabs {
  setTabIds(tabIdTop, tabIdLeft)
  setDisableSlot(x, y)
}