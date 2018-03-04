package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.Library.{AdvancementInfo, TextureInfo}
import com.rikmuld.camping.inventory.{SlotCooking, SlotItem, SlotItemMeta}
import com.rikmuld.camping.objs.Definitions.Kit
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileCampfireCook
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.gui.container.ContainerSimple
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class GuiCampfireCook(player: EntityPlayer, tile: IInventory)
  extends GuiContainerSimple(new ContainerCampfireCook(player, tile)) {

  ySize =
    188

  val fire: TileCampfireCook =
    tile.asInstanceOf[TileCampfireCook]

  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_CAMPFIRE_COOK)

  override def drawGUI(mouseX: Int, mouseY: Int) {
    super.drawGUI(mouseX, mouseY)

    val coal = fire.getScaledFuel(40).toInt + 1

    fire.getEquipment.foreach(_.renderGUIBackground(this))

    drawTexturedModalRect(guiLeft + 66, (guiTop + 94) - coal, 176, 40 - coal, 44, coal)
    drawTexturedModalRect(guiLeft + 79, guiTop + 83, 79, 105, 18, 18)

    for (i <- 0 until fire.getEquipment.fold(0)(_.getMaxCookingSlot))
      drawCookProcess(i)
  }

  def drawCookProcess(slot: Int): Unit = {
    val process = fire.getScaledCookProgress(10, slot).toInt
    val food = fire.getStackInSlot(slot + 2)
    val equipment = fire.getEquipment.get

    val barOffset =
      if (!equipment.canCook(food)) 227
      else 226

    val slotOffset =
      equipment.getSlotPosition(slot)

    drawTexturedModalRect(guiLeft + slotOffset._1 + 16, guiTop + slotOffset._2 + 2, 223, 0, 3, 12)
    drawTexturedModalRect(guiLeft + slotOffset._1 + 17, guiTop + slotOffset._2 + 13 - process, barOffset, 11 - process, 1, process)
  }
}

class ContainerCampfireCook(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileCampfireCook](player) {
  override def playerInvY: Int =
    106

  override def addInventorySlots(): Unit = {
    val thisPlayer = player

    addSlotToContainer(new SlotItem(tile, 0, 80, 84, Items.COAL))
    addSlotToContainer(new SlotItemMeta(tile, 1, 150, 9, ObjRegistry.kit, Vector(Kit.SPIT, Kit.GRILL, Kit.PAN)) with SlotEquipment {
      val player: EntityPlayer = thisPlayer
    })

    val slots = for (i <- 0 until 10)
      yield new SlotCooking(tile, i + 2, 0, 0)

    getIInventory.setSlots(slots)
    slots.foreach(slot => addSlotToContainer(slot))
  }

  override def initIInventory: TileCampfireCook =
    tile.asInstanceOf[TileCampfireCook]

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    if (stack.getItem == Items.COAL) {
      mergeItemStack(stack, 0, 1, false)
    } else if (stack.getItem == ObjRegistry.kit && stack.getItemDamage != Kit.USELESS && stack.getItemDamage != Kit.EMPTY) {
      mergeItemStack(stack, 1, 2, false)
    } else if(getIInventory.getEquipment.fold(false)(_.canCook(stack))) {
      mergeItemStack(stack, 2, 2 + getIInventory.getEquipment.get.getMaxCookingSlot, false)
    } else false

  def getID: String =
    tile.getName
}

trait SlotEquipment extends Slot {
  val player: EntityPlayer

  override def onSlotChanged(): Unit = {
    super.onSlotChanged()

    if (!getStack.isEmpty && !player.world.isRemote)
      TriggerHelper.trigger(AdvancementInfo.CAMPFIRES_MADE, player, getStack.getItemDamage)
  }
}