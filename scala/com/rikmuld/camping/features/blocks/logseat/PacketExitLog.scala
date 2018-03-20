package com.rikmuld.camping.features.blocks.logseat

import com.rikmuld.corerm.network.packets.PacketBasic
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketExitLog(var x: Int, var y: Int, var z: Int) extends PacketBasic {
  def this() =
    this(0, 0, 0)

  override def handlePacket(player: EntityPlayer, ctx: MessageContext): Unit =
    if(player.getRidingEntity.isInstanceOf[EntityMountable])
      player.dismountRidingEntity()

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
