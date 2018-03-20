package com.rikmuld.camping.features.general.keys

import com.rikmuld.corerm.network.packets.PacketBasic
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketKeyData(var id: Int) extends PacketBasic {
  def this() =
    this(0)

  override def write(stream: PacketBuffer): Unit =
    stream.writeInt(id)

  override def read(stream: PacketBuffer): Unit =
    id = stream.readInt

  override def handlePacket(player: EntityPlayer, ctx: MessageContext): Unit =
    EventsServer.keyPressed(player, id)
}