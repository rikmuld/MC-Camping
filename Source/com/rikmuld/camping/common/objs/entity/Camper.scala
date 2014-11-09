package com.rikmuld.camping.common.objs.entity

import net.minecraft.entity.EntityCreature
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIAttackOnCollide
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraft.entity.EntityLivingBase
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper
import net.minecraft.util.DamageSource
import net.minecraft.entity.ai.EntityAIWatchClosest2

class Camper(world:World) extends EntityCreature(world) {
  setGender(rand.nextInt(2))
  setSize(0.6F, 1.8F)
  getNavigator().setAvoidsWater(true)
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(2, new EntityAIAttackOnCollide(this, classOf[Bear], 1.0F, false))
  tasks.addTask(3, new EntityAIAttackOnCollide(this, classOf[EntityMob], 1.0F, false))
  tasks.addTask(4, new EntityAIWatchClosest2(this, classOf[EntityPlayer], 3.0F, 1.0F))
  tasks.addTask(6, new EntityAIWander(this, 0.6D))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[EntityMob], 0, false))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[Bear], 0, false))
    
  def this(world:World, x:Double, y:Double, z:Double) {
    this(world)
    setPosition(x, y, z)
  } 
  def setGender(gender:Int) = dataWatcher.updateObject(16, Integer.valueOf(gender))
  def getGender:Int = dataWatcher.getWatchableObjectInt(16)
  override def writeEntityToNBT(tag:NBTTagCompound){
    super.writeEntityToNBT(tag)
    tag.setInteger("gender", getGender)
  }
  override def readEntityFromNBT(tag:NBTTagCompound){
    super.writeEntityToNBT(tag)
    setGender(tag.getInteger("gender"))
  }
  protected override def applyEntityAttributes {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D)
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D)
  }
  override def canDespawn = false
  override def entityInit {
    super.entityInit
    dataWatcher.addObject(16, Integer.valueOf(0))
  }
  protected override def getDeathSound = "mob.villager.death"
  protected override def getHurtSound = "mob.villager.hit"
  protected override def getLivingSound = "mob.villager.idle"
  override def isAIEnabled = true
  override def attackEntityAsMob(entity: Entity): Boolean = {
    var f = this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue.toFloat
    var i = 0
    if (entity.isInstanceOf[EntityLivingBase]) {
      f += EnchantmentHelper.getEnchantmentModifierLiving(this, entity.asInstanceOf[EntityLivingBase])
      i += EnchantmentHelper.getKnockbackModifier(this, entity.asInstanceOf[EntityLivingBase])
    }
    val flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f)
    if (flag) {
      if (i > 0) {
        entity.addVelocity((-MathHelper.sin(this.rotationYaw * Math.PI.toFloat / 180.0F) * i.toFloat * 0.5F).toDouble, 0.1D, (MathHelper.cos(this.rotationYaw * Math.PI.toFloat / 180.0F) * i.toFloat * 0.5F).toDouble)
        this.motionX *= 0.6D
        this.motionZ *= 0.6D
      }
      val j = EnchantmentHelper.getFireAspectModifier(this)
      if (j > 0) entity.setFire(j * 4)
      if (entity.isInstanceOf[EntityLivingBase]) EnchantmentHelper.func_151384_a(entity.asInstanceOf[EntityLivingBase], this)
      EnchantmentHelper.func_151385_b(this, entity)
    }
    flag
  }
  override protected def attackEntity(entity: Entity, num: Float) {
    if (this.attackTime <= 0 && num < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
      this.attackTime = 20
      this.attackEntityAsMob(entity)
    }
  }
}