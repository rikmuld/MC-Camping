package com.rikmuld.camping.core

import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.client.config.IConfigElement
import java.util.ArrayList

class Config(val file: Configuration) {
  var toolUse = 200
  var elements: java.util.List[IConfigElement[_]] = new ArrayList[IConfigElement[_]];

  def sync = {
    toolUse = getVar("Knife Durability", "The durability of the pocket knife.", ConfigInfo.CAT_TOOLS, toolUse).asInstanceOf[Integer]

    if (file.hasChanged) file.save
    for (i <- 0 until file.getCategoryNames.size) elements.addAll(new ConfigElement(file.getCategory(file.getCategoryNames().toArray().apply(i).asInstanceOf[String])).getChildElements());
  }
  def getVar(name: String, desc: String, cat: String, curr: Any): Any = {
    if (curr.isInstanceOf[Integer]) return file.getInt(name, cat, curr.asInstanceOf[Integer], 0, Integer.MAX_VALUE, desc)
    else if (curr.isInstanceOf[Double]) return file.getFloat(name, cat, curr.asInstanceOf[Float], 0, Float.MaxValue, desc)
    else if (curr.isInstanceOf[Boolean]) return file.getBoolean(name, cat, curr.asInstanceOf[Boolean], desc)
    else if (curr.isInstanceOf[String]) return file.getString(name, cat, curr.asInstanceOf[String], desc)
  }
}

class GuiFactory extends IModGuiFactory {
  override def initialize(minecraftInstance: Minecraft) = {}
  override def mainConfigGuiClass(): Class[GuiScreen] = classOf[ConfigGUI].asInstanceOf[Class[GuiScreen]]
  override def runtimeGuiCategories: java.util.Set[RuntimeOptionCategoryElement] = null
  override def getHandlerFor(element: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = null;
}

class ConfigGUI(parent: GuiScreen) extends GuiConfig(parent, Objs.config.elements, ModInfo.MOD_ID, false, false, "Camping Config!")