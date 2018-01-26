package com.rikmuld.camping.objs.tile

import java.util.{Random, UUID}

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.entity.Bear
import com.rikmuld.camping.objs.misc.ItemsData
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.tileentity.{RMTile, WithTileInventory}
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.entity.{EntityLiving, EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.monster._
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB

import scala.collection.JavaConversions._

object TileTrap {
  val UUIDSpeedTrap = new UUID(new Random(242346763).nextLong, new Random(476456556).nextLong)
}

class TileTrap extends RMTile with WithTileInventory with ITickable {
  var trappedEntity: EntityLivingBase = _
  var random: Random = new Random()
  var cooldown: Int = _
  var open: Boolean = true
  var captureFlag: Boolean = _
  var monsterItemAttr = Array(Items.ROTTEN_FLESH, Items.CHICKEN, Items.BEEF, Items.PORKCHOP, Objs.venisonRaw, Items.MUTTON, Items.RABBIT)
  var lastPlayer:Option[EntityPlayer] = None 
  
  override def getSizeInventory(): Int = 1
  override def onInventoryChanged(slot: Int) {
    super[WithTileInventory].onInventoryChanged(slot)
    if (!world.isRemote) PacketSender.toClient(new ItemsData(0, bd.x, bd.y, bd.z, getStackInSlot(0)))
  }
  override def readFromNBT(tag: NBTTagCompound) {
    cooldown = tag.getInteger("cooldown")
    captureFlag = tag.getBoolean("captureFlag")
    open = tag.getBoolean("open")
    super[WithTileInventory].readFromNBT(tag)
    super[RMTile].readFromNBT(tag)
  }
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setInteger("cooldown", cooldown)
    tag.setBoolean("captureFlag", trappedEntity != null)
    tag.setBoolean("open", open)
    super[WithTileInventory].writeToNBT(tag)
    super[RMTile].writeToNBT(tag)
  }
  def forceOpen {
    setOpen(true)
    cooldown = 5
    trappedEntity = null
  }
  
  override def setTileData(id: Int, data: Array[Int]) = if (id == 0) open = data(0) == 1
  def captureBounds = new AxisAlignedBB(bd.x + 0.21875, bd.y, bd.z + 0.21875, bd.x + 0.78125, bd.y + 0.1875, bd.z + 0.78125)
  def setOpen(open:Boolean) = {
    this.open = open
    sendTileData(0, true, if (this.open) 1 else 0)
  }
  override def update() {
    if (!world.isRemote) {
      if (world != null) {
        val entities = world.getEntitiesWithinAABB(classOf[EntityLivingBase], captureBounds).asInstanceOf[java.util.List[EntityLivingBase]]
        if (!entities.contains(trappedEntity)) trappedEntity = null
      }
      if (cooldown > 0) cooldown -= 1
      if ((trappedEntity != null) && open) setOpen(false)
      if ((open || captureFlag) && (cooldown <= 0)) {
        captureFlag = false
        val entities = world.getEntitiesWithinAABB(classOf[EntityLivingBase], captureBounds).asInstanceOf[java.util.List[EntityLivingBase]]
        if (entities.size > 0) {
          if ((entities.get(0).isInstanceOf[EntityPlayer]) && config.trapPlayer) {
            trappedEntity = entities.get(0)
          } else if (!(entities.get(0).isInstanceOf[EntityPlayer])) {
            trappedEntity = entities.get(0)
            if (!getStackInSlot(0).isEmpty) getStackInSlot(0).setCount(getStackInSlot(0).getCount - 1)
          }
        }
        if (trappedEntity != null) setOpen(false)
      }
      if (trappedEntity != null) {
        if (!(trappedEntity.isInstanceOf[EntityPlayer])) trappedEntity.setPositionAndUpdate(bd.x + 0.5F, bd.y, bd.z + 0.5F)
        trappedEntity.setInWeb()
        if (trappedEntity.isInstanceOf[EntityPlayer]) {
          trappedEntity.getEntityData().setInteger("isInTrap", 20)
          if (trappedEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap) != null) trappedEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(trappedEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap))
          trappedEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(TileTrap.UUIDSpeedTrap, "trap.speedNeg", -0.95f, 2))
        }
        if (random.nextInt(50) == 0) {
          if (!(trappedEntity.isInstanceOf[EntityPlayer]) ||
            !trappedEntity.asInstanceOf[EntityPlayer].capabilities.isCreativeMode) {
            trappedEntity.attackEntityFrom(Objs.bleedingSource, trappedEntity.getMaxHealth / 20F)
          }
          val effect = new PotionEffect(Objs.bleeding, 200, 1)
          effect.getCurativeItems.clear()
          trappedEntity.addPotionEffect(effect)
        }
        if (trappedEntity.isDead) trappedEntity = null
      }
      if (!getStackInSlot(0).isEmpty && (trappedEntity == null) && open) {
        val entities = world.getEntitiesWithinAABB(classOf[EntityLivingBase], new AxisAlignedBB(bd.x - 20, bd.y - 10, bd.z - 20, bd.x + 20, bd.y + 10, bd.z + 20)).asInstanceOf[java.util.List[EntityLivingBase]]
        for (entity <- entities) {
          if (entity.isInstanceOf[EntityAnimal]) {
            if (entity.asInstanceOf[EntityAnimal].isBreedingItem(getStackInSlot(0))) entity.asInstanceOf[EntityAnimal].getMoveHelper.setMoveTo(bd.x + 0.5F, bd.y, bd.z + 0.5F, 1)
          }
          if (entity.isInstanceOf[Bear] || entity.isInstanceOf[EntityZombie] || entity.isInstanceOf[EntityCreeper] || entity.isInstanceOf[EntitySkeleton] || entity.isInstanceOf[EntityEnderman] || entity.isInstanceOf[EntitySpider]) {
            if (monsterItemAttr.contains(getStackInSlot(0).getItem())) entity.asInstanceOf[EntityLiving].getMoveHelper.setMoveTo(bd.x + 0.5F, bd.y, bd.z + 0.5F, 1)
          }
        }
      }
    }
  }
}