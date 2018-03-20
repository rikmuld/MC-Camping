package com.rikmuld.camping.features.inventory

import com.rikmuld.camping.features.inventory.GuiMap.MapData
import com.rikmuld.corerm.network.packets.PacketBasic
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketMapData(var scale: Int, var centerX: Int, var centerZ: Int, var colours: Array[Byte]) extends PacketBasic {
  def this() =
    this(0, 0, 0, Array())

  override def write(stream: PacketBuffer): Unit = {
    stream.writeBoolean(colours.length == 16384)
    stream.writeInt(centerX)
    stream.writeInt(centerZ)
    stream.writeInt(scale)
    stream.writeBytes(colours)
  }

  override def read(stream: PacketBuffer): Unit =
    if (stream.readBoolean) {
      centerX = stream.readInt()
      centerZ = stream.readInt()
      scale = stream.readInt()
      colours = Array.ofDim[Byte](16384)
      stream.readBytes(colours)
    }

  override def handlePacket(player: EntityPlayer, ctx: MessageContext): Unit = {
    EventsClient.updateMap = true
    EventsClient.mapData = MapData(centerX, centerZ, scale, colours)
  }
}
