package com.rikmuld.camping.common.network

import net.minecraft.network.PacketBuffer
import net.minecraft.entity.player.EntityPlayer
import java.nio.ByteBuffer
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import com.rikmuld.camping.common.objs.tile.TileEntityMain

class TileData(var id:Int, var x: Int, var y: Int, var z: Int, tileData: Seq[Int]) extends BasicPacketData {
  var length: Int = if (tileData == null) 0 else tileData.length*4
  var data: Array[Byte] = {
    if (length > 0) {
      val dataHolder = new Array[Byte](length)
      for (i <- 0 until tileData.length) {
        val intData: Array[Byte] = ByteBuffer.allocate(4).putInt(tileData(i)).array
        for (j <- 0 until 4) dataHolder((i * 4) + j) = intData(j)
      }
      dataHolder
    } else null
  }
  
  def this() = this(0, 0, 0, 0, null)
  def setData(stream: PacketBuffer) {
    stream.writeInt(id)
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
    stream.writeInt(length)
    stream.writeBytes(data)
  }
  def getData(stream: PacketBuffer) {
    id = stream.readInt
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
    length = stream.readInt
    data = new Array[Byte](length)
    stream.readBytes(data)
  }
  def handlePacket(player: EntityPlayer, ctx: MessageContext) {
    val intData: Array[Int] = new Array[Int](length / 4)
    for (i <- 0 until intData.length) {
      val wrap = new Array[Byte](4)
      for (j <- 0 until 4) wrap(j) = data(j + (i * 4))
      intData(i) = ByteBuffer.wrap(wrap).getInt
    }
    if (player.worldObj.getTileEntity(x, y, z) != null) player.worldObj.getTileEntity(x, y, z).asInstanceOf[TileEntityMain].setTileData(id, intData)
  }
}