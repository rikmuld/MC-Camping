package com.rikmuld.camping

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Library.ConfigInfo._
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.{ConfigElement, Configuration}
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import net.minecraftforge.fml.client.config.{GuiConfig, IConfigElement}

import scala.collection.JavaConversions._

class Config(val file: Configuration) {
  var useBears = true
  var campfireMaxFuel = 20000
  var campfireCoalFuel = 1000
  var cookTimeSpit = 300
  var cookTimePan = 1000
  var cookTimeGrill = 600
  var hempSpeed = 1f
  var hasWorldGen = true
  var worldGenHemp = true
  var hempGenMulti = 5
  var useFoxes = true
  var trapPlayer = true
  var worldGenCampsite = true
  var campsiteRareness = 5
  var alwaysCampingInv = true
  var welcomeMess = true
  var maxWoodFuel = 5000

  var elements: List[IConfigElement] =
    _

  def sync(): Unit = {
    campfireMaxFuel = getVar("Campfire Max Fuel", "Max fuel of a campfire in ticks.", CAT_CAMPFIRE, campfireMaxFuel).asInstanceOf[Integer]
    campfireCoalFuel = getVar("Coal Fuel", "Fuel worth of 1 coal pice in ticks.", CAT_CAMPFIRE, campfireCoalFuel).asInstanceOf[Integer]
    cookTimeSpit = getVar("Spit cook time", "Time in ticks to complete spit cook cycle.", CAT_CAMPFIRE, cookTimeSpit).asInstanceOf[Integer]
    cookTimePan = getVar("Pan cook time", "Time in ticks to complete pan cook cycle.", CAT_CAMPFIRE, cookTimePan).asInstanceOf[Integer]
    cookTimeGrill = getVar("Grill cook time", "Time in ticks to complete grill cook cycle.", CAT_CAMPFIRE, cookTimeGrill).asInstanceOf[Integer]
    hempSpeed = getVar("Hemp Growth Speed", "The growth rate of the hemp plant.", CAT_WORLD, hempSpeed).asInstanceOf[Float]
    hasWorldGen = getVar("Enable World generation", "Enable/Disable all of the world generation of this mod.", CAT_WORLD, hasWorldGen).asInstanceOf[Boolean]
    worldGenHemp = getVar("Enable Hemp generation", "Enable/Disable the world generation for Hemp plants.", CAT_WORLD, worldGenHemp).asInstanceOf[Boolean]
    hempGenMulti = getVar("Hemp Generation Multiplier", "Multiplier for the hemp world generaion.", CAT_WORLD, hempGenMulti).asInstanceOf[Integer]
    useBears = getVar("Spawn Bears", "Spawn Grizzly Bears in forrests", CAT_WORLD, useBears).asInstanceOf[Boolean]
    useFoxes = getVar("Spawn Foxes", "Spawn Arctic Foxes in snowy regions", CAT_WORLD, useFoxes).asInstanceOf[Boolean]
    trapPlayer = getVar("Trap players in Bear Trap", "Bear traps can trap players.", CAT_TOOLS, trapPlayer).asInstanceOf[Boolean]
    worldGenHemp = getVar("Enable Campsite generation", "Enable/Disable the world generation for Campsites.", CAT_WORLD, worldGenCampsite).asInstanceOf[Boolean]
    campsiteRareness = getVar("Campsite Rareness", "Rareness of the campsite world generation", CAT_WORLD, campsiteRareness).asInstanceOf[Integer]
    alwaysCampingInv = getVar("Always use the Camping Inventory", "Set to false to hide the Camping Inventory (can be triggered using the `C' key)", CAT_GENERAL, alwaysCampingInv).asInstanceOf[Boolean]
    welcomeMess = getVar("Welcome Message", "Print the welcome message", CAT_GENERAL, welcomeMess).asInstanceOf[Boolean]
    maxWoodFuel = getVar("Provisional Campfire Burn Time", "The burn time of a provisional campfire", CAT_CAMPFIRE, maxWoodFuel).asInstanceOf[Integer]

    if (file.hasChanged)
      file.save

    elements = file.getCategoryNames.map(file.getCategory).flatMap(new ConfigElement(_).getChildElements).toList
  }

  def getVar(name: String, desc: String, cat: String, curr: Any): Any = curr match {
    case int: Int =>
      file.getInt(name, cat, int, 0, Integer.MAX_VALUE, desc)
    case float: Float =>
      file.getFloat(name, cat, float, 0, Float.MaxValue, desc)
    case boolean: Boolean =>
      file.getBoolean(name, cat, boolean, desc)
    case string: String =>
      file.getString(name, cat, string, desc)
  }

  def getVar(name: String, desc: String, cat: String, curr: Any, min: Float, max: Float): Any = curr match {
    case int: Int =>
      file.getInt(name, cat, int, min.toInt, max.toInt, desc)
    case float: Float =>
      file.getFloat(name, cat, float, min, max, desc)
  }

  def disableMess(){
    file.getCategory(CAT_GENERAL).get("Welcome Message").setValue(false)
    file.save()
  }
}

class GuiFactory extends IModGuiFactory {
  override def initialize(minecraftInstance: Minecraft): Unit =
    Unit

  override def runtimeGuiCategories: java.util.Set[RuntimeOptionCategoryElement] =
    null

  override def createConfigGui(parentScreen: GuiScreen): GuiScreen =
    new ConfigGUI(parentScreen)

  override def hasConfigGui: Boolean =
    true
}

class ConfigGUI(parent: GuiScreen) extends GuiConfig(parent, config.elements, MOD_ID, false, false, "Camping Config")