package com.rikmuld.camping.features.general.config

import com.rikmuld.camping.CampingMod.{CONFIG, MOD_ID}
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.config

import scala.collection.JavaConversions._

class GuiConfig(parent: GuiScreen) extends config.GuiConfig(
  parent,
  CONFIG.elements,
  MOD_ID,
  false,
  false,
  "Camping Config"
)