package com.rikmuld.camping.objs.tile

import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.entity.EntityLivingBase
import com.rikmuld.corerm.objs.WithTileInventory
import java.util.UUID
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import net.minecraft.init.Items
import java.util.Random
import com.rikmuld.corerm.objs.WithTileInventory
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import com.rikmuld.camping.objs.misc.ItemsData
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.camping.CampingMod._
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntitySpider
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.monster.EntityZombie
import scala.collection.JavaConversions._
import com.rikmuld.camping.objs.entity.Bear
import net.minecraft.server.gui.IUpdatePlayerListBox
import com.rikmuld.camping.objs.block.Trap

object TileTrap {
  val UUIDSpeedTrap = new UUID(new Random(242346763).nextLong, new Random(476456556).nextLong)
}

class TileTrap extends RMTile with WithTileInventory with IUpdatePlayerListBox {
  var trappedEntity: EntityLivingBase = _
  var random: Random = new Random()
  var cooldown: Int = _
  var open: Boolean = true
  var captureFlag: Boolean = _
  var monsterItemAttr = Array(Items.rotten_flesh, Items.chicken, Items.beef, Items.porkchop, Objs.venisonRaw)

  override def getSizeInventory(): Int = 1
  override def onInventoryChanged(slot: Int) {
    super[WithTileInventory].onInventoryChanged(slot)
    if (!worldObj.isRemote) PacketSender.toClient(new ItemsData(0, bd.x, bd.y, bd.z, getStackInSlot(0)))
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    cooldown = tag.getInteger("cooldown")
    captureFlag = tag.getBoolean("captureFlag")
    open = tag.getBoolean("open")
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("cooldown", cooldown)
    tag.setBoolean("captureFlag", trappedEntity != null)
    tag.setBoolean("open", open)
  }
  def forceOpen {
    setOpen(true)
    cooldown = 5
    trappedEntity = null
  }
  override def setTileData(id: Int, data: Array[Int]) = if (id == 0) open = data(0) == 1
  def captureBounds = AxisAlignedBB.fromBounds(bd.x + 0.21875, bd.y, bd.z + 0.21875, bd.x + 0.78125, bd.y + 0.1875, bd.z + 0.78125)
  def setOpen(open:Boolean) = {
    this.open = open
    sendTileData(0, true, if (this.open) 1 else 0)
  }
  override def update() {
    if (!worldObj.isRemote) {
      if (trappedEntity != null) {
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], captureBounds).asInstanceOf[java.util.List[EntityLivingBase]]
        if (!entities.contains(trappedEntity)) trappedEntity = null
      }
      if (cooldown > 0) cooldown -= 1
      if ((trappedEntity != null) && open) setOpen(false)
      if ((open || captureFlag) && (cooldown <= 0)) {
        captureFlag = false
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], captureBounds).asInstanceOf[java.util.List[EntityLivingBase]]
        if (entities.size > 0) {
          if ((entities.get(0).isInstanceOf[EntityPlayer]) && config.trapPlayer) {
            trappedEntity = entities.get(0)
          } else if (!(entities.get(0).isInstanceOf[EntityPlayer])) {
            trappedEntity = entities.get(0)
            if (getStackInSlot(0) != null) getStackInSlot(0).stackSize -= 1
          }
        }
        if (trappedEntity != null) setOpen(false)
      }
      if (trappedEntity != null) {
        if (!(trappedEntity.isInstanceOf[EntityPlayer])) trappedEntity.setPositionAndUpdate(bd.x + 0.5F, bd.y, bd.z + 0.5F)
        trappedEntity.setInWeb()
        if (trappedEntity.isInstanceOf[EntityPlayer]) {
          trappedEntity.getEntityData().setInteger("isInTrap", 20)
          if (trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileTrap.UUIDSpeedTrap) != null) trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileTrap.UUIDSpeedTrap))
          trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier(TileTrap.UUIDSpeedTrap, "trap.speedNeg", -0.95f, 2))
        }
        if (random.nextInt(50) == 0) {
          if (!(trappedEntity.isInstanceOf[EntityPlayer]) ||
            !trappedEntity.asInstanceOf[EntityPlayer].capabilities.isCreativeMode) {
            trappedEntity.attackEntityFrom(Objs.bleedingSource, trappedEntity.getMaxHealth / 20F)
          }
          val effect = new PotionEffect(Objs.bleeding.id, 200, 1)
          effect.getCurativeItems.clear()
          trappedEntity.addPotionEffect(effect)
        }
        if (trappedEntity.isDead) trappedEntity = null
      }
      if ((getStackInSlot(0) != null) && (trappedEntity == null) && open) {
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.fromBounds(bd.x - 20, bd.y - 10, bd.z - 20, bd.x + 20, bd.y + 10, bd.z + 20)).asInstanceOf[java.util.List[EntityLivingBase]]
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