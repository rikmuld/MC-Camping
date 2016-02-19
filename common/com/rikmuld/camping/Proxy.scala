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

object Solution {
    def main(args: Array[String]) = { 
        val pos = Console.readLine
        val board = new Array[String](5)
        for (i <- 0 until 5) {
            board.update(i, Console.readLine)
        }
    nextMove(pos, board)
    }

  def nextMove(pos:String, board:Array[String])={
    var dist = Integer.MAX_VALUE
    val bot = pos.split(' ')
    val posi = (bot(0).toInt, bot(1).toInt)
    var coords = (0, 0)

    for(row <- 0 until board.length){
      for(column <- 0 until row){
        if(board(row)(column) == 'd'){
          val nwDist = Math.abs(posi._2 - row) + Math.abs(posi._1 - column)
          if(nwDist < dist){
            dist = nwDist
            coords = (column, row)
          }
        }
      }
    }
    
    if(posi._1<coords._1)println("RIGHT")
    else if(posi._1>coords._1)println("LEFT")
    else if(posi._2<coords._2)println("DOWN")
    else if(posi._2>coords._2)println("UP")
  }
}
