package com.rikmuld.camping.common.objs.entity

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import org.lwjgl.input.Keyboard
import com.rikmuld.camping.core.Objs
import net.minecraft.client.settings.GameSettings
import com.sun.beans.decoder.TrueElementHandler
import com.rikmuld.camping.common.network.PlayerExitLog
import com.rikmuld.corerm.common.network.PacketSender

class EntityMountableBlock(world: World, var xFlag: Int, var yFlag: Int, var zFlag: Int) extends Entity(world) {
  var plX: Double = _
  var plY: Double = _
  var plZ: Double = _

  var player: EntityPlayer = _
  var update = -1;

  setSize(0.25F, 0.25F)
  setPosition(xFlag + 0.5F, yFlag + 0.1F, zFlag + 0.5F)

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
    if (worldObj.getBlock(xFlag, yFlag, zFlag) != Objs.log) setDead()
    if (riddenByEntity != null) {
      if (worldObj.isRemote && Minecraft.getMinecraft.gameSettings.keyBindSneak.isPressed && Minecraft.getMinecraft.inGameHasFocus) {
        riddenByEntity.asInstanceOf[EntityPlayer].mountEntity(null)
        PacketSender.toServer(new PlayerExitLog(xFlag, yFlag, zFlag))
        riddenByEntity = null
        player = null
      }
    } else if (player != null&&update==0) {
      player.mountEntity(this);
      update = -1;
    }
    
    if(update>0)update-=1;
  }

  protected override def readEntityFromNBT(tag: NBTTagCompound) {}
  protected override def writeEntityToNBT(tag: NBTTagCompound) {}
}
