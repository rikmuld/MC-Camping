package com.rikmuld.camping.objs.misc

import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.objs.WithTileInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.network.BasicPacketData
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraft.util.BlockPos
import com.rikmuld.camping.CampingMod
import com.rikmuld.corerm.RMMod
import com.rikmuld.camping.objs.tile.TileLogseat
import com.rikmuld.camping.objs.tile.TileTent

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
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = player.openGui(RMMod, id, player.worldObj, x, y, z)
}

class NBTPlayer(var tag: NBTTagCompound) extends BasicPacketData {
  def this() = this(null);
  override def setData(stream: PacketBuffer) = stream.writeNBTTagCompoundToBuffer(tag)
  override def getData(stream: PacketBuffer) = tag = stream.readNBTTagCompoundFromBuffer()
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = if(player!=null)player.getEntityData.setTag(NBTInfo.INV_CAMPING, tag)
}

class MapData(var scale: Int, var x: Int, var z: Int, var colours: Array[Byte]) extends BasicPacketData {
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
    if (eventsC.map != null) {
      eventsC.map.colorData(player) = colours
      eventsC.map.posData(player) = Array(scale, x, z)
    }
  }
}

class ItemsData(var slot: Int, var x: Int, var y: Int, var z: Int, var stack: ItemStack) extends BasicPacketData {
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
    if (player.worldObj.getTileEntity(new BlockPos(x, y, z)) != null) {
      player.worldObj.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[WithTileInventory].setInventorySlotContents(slot, stack)
    }
  }
}

class PlayerSleepInTent(var x: Int, var y: Int, var z: Int) extends BasicPacketData {
  def this() = this(0, 0, 0)

  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = player.worldObj.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileTent].sleep(player)
  override def getData(stream: PacketBuffer) {
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
  }
  override def setData(stream: PacketBuffer) {
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
  }
}

class PlayerExitLog(var x: Int, var y: Int, var z: Int) extends BasicPacketData {
  def this() = this(0, 0, 0)
 
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) {
    if(!player.worldObj.isRemote&&player.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock==Objs.logseat&&player.worldObj.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileLogseat].mountable.riddenByEntity!=null){
      player.worldObj.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileLogseat].mountable.riddenByEntity.mountEntity(null) 
      player.worldObj.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileLogseat].mountable.player = null;
    }
  }
  override def getData(stream: PacketBuffer) {
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
  }
  override def setData(stream: PacketBuffer) {
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
  }
}

class KeyData(var id: Int) extends BasicPacketData {
  def this() = this(0)
  override def setData(stream: PacketBuffer) = stream.writeInt(id)
  override def getData(stream: PacketBuffer) = id = stream.readInt
  override def handlePacket(player: EntityPlayer, ctx: MessageContext) = eventsS.keyPressedServer(player, id)
}