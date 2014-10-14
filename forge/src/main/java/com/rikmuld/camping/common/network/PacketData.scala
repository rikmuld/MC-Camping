package com.rikmuld.camping.common.network

import java.nio.ByteBuffer

import scala.collection.JavaConversions._

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.common.objs.tile.TileEntityMain
import com.rikmuld.camping.common.objs.tile.TileEntityWithInventory
import com.rikmuld.camping.core.Events
import com.rikmuld.camping.core.NBTInfo
import com.rikmuld.camping.core.Objs

import cpw.mods.fml.common.network.simpleimpl.MessageContext
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer

class TileData(var id: Int, var x: Int, var y: Int, var z: Int, tileData: Seq[Int]) extends BasicPacketData {
  var length: Int = if (tileData == null) 0 else tileData.length * 4
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

class OpenGui(var id: Int) extends BasicPacketData {
  var x: Int = 0
  var y: Int = 0
  var z: Int = 0

  def this() = this(0);
  def this(id: Int, x: Int, y: Int, z: Int) {
    this(id)
    this.x = x
    this.y = y
    this.z = z
  }
  override def setData(stream: PacketBuffer) {
    stream.writeInt(id)
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
  }
  override def getData(stream: PacketBuffer) {
    id = stream.readInt
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
  }
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = player.openGui(CampingMod, id, player.worldObj, x, y, z)
}

class NBTPlayer(var tag: NBTTagCompound) extends BasicPacketData {
  def this() = this(null);
  override def setData(stream: PacketBuffer) = stream.writeNBTTagCompoundToBuffer(tag)
  override def getData(stream: PacketBuffer) = tag = stream.readNBTTagCompoundFromBuffer()
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = player.getEntityData.setTag(NBTInfo.INV_CAMPING, tag)
}

class Map(var scale: Int, var x: Int, var z: Int, var colours: Array[Byte]) extends BasicPacketData {
  def this() = this(0, 0, 0, null)
  override def setData(stream: PacketBuffer) {
    stream.writeInt(x)
    stream.writeInt(z)
    stream.writeInt(scale)
    stream.writeBytes(colours)
  }
  override def getData(stream: PacketBuffer) {
    x = stream.readInt()
    z = stream.readInt()
    scale = stream.readInt()
    colours = Array.ofDim[Byte](16384)
    stream.readBytes(colours)
  }
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) {
    if (Objs.eventsClient.map != null) {
      Objs.eventsClient.map.colorData(player) = colours
      Objs.eventsClient.map.posData(player) = Array(scale, x, z)
    }
  }
}

class Items(var slot: Int, var x: Int, var y: Int, var z: Int, var stack: ItemStack) extends BasicPacketData {
  def this() = this(0, 0, 0, 0, null)
  override def setData(stream: PacketBuffer) {
    stream.writeInt(slot)
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
    stream.writeItemStackToBuffer(stack)
  }
  override def getData(stream: PacketBuffer) {
    slot = stream.readInt
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
    stack = stream.readItemStackFromBuffer()
  }
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) {
    println(player.worldObj.isRemote, x, y, z)
    if (player.worldObj.getTileEntity(x, y, z) != null) {
      println(player.worldObj.isRemote, slot, stack)
      player.worldObj.getTileEntity(x, y, z).asInstanceOf[TileEntityWithInventory].setInventorySlotContents(slot, stack)
    }
  }
}
