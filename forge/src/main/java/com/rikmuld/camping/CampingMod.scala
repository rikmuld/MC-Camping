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

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCIES, modLanguage = ModInfo.MOD_LANUAGE, guiFactory = ModInfo.MOD_GUIFACTORY)
object CampingMod {

  @SidedProxy(clientSide = ModInfo.MOD_CLIENT_PROXY, serverSide = ModInfo.MOD_SERVER_PROXY)
  var proxy: ProxyServer = null

  @EventHandler
  def Init(event: FMLInitializationEvent) {
    proxy.register
    MiscRegistry.init(event)
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
  }
}
// TEST MULTIPLAYER

// FASE 2 - Tents, Recipes so far
// FASE 3 - Campers, Animals, Traps, Armor

// FASE 4 - Guide, New tent: shelter, Backpack when on back Model, Lantern when active wearing model.