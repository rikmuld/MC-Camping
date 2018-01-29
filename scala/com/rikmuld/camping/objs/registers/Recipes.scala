package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.utils.CoreUtils._
import net.minecraftforge.fml.common.registry.GameRegistry

object ModRecipes {
  def register(): Unit = {
    GameRegistry.addSmelting(nwsk(venisonRaw), nwsk(venisonCooked), 3)
  }
}
