package com.rikmuld.camping.features.blocks.trap

import com.rikmuld.camping.Library.TextureInfo
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation

class GuiTrap(player: EntityPlayer, inv: IInventory) extends GuiContainerSimple(new ContainerTrap(player, inv)) {
  ySize = 120

  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_TRAP)
}