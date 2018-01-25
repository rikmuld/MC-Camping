package com.rikmuld.camping

import com.rikmuld.camping.render.fx.ColouredFlame
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

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