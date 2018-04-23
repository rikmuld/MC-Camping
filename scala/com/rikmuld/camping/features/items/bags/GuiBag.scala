package com.rikmuld.camping.features.items.bags

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Backpack._
import com.rikmuld.camping.Library._
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

object GuiBag {
  final val TEXTURE =
    new ResourceLocation(TextureInfo.GUI_SIMPLE)
}

abstract class GuiBag(container: Container, metaData: Int, x: Int, y: Int, width: Int, height: Int) extends GuiContainerSimple(container) {
  val getTexture: ResourceLocation =
    GuiBag.TEXTURE

  override def getName: String =
    new ItemStack(CampingMod.OBJ.backpack, 1, metaData).getDisplayName

  override def hasName: Boolean =
    true

  override def drawGUI(mouseX: Int, mouseY: Int) {
    super.drawGUI(mouseX, mouseY)
    drawTexturedModalRect(guiLeft + x, guiTop + y, 0, 166, width, height)
  }
}

class GuiPouch(player: EntityPlayer) extends GuiBag(new ContainerPouch(player), POUCH, 61, 43, 54, 18)

class GuiBackpack(player: EntityPlayer) extends GuiBag(new ContainerBackpack(player), BACKPACK, 61, 25, 54, 54)

class GuiRucksack(player: EntityPlayer) extends GuiBag(new ContainerRucksack(player), RUCKSACK, 7, 25, 162, 54)