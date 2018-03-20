package com.rikmuld.camping.features.blocks.campfire.cook

import com.rikmuld.camping.Library.TextureInfo
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
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