package com.rikmuld.camping.core

import scala.collection.mutable.HashMap
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.world.World
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.inventory.ContainerPlayer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiContainerCreative.ContainerCreative
import cpw.mods.fml.client.FMLClientHandler
import com.rikmuld.camping.client.render.fx.ColoredFlame

class ProxyServer extends IGuiHandler {
  val guis = HashMap[Integer, List[Class[Any]]]();

  def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = {
    if (id == GuiInfo.GUI_INVENTORY) new ContainerPlayer(player.inventory, false, player);
    else {
      try {
        guis(id).apply(0).getConstructor(classOf[EntityPlayer], classOf[IInventory]).newInstance(player, world.getTileEntity(x, y, z)).asInstanceOf[Container];
      } catch {
        case e: NoSuchMethodException => guis(id).apply(0).getConstructor(classOf[EntityPlayer]).newInstance(player).asInstanceOf[Container];
      }
    }

  }
  def getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = {
    if (id == GuiInfo.GUI_INVENTORY) if (player.capabilities.isCreativeMode) new GuiContainerCreative(player) else new GuiInventory(player)
    else {
      try {
        guis(id).apply(1).getConstructor(classOf[EntityPlayer], classOf[IInventory]).newInstance(player, world.getTileEntity(x, y, z)).asInstanceOf[Gui];
      } catch {
        case e: NoSuchMethodException => guis(id).apply(1).getConstructor(classOf[EntityPlayer]).newInstance(player).asInstanceOf[Gui];
      }
    }
  }
  def register = MiscRegistry.initServer
  def registerGui(id: Int, container: Class[Container], gui: Class[Gui]) = guis(id) = List(container.asInstanceOf[Class[Any]], gui.asInstanceOf[Class[Any]])
  def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) {}
}

class ProxyClient extends ProxyServer {
  override def register = MiscRegistry.initClient
  override def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) = FMLClientHandler.instance().getClient.effectRenderer.addEffect(new ColoredFlame(world, x, y, z, motionX, motionY, motionZ, color))
}