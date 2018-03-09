package com.rikmuld.camping.entity

import com.rikmuld.camping.misc.PlayerExitLog
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class Mountable(worldIn: World) extends Entity(worldIn) {
  var pos: BlockPos =
    _

  var checkDeath: Int =
    0

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
      if (world.getBlockState(pos).getBlock != ObjRegistry.logSeat)
        setDead()//TODO put better in logseat code

      if (this.getPassengers.size() > 0 && world.isRemote && Minecraft.getMinecraft.gameSettings.keyBindSneak.isPressed && Minecraft.getMinecraft.inGameHasFocus) {
        PacketSender.sendToServer(new PlayerExitLog(pos.getX, pos.getY, pos.getZ))
        this.getPassengers.get(0).dismountRidingEntity()
      }
    }
  }

  protected override def readEntityFromNBT(tag: NBTTagCompound) {}
  protected override def writeEntityToNBT(tag: NBTTagCompound) {}
}