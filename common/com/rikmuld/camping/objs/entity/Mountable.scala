package com.rikmuld.camping.objs.entity

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import org.lwjgl.input.Keyboard
import com.rikmuld.camping.objs.Objs
import net.minecraft.client.settings.GameSettings
import com.sun.beans.decoder.TrueElementHandler
import net.minecraft.util.math.BlockPos
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.camping.objs.misc.PlayerExitLog
import scala.collection.JavaConversions._
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand

class Mountable(world: World) extends Entity(world) {
  var pos:BlockPos = _

  setSize(0.25F, 0.25F)

  def setPos(pos:BlockPos){
    this.pos = pos
    setPosition(pos.getX + 0.5F, pos.getY + 0.1F, pos.getZ + 0.5F)
  }
  protected override def entityInit() {}
  def tryAddPlayer(player: EntityPlayer) {
    if (getPassengers.size() == 0) player.startRiding(this)
  }

  override def onUpdate() {
    super.onUpdate()
    if(pos!=null){
      if (world.getBlockState(pos).getBlock != Objs.logseat) setDead()
      if (this.getPassengers.size() > 0 && 
          worldObj.isRemote && Minecraft.getMinecraft.gameSettings.keyBindSneak.isPressed && 
          Minecraft.getMinecraft.inGameHasFocus) {
          PacketSender.toServer(new PlayerExitLog(pos.getX, pos.getY, pos.getZ))
          this.getPassengers.get(0).dismountRidingEntity()
      }
    }
  }

  protected override def readEntityFromNBT(tag: NBTTagCompound) {}
  protected override def writeEntityToNBT(tag: NBTTagCompound) {}
}