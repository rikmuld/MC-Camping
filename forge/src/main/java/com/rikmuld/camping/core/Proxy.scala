package com.rikmuld.camping.core

import scala.collection.mutable.HashMap

import com.rikmuld.camping.client.render.fx.ColoredFlame

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.common.network.IGuiHandler
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.ContainerPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.world.World

class ProxyServer extends IGuiHandler {
  val guis = HashMap[Integer, List[Class[_]]]();

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
  @SideOnly(Side.CLIENT)
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
  def registerGui(id: Int, container: Class[_], gui: Class[_]) = guis(id) = List(container, gui)
  def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) {}
}

@SideOnly(Side.CLIENT)
class ProxyClient extends ProxyServer {
  override def register = MiscRegistry.initClient
  override def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) = FMLClientHandler.instance().getClient.effectRenderer.addEffect(new ColoredFlame(world, x, y, z, motionX, motionY, motionZ, color))
}