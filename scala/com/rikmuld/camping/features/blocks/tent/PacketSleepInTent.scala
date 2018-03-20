package com.rikmuld.camping.features.blocks.tent

import com.rikmuld.corerm.network.packets.PacketBasic
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketSleepInTent(var x: Int, var y: Int, var z: Int) extends PacketBasic {
  def this() = this(0, 0, 0)

  override def handlePacket(player: EntityPlayer, ctx: MessageContext): Unit =
    player.world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityTent].sleep(player)

  override def read(stream: PacketBuffer): Unit = {
    x = stream.readInt
    y = stream.readInt
    z = stream.readInt
  }

  override def write(stream: PacketBuffer): Unit = {
    stream.writeInt(x)
    stream.writeInt(y)
    stream.writeInt(z)
  }
}