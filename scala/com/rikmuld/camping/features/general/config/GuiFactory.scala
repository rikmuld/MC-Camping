package com.rikmuld.camping.features.general.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement

class GuiFactory extends IModGuiFactory {
  override def initialize(minecraftInstance: Minecraft): Unit =
    Unit

  override def runtimeGuiCategories: java.util.Set[RuntimeOptionCategoryElement] =
    null

  override def createConfigGui(parentScreen: GuiScreen): GuiScreen =
    new GuiConfig(parentScreen)

  override def hasConfigGui: Boolean =
    true
}