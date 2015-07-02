package com.rikmuld.camping

import net.minecraft.client.gui.Gui
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import scala.collection.mutable.HashMap
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.inventory.IInventory
import net.minecraft.util.BlockPos
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.client.Minecraft
import com.rikmuld.corerm.objs.PropType
import com.rikmuld.corerm.objs.Properties._
import com.rikmuld.corerm.objs.ObjInfo
import net.minecraft.item.Item
import net.minecraft.block.Block
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.common.MinecraftForge
import com.rikmuld.camping.render.fx.ColouredFlame

class ProxyServer {
  var eventsS:EventsS = _
  
  def registerEvents { 
    eventsS = new EventsS
    MinecraftForge.EVENT_BUS.register(eventsS)
    FMLCommonHandler.instance.bus.register(eventsS)
  }
  def getEventClient:Any = eventsS
  def getEventServer:EventsS = eventsS
  def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) {}
}

@SideOnly(Side.CLIENT)
class ProxyClient extends ProxyServer {
  var eventsC:EventsC = _

  override def registerEvents {
    super.registerEvents
    
    eventsC = new EventsC
    MinecraftForge.EVENT_BUS.register(eventsC)
    FMLCommonHandler.instance.bus.register(eventsC)
  }
  override def getEventClient:Any = eventsC
  override def spawnFlame(world: World, x: Double, y: Double, z: Double, motionX: Double, motionY: Double, motionZ: Double, color: Int) = FMLClientHandler.instance().getClient.effectRenderer.addEffect(new ColouredFlame(world, x, y, z, motionX, motionY, motionZ, color))
}