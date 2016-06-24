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

class Mountable(world: World) extends Entity(world) {
  var plX: Double = _
  var plY: Double = _
  var plZ: Double = _
  
  var pos:BlockPos = _

  var player: EntityPlayer = _
  var update = -1;

  setSize(0.25F, 0.25F)

  def setPos(pos:BlockPos){
    this.pos = pos
    setPosition(pos.getX + 0.5F, pos.getY + 0.1F, pos.getZ + 0.5F)
  }
  protected override def entityInit() {}
  override def interactFirst(player: EntityPlayer): Boolean = {
    if (riddenByEntity != null && riddenByEntity.isInstanceOf[EntityPlayer] && this.riddenByEntity != player) return true
    else {
      if (!world.isRemote) {
        player.mountEntity(this)
      } else {
        this.player = player
        this.update = 5;
      }
    }
    true
  }
  override def onUpdate() {
    super.onUpdate()
    if(pos!=null){
      if (world.getBlockState(pos).getBlock != Objs.logseat) setDead()
      if (riddenByEntity != null) {
        if (worldObj.isRemote && Minecraft.getMinecraft.gameSettings.keyBindSneak.isPressed && Minecraft.getMinecraft.inGameHasFocus) {
          riddenByEntity.asInstanceOf[EntityPlayer].mountEntity(null)
          PacketSender.toServer(new PlayerExitLog(pos.getX, pos.getY, pos.getZ))
          riddenByEntity = null
          player = null
        }
      } else if (player != null&&update==0) {
        player.mountEntity(this);
        update = -1;
      }
      
      if(update>0)update-=1;
    }
  }

  protected override def readEntityFromNBT(tag: NBTTagCompound) {}
  protected override def writeEntityToNBT(tag: NBTTagCompound) {}
}