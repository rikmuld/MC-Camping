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
  var campfireMaxFuel = 20000
  var campfireCoalFuel = 1000
  var cookTimeSpit = 300
  var cookTimePan = 1000
  var cookTimeGrill = 600
  var marshSaturation = 0.2f
  var marshHeal = 3
  var hempSpeed = 1f
  var hasWorldGen = true
  var worldGenHemp = true
  var hempGenMulti = 2
  
  var elements: java.util.List[IConfigElement[_]] = new ArrayList[IConfigElement[_]];

  def sync = {
    toolUse = getVar("Knife Durability", "The durability of the pocket knife.", ConfigInfo.CAT_TOOLS, toolUse).asInstanceOf[Integer]
    campfireMaxFuel = getVar("Campfire Max Fuel", "Max fuel of a campfire in ticks.", ConfigInfo.CAT_CAMPFIRE, campfireMaxFuel).asInstanceOf[Integer]
    campfireCoalFuel = getVar("Coal Fuel", "Fuel worth of 1 coal pice in ticks.", ConfigInfo.CAT_CAMPFIRE, campfireCoalFuel).asInstanceOf[Integer]
    cookTimeSpit = getVar("Spit cook time", "Time in ticks to complete spit cook cycle.", ConfigInfo.CAT_CAMPFIRE, cookTimeSpit).asInstanceOf[Integer]
    cookTimePan = getVar("Pan cook time", "Time in ticks to complete pan cook cycle.", ConfigInfo.CAT_CAMPFIRE, cookTimePan).asInstanceOf[Integer]
    cookTimeGrill = getVar("Grill cook time", "Time in ticks to complete grill cook cycle.", ConfigInfo.CAT_CAMPFIRE, cookTimeGrill).asInstanceOf[Integer]
    marshSaturation = getVar("Marshmallow Saturation Amount", "The amount of saturation a marshmallow gives.", ConfigInfo.CAT_FOOD, marshSaturation).asInstanceOf[Float]
    marshHeal = getVar("Marshmallow Heal Amount", "The amount of lives a marshmallow heals (in half harts).", ConfigInfo.CAT_FOOD, marshHeal).asInstanceOf[Integer]
    hempSpeed = getVar("Hemp Growth Speed", "The growth rate of the hemp plant.", ConfigInfo.CAT_WORLD, hempSpeed).asInstanceOf[Float]
    hasWorldGen = getVar("Enable World generation", "Enable/Disable all of the world generation of this mod.", ConfigInfo.CAT_WORLD, hasWorldGen).asInstanceOf[Boolean]
    worldGenHemp = getVar("Enable Hemp generation", "Enable/Disable the world generation of Hemp plants.", ConfigInfo.CAT_WORLD, worldGenHemp).asInstanceOf[Boolean]
    hempGenMulti = getVar("Hemp Generation Multiplier", "Multiplier for the hemp world generaion.", ConfigInfo.CAT_WORLD, hempGenMulti).asInstanceOf[Integer]

    if (file.hasChanged) file.save
    for (i <- 0 until file.getCategoryNames.size) elements.addAll(new ConfigElement(file.getCategory(file.getCategoryNames().toArray().apply(i).asInstanceOf[String])).getChildElements());
  }
  def getVar(name: String, desc: String, cat: String, curr: Any): Any = {
    if (curr.isInstanceOf[Integer]) return file.getInt(name, cat, curr.asInstanceOf[Integer], 0, Integer.MAX_VALUE, desc)
    else if (curr.isInstanceOf[Float]) return file.getFloat(name, cat, curr.asInstanceOf[Float], 0, Float.MaxValue, desc)
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