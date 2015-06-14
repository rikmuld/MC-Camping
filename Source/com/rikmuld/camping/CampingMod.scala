package com.rikmuld.camping

import scala.collection.mutable.ListBuffer
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.Properties._
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.client.model.ISmartItemModel
import net.minecraftforge.client.model.ISmartBlockModel
import net.minecraftforge.fml.relauncher.SideOnly
@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, modLanguage = MOD_LANUAGE)
object CampingMod {
  final val MOD_ID = "camping"
  final val MOD_NAME = "The Camping Mod 2"
  final val MOD_VERSION = "2.2"
  final val MOD_LANUAGE = "scala"
  final val MOD_DEPENDENCIES = "required-after:Forge@[v10.13.2.1231,);required-after:corerm@[1.1,)"
  final val MOD_SERVER_PROXY = "com.rikmuld."+MOD_ID+".ProxyServer"
  final val MOD_CLIENT_PROXY = "com.rikmuld."+MOD_ID+".ProxyClient"
  final val MOD_GUIFACTORY = "com.rikmuld.camping.core.GuiFactory"
  final val PACKET_CHANEL = MOD_ID
  
  val registers = new ListBuffer[ModRegister]

  @SidedProxy(clientSide = MOD_CLIENT_PROXY, serverSide = MOD_SERVER_PROXY)
  var proxy: ProxyServer = _
  var config: Config = _

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    config = new Config(new Configuration(event.getSuggestedConfigurationFile()))
    config.sync
    proxy.registerEvents
  }
  @EventHandler
  def Init(event: FMLInitializationEvent) {
    register(event.getSide, ModMisc, ModRegister.PERI)
    register(event.getSide, ModGuis, ModRegister.PERI)
    register(event.getSide, ModEntities, ModRegister.PERI)
    register(event.getSide, ModTiles, ModRegister.PERI)
    register(event.getSide, ModBlocks, ModRegister.PERI)
    register(event.getSide, ModItems, ModRegister.PERI)
    register(event.getSide, ModRecipes, ModRegister.PERI)
  }
  @EventHandler
  def PosInit(event: FMLPostInitializationEvent) {
    register(event.getSide, ModEntities, ModRegister.POST)
    register(event.getSide, ModMisc, ModRegister.POST)
  }
  
  def register(side:Side, register:ModRegister, phase:Int) {    
    register.phase=phase

    register.register
    if(side == Side.CLIENT)register.registerClient
    if(side == Side.SERVER)register.registerServer    
  }
}

object Lib {
  object ConfigInfo {
    final val CAT_ANIMALS = "animals"
    final val CAT_WORLD = "world genaration"
    final val CAT_TOOLS = "tools"
    final val CAT_FOOD = "food"
    final val CAT_GENERAL = "general"
    final val CAT_CAMPFIRE = "campfire"
  }
  
  object EntityInfo {
    final val BEAR = 0
    final val FOX = 1
    final val CAMPER = 2
    final val MOUNTABLE = 3
  }
  
  object NBTInfo {
    final val INV_CAMPING = "campInv"
  }
  
  object PotionInfo {
    final val BLEEDING = "Bleeding"
  }
  
  object DamageInfo {
    final val BLEEDING = "bleedingSource"
  }

  object KeyInfo {
    final val CATAGORY_MOD = "The Camping Mod"
    final val INVENTORY_KEY = 0
  
    final val desc: Array[String] = Array(
      "Camping Inventory Key")
  
    final val default: Array[Integer] = Array(
      Keyboard.KEY_C)
  }
  
  object TextureInfo {
    final val GUI_LOCATION = MOD_ID + ":textures/gui/"
    final val MODEL_LOCATION = MOD_ID + ":textures/models/"
    final val SPRITE_LOCATION = MOD_ID + ":textures/sprite/"
    final val MC_LOCATION = "minecraft:textures/"
    final val MC_GUI_LOCATION = MC_LOCATION + "gui/"
  
    final val MODEL_CAMPFIRE = MODEL_LOCATION + "ModelCampfireDeco.png"
    final val SPRITE_FX = SPRITE_LOCATION + "SpriteFX.png"
    final val GUI_CAMPFIRE = GUI_LOCATION + "GuiCampfireDeco.png"
    final val GUI_CAMPINV_BACK = GUI_LOCATION + "GuiCampingBackpack.png"
    final val GUI_CAMPINV_TOOL = GUI_LOCATION + "GuiCampingTool.png"
    final val MC_INVENTORY = MC_GUI_LOCATION + "inventory.png"
    final val GUI_BAG = GUI_LOCATION + "GuiBackpack.png"
    final val GUI_KIT = GUI_LOCATION + "GuiKit.png"
    final val GUI_CAMPFIRE_COOK = GUI_LOCATION + "GuiCampfireCook.png"
    final val MODEL_GRILL = MODEL_LOCATION + "ModelGrill.png"
    final val MODEL_PAN = MODEL_LOCATION + "ModelPan.png"
    final val MODEL_LOG = MODEL_LOCATION + "ModelLog.png"
    final val MODEL_LOG2 = MODEL_LOCATION + "ModelLog2.png"
    final val SPRITE_POTION = SPRITE_LOCATION + "SpritePotion.png"
    final val MODEL_SPIT = MODEL_LOCATION + "ModelSpit.png"
    final val GUI_CAMPINV = GUI_LOCATION + "GuiCampinv.png"
    final val GUI_CAMPINV_CRAFT = GUI_LOCATION + "GuiCampinvCraft.png"
    final val RED_DOT = GUI_LOCATION + "GuiMapRed.png"
    final val BLUE_DOT = GUI_LOCATION + "GuiMapBlue.png"
    final val GUI_UTILS = GUI_LOCATION + "GuiUtils.png"
    final val GUI_TRAP = GUI_LOCATION + "GuiTrap.png"
    final val MODEL_SLEEPING_TOP = MODEL_LOCATION + "ModelSleepingBagTop.png"
    final val MODEL_SLEEPING_DOWN = MODEL_LOCATION + "ModelSleepingBagDown.png"
    final val MODEL_TENT_WHITE = MODEL_LOCATION + "ModelTentWhite.png"
    final val GUI_TENT = GUI_LOCATION + "GuiTent.png"
    final val GUI_TENT_CONTENDS_1 = GUI_LOCATION + "GuiTentContend1.png"
    final val GUI_TENT_CONTENDS_2 = GUI_LOCATION + "GuiTentContend2.png"
    final val ARMOR_FUR_LEG = MODEL_LOCATION + "ModelArmorFurLeg.png"
    final val ARMOR_FUR_MAIN = MODEL_LOCATION + "ModelArmorFurMain.png"
    final val MODEL_BEAR = MODEL_LOCATION + "ModelBear.png"
    final val MODEL_FOX = MODEL_LOCATION + "ModelArcticFox.png"
    final val MODEL_TRAP = MODEL_LOCATION + "ModelBearTrapOpen.png"
    final val MODEL_CAMPER_FEMALE = MODEL_LOCATION + "ModelCamperFemale.png"
    final val MODEL_CAMPER_MALE = MODEL_LOCATION + "ModelCamperMale.png"
  }
}

//  TODO BEFORE UPDATE
//
//  TENT LIGHTING CHANGE NOT WORK SMOOTLY

//CHANGELOG FOR THIS UPDATE SO FAR

//FIXED: CHANGING KEY DOESN'T WORK
//FIXED: SOME BUGS THAT CAUSED CRASHES
//IMPROVED SPAWN ALGRORITM FOR CAMAPSITES
//ADDED RUSSIAN LANGUAGE SUPPROT 
//ENCHANTED FEEL EN STABILITY OF THE LOGSEAT, THE SLEEPINGBAG, THE HEMP AND THE TENT
//PORTED TO MINECRAFT 1.8
//IMPROVED/FIXED SHIFT-CLICKING IN SOME INVENTORIES