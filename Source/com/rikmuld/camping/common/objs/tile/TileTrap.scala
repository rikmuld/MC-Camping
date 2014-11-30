package com.rikmuld.camping.common.objs.tile

import com.rikmuld.corerm.common.network.PacketSender
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.passive.EntityAnimal
import com.rikmuld.camping.core.ConfigInfo
import net.minecraft.potion.PotionEffect
import java.util.Random
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntitySpider
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.item.Item
import net.minecraft.entity.Entity
import com.rikmuld.camping.core.Objs
import scala.collection.JavaConversions._
import net.minecraft.init.Items
import net.minecraft.entity.EntityLiving
import com.rikmuld.camping.common.objs.entity.Bear
import java.util.UUID
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import com.rikmuld.corerm.common.objs.tile.TileEntityWithInventory
import com.rikmuld.corerm.common.objs.tile.TileEntityMain

object TileEntityTrap {
  val UUIDSpeedTrap = new UUID(new Random(242346763).nextLong, new Random(476456556).nextLong)
}

class TileEntityTrap extends TileEntityMain with TileEntityWithInventory {
  var trappedEntity: EntityLivingBase = _
  var open: Boolean = true
  var random: Random = new Random()
  var cooldown: Int = _
  var captureFlag: Boolean = _
  override def getSizeInventory(): Int = 1
  var monsterItemAttr = Array(Items.rotten_flesh, Items.chicken, Items.beef, Items.porkchop, Objs.venisonRaw)

  override def onInventoryChanged(slot: Int) {
    super[TileEntityWithInventory].onInventoryChanged(slot)
    if (!worldObj.isRemote) PacketSender.toClient(new com.rikmuld.camping.common.network.Items(0, xCoord, yCoord, zCoord, getStackInSlot(0)))
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    cooldown = tag.getInteger("cooldown")
    open = tag.getBoolean("open")
    captureFlag = tag.getBoolean("captureFlag")
  }
  override def setTileData(id: Int, data: Array[Int]) = if (id == 0) open = data(0) == 1
  override def updateEntity() {
    if (!worldObj.isRemote) {
      if (trappedEntity != null) {
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.getBoundingBox(xCoord + 0.21875F, yCoord, zCoord + 0.21875, xCoord + 0.78125, yCoord + 0.1875, zCoord + 0.78125)).asInstanceOf[java.util.List[EntityLivingBase]]
        if (!entities.contains(trappedEntity)) {
          trappedEntity = null
        }
      }
      if (cooldown > 0) cooldown = 0
      if ((trappedEntity != null) && open) {
        open = false
        sendTileData(0, true, if (open) 1 else 0)
      }
      if ((open || captureFlag) && (cooldown <= 0)) {
        captureFlag = false
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.getBoundingBox(xCoord + 0.21875F, yCoord, zCoord + 0.21875, xCoord + 0.78125, yCoord + 0.1875, zCoord + 0.78125)).asInstanceOf[java.util.List[EntityLivingBase]]
        if (entities.size > 0) {
          if ((entities.get(0).isInstanceOf[EntityPlayer]) && Objs.config.trapPlayer) {
            trappedEntity = entities.get(0)
          } else if (!(entities.get(0).isInstanceOf[EntityPlayer])) {
            trappedEntity = entities.get(0)
            if (getStackInSlot(0) != null) {
              getStackInSlot(0).stackSize -= 1
            }
          }
        }
        if (trappedEntity != null) {
          open = false
          sendTileData(0, true, if (open) 1 else 0)
        }
      }
      if (trappedEntity != null) {
        if (!(trappedEntity.isInstanceOf[EntityPlayer])) {
          trappedEntity.setPositionAndUpdate(xCoord + 0.5F, yCoord, zCoord + 0.5F)
        }
        trappedEntity.setInWeb()
        if (trappedEntity.isInstanceOf[EntityPlayer]) {
          trappedEntity.getEntityData().setInteger("isInTrap", 20)
          if (trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileEntityTrap.UUIDSpeedTrap) != null) trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileEntityTrap.UUIDSpeedTrap))
          trappedEntity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier(TileEntityTrap.UUIDSpeedTrap, "trap.speedNeg", -0.95f, 2))
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
        if (trappedEntity.isDead) {
          trappedEntity = null
        }
      }
      if ((getStackInSlot(0) != null) && (trappedEntity == null) && open) {
        val entities = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.getBoundingBox(xCoord - 20, yCoord - 10, zCoord - 20, xCoord + 20, yCoord + 10, zCoord + 20)).asInstanceOf[java.util.List[EntityLivingBase]]
        for (entity <- entities) {
          if (entity.isInstanceOf[EntityAnimal]) {
            if (entity.asInstanceOf[EntityAnimal].isBreedingItem(getStackInSlot(0))) {
              entity.asInstanceOf[EntityAnimal].getMoveHelper.setMoveTo(xCoord + 0.5F, yCoord, zCoord + 0.5F, 1)
            }
          }
          if (entity.isInstanceOf[Bear] || entity.isInstanceOf[EntityZombie] || entity.isInstanceOf[EntityCreeper] || entity.isInstanceOf[EntitySkeleton] || entity.isInstanceOf[EntityEnderman] || entity.isInstanceOf[EntitySpider]) {
            if (monsterItemAttr.contains(getStackInSlot(0).getItem())) {
              entity.asInstanceOf[EntityLiving].getMoveHelper.setMoveTo(xCoord + 0.5F, yCoord, zCoord + 0.5F, 1)
            }
          }
        }
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("cooldown", cooldown)
    tag.setBoolean("open", open)
    tag.setBoolean("captureFlag", trappedEntity != null)
  }
}
