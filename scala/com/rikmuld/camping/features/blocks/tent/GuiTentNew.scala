package com.rikmuld.camping.features.blocks.tent

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Tent
import com.rikmuld.camping.Library.TextureInfo
import com.rikmuld.camping.features.blocks.tent.GuiTentNew._
import com.rikmuld.corerm.gui.gui.{GuiTabbed, Tab}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

object GuiTentNew {
  final val TEXTURE =
    new ResourceLocation(TextureInfo.GUI_SIMPLE)

  final val TAB_CONTENTS =
    0

  final val TAB_STORAGE =
    1
}

class GuiTentNew(player: EntityPlayer, tile: TileEntityTent) extends GuiTabbed(player, new ContainerTentNew(player, tile)) {
  override def getTabs: Seq[Tab] = Seq(
    new Tab("Manage Tent", 0, this, TAB_CONTENTS, new ItemStack(CampingMod.OBJ.tent, 0, Tent.WHITE)),
    new Tab("Storage", 0, this, TAB_STORAGE, new ItemStack(Blocks.CHEST))
  )

  override def drawPage(tab: Int, mouseX: Int, mouseY: Int): Unit = tab match {
    case TAB_STORAGE =>
      //render in storage
      //render chest buttons for which chest
    case TAB_CONTENTS =>
      //render in slots where needed, render some text, add sleeping button, add glowstone add and progress stuff
  }

  override val getTexture: ResourceLocation =
    TEXTURE
}
