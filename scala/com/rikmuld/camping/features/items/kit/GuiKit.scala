package com.rikmuld.camping.features.items.kit

import com.rikmuld.camping.Library._
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation

class GuiKit(player: EntityPlayer) extends GuiContainerSimple(new ContainerKit(player)) {
  ySize = 181

  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_KIT)
}