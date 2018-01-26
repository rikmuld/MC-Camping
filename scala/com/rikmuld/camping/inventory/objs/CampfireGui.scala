package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.Lib.TextureInfo
import com.rikmuld.camping.inventory.{SlotCooking, SlotItem, SlotItemMeta}
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.objs.ItemDefinitions.Kit
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.corerm.inventory.container.ContainerSimple
import com.rikmuld.corerm.inventory.gui.GuiContainerSimple
import com.rikmuld.corerm.inventory.slots.{SlotDisable, SlotOnly}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.{IContainerListener, IInventory, Slot}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.ResourceLocation

class GuiCampfireCook(player: EntityPlayer, tile: IInventory) extends GuiContainerSimple(new ContainerCampfireCook(player, tile)) {
  ySize =
    188

  val fire =
    tile.asInstanceOf[TileCampfireCook]

  override def getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_CAMPFIRE_COOK)

  override def drawGUI(mouseX: Int, mouseY: Int) {
    super.drawGUI(mouseX, mouseY)

    val equipment = fire.equipment
    val coal = fire.getScaledCoal(40).toInt + 1

    if (equipment != null)
      fire.equipment.drawGuiTexture(this)

    drawTexturedModalRect(guiLeft + 66, (guiTop + 94) - coal, 176, 40 - coal, 44, coal)
    drawTexturedModalRect(guiLeft + 79, guiTop + 83, 79, 105, 18, 18)

    if (equipment != null)
      drawCookProcess(equipment)
  }

  def drawCookProcess(equipment: CookingEquipment): Unit =
    for (i <- 0 until equipment.maxFood) {
      val process = fire.getScaledcookProgress(10, i).toInt
      val food = fire.getStackInSlot(i + 2)

      val barOffset =
        if(!food.isEmpty && !equipment.canCook(food)) 227
        else 226

      val offsetX = equipment.slots(0)(i)
      val offsetY = equipment.slots(1)(i)

      drawTexturedModalRect(guiLeft + offsetX + 16, guiTop + offsetY + 2, 223, 0, 3, 12)
      drawTexturedModalRect(guiLeft + offsetX + 17, guiTop + offsetY + 13 - process, barOffset, 11 - process, 1, process)
    }
}

class ContainerCampfireCook(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileCampfireCook](player) {
  override def playerInvY: Int =
    106

  override def addInventorySlots(): Unit = {
    addSlotToContainer(new SlotItem(tile, 0, 80, 84, Items.COAL))
    addSlotToContainer(new SlotItemMeta(tile, 1, 150, 9, Objs.kit, Vector(Kit.SPIT, Kit.GRILL, Kit.PAN)))

    val slots = for (i <- 0 until 10)
      yield new SlotCooking(tile, i + 2, 0, 0)

    getIInventory.setSlots(slots)
    slots.foreach(slot => addSlotToContainer(slot))
  }

  override def initIInventory: TileCampfireCook =
    tile.asInstanceOf[TileCampfireCook]

  override def addListener(crafting: IContainerListener) {
    super.addListener(crafting)
    for (i <- getIInventory.cookProgress.indices) {
      crafting.sendWindowProperty(this, i, getIInventory.cookProgress(i))
    }
  }

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    if (stack.getItem == Items.COAL) {
      mergeItemStack(stack, 0, 1, false)
    } else if (stack.getItem == Objs.kit && stack.getItemDamage != Kit.USELESS && stack.getItemDamage != Kit.EMPTY) {
      mergeItemStack(stack, 1, 2, false)
    } else if(getIInventory.getEquipment.fold(false)(_.canCook(stack))) {
      mergeItemStack(stack, 2, 2 + getIInventory.equipment.maxFood, false)
    } else false
}