package com.rikmuld.camping.features.blocks.tent

import com.rikmuld.camping.features.blocks.tent.TileEntityTent._
import com.rikmuld.camping.inventory.{SlotItem, SlotState}
import com.rikmuld.corerm.gui.container.ContainerSimple
import com.rikmuld.corerm.utils.MathUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

// make a tabbed GUI :)
// sleeping tab, lantern tab, chests tab, content tab (just an inventory)
// this also fixes the tent background lighting issue

class ContainerTentLanterns(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileEntityTent](player) {
  override def playerInvY: Int =
    113

  def getID: String =
    tile.getName

  def addInventorySlots(): Unit =
    addSlotToContainer(new SlotItem(tile, 0, 80, 88, Items.GLOWSTONE_DUST))

  def initIInventory: TileEntityTent =
    tile.asInstanceOf[TileEntityTent]
}

class ContainerTentChests(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileEntityTent](player) {
  setSlider(0)

  override def playerInvY: Int =
    146

  override def playerInvX: Int =
    27

  val getID: String =
    tile.getName

  var slots: Seq[Seq[SlotState]] =
    _

  override def addInventorySlots(): Unit =
    slots =
      for (column <- 0 until 25)
        yield for(row <- 0 until 6)
          yield {
            val slot = new SlotState(tile, row + (column * 6) + 1, 9 + (column * 18), 34 + (row * 18))
            slot.disable()

            addSlotToContainer(slot)
            slot
          }

  def initIInventory: TileEntityTent =
    tile.asInstanceOf[TileEntityTent]

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    mergeItemStack(stack, 0, getIInventory.count(CHESTS) * 5 * 6, false)

  def setSlider(x: Int): Unit = {
    val chests = getIInventory.count(TileEntityTent.CHESTS)

    if (chests > 2) {
      val scaledSlide = MathUtils.getScaledNumber(x, 144, (5 * chests) - 11).toInt
      for (i <- 0 until (5 * chests); j <- 0 until 6) {
        slots(i)(j).setStateX(scaledSlide)
        if ((i < scaledSlide) || (i >= (scaledSlide + 11))) {
          slots(i)(j).disable()
        } else {
          slots(i)(j).enable()
        }
      }
    } else {
      for (i <- 0 until (5 * chests); j <- 0 until 6) {
        slots(i)(j).setStateX(0)
        slots(i)(j).enable()
      }
    }
  }
}