package com.rikmuld.camping.core

import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class ProxyServer extends IGuiHandler {
  def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = null
  def getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = null

  def register = MiscRegistry.initServer
}

class ProxyClient extends ProxyServer {
  override def register = MiscRegistry.initClient
}