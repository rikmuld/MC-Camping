package com.rikmuld.camping.features.inventory

import com.rikmuld.camping.Library.NBTInfo
import com.rikmuld.corerm.network.packets.PacketBasic
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class PacketNBTPlayer(var tag: NBTTagCompound) extends PacketBasic {
  def this() =
    this(null)

  override def write(stream: PacketBuffer): Unit =
    stream.writeCompoundTag(tag)

  override def read(stream: PacketBuffer): Unit =
    tag = stream.readCompoundTag()

  override def handlePacket(player: EntityPlayer, ctx: MessageContext): Unit =
    Option(player).foreach(_.getEntityData.setTag(NBTInfo.INV_CAMPING, tag))
}
