package com.rikmuld.camping

import com.rikmuld.camping.core.MiscRegistry
import com.rikmuld.camping.core.ObjRegistry
import com.rikmuld.camping.core.ProxyServer
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import com.rikmuld.camping.core.ModInfo
import scala.runtime.ObjectRef
import com.rikmuld.camping.core.ObjRegistry

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCIES, modLanguage = ModInfo.MOD_LANUAGE, guiFactory = ModInfo.MOD_GUIFACTORY)
object CampingMod {

  @SidedProxy(clientSide = ModInfo.MOD_CLIENT_PROXY, serverSide = ModInfo.MOD_SERVER_PROXY)
  var proxy: ProxyServer = null

  @EventHandler
  def Init(event: FMLInitializationEvent) {
    proxy.register
    MiscRegistry.init(event)
    ObjRegistry.init
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
  }
  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    MiscRegistry.preInit(event)
    ObjRegistry.preInit
  }
  @EventHandler
  def PosInit(event: FMLPostInitializationEvent) {
    MiscRegistry.postInit(event)
    ObjRegistry.postInit
  }
}
//Add backpack/lantern model when wearing
//Add shelter
//More advanced campers
//Add Guide
//
//TAB ->

/*
 * Button options
 */
//
//  def intersectX(part:ModelPart, y:Float, z:Float):Boolean = part.cubeList.exists(box => interX(getPartBoxCoordinates(part, box), y, z))
//  def intersectY(part:ModelPart, x:Float, z:Float):Boolean = part.cubeList.exists(box => interY(getPartBoxCoordinates(part, box), x, z))
//  def intersectZ(part:ModelPart, x:Float, y:Float):Boolean = part.cubeList.exists(box => interZ(getPartBoxCoordinates(part, box), x, y))
//  def interX(coords, y, z) = isInBetween(y, coords._1.yCoord, coords._2.yCoord)&&isInBetween(z, coords._1.zCoord, coords._2.zCoord)
//  def interY(coords, x, z) = isInBetween(x, coords._1.xCoord, coords._2.xCoord)&&isInBetween(z, coords._1.zCoord, coords._2.zCoord)
//  def interZ(coords, x, y) = isInBetween(x, coords._1.xCoord, coords._2.xCoord)&7isInBetween(y, coords._1.yCoord, coords._2.yCoord)