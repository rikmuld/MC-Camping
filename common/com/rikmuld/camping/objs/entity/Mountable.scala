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

//TODO REDO, SYSTEM IMPROVED SO IMPROVE
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
  override def processInitialInteract(player: EntityPlayer, stack:ItemStack, hand:EnumHand): Boolean = {
    val pass = this.getPassengers
    this.getRidingEntity
    if (pass.size() > 0 && pass.find(entity => entity.isInstanceOf[EntityPlayer]).isDefined && !pass.contains(player)) return true
    else {
      if (!world.isRemote) {
        this.addPassenger(player)
      } else {
        this.player = player
        this.update = 5;
      }
    }
    true
  }
  def removePassenger2(entity:Entity) {
    this.removePassenger(entity)
  }
  override def onUpdate() {
    super.onUpdate()
    if(pos!=null){
      if (world.getBlockState(pos).getBlock != Objs.logseat) setDead()
      if (this.getPassengers.size() > 0) {
        if (worldObj.isRemote && Minecraft.getMinecraft.gameSettings.keyBindSneak.isPressed && Minecraft.getMinecraft.inGameHasFocus) {
          PacketSender.toServer(new PlayerExitLog(pos.getX, pos.getY, pos.getZ))
          this.removePassenger(player)
          player = null
        }
      } else if (player != null&&update==0) {
        this.addPassenger(player)
        update = -1;
      }
      
      if(update>0)update-=1;
    }
  }

  protected override def readEntityFromNBT(tag: NBTTagCompound) {}
  protected override def writeEntityToNBT(tag: NBTTagCompound) {}
}