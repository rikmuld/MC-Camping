package com.rikmuld.camping.common.objs.entity

import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.ai.EntityAIAvoidEntity
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAILeapAtTarget
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.ai.EntityAITarget
import net.minecraft.util.DamageSource
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.passive.EntityOcelot
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.IEntityLivingData
import net.minecraft.item.ItemStack
import net.minecraft.entity.Entity
import net.minecraft.init.Items
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAIAttackOnCollide
import com.rikmuld.camping.core.AnimalPartInfo
import com.rikmuld.camping.core.Objs
import net.minecraft.entity.ai.EntityAITempt
import net.minecraft.entity.EnumCreatureAttribute

class Fox(world: World) extends EntityAnimal(world) {

  setSize(0.8F, 0.5F)
  getNavigator().setAvoidsWater(true)
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[EntityChicken], 1.0D, false))
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(4, new EntityAIAvoidEntity(this, classOf[EntityPlayer], 16.0F, 0.8D, 1.33D))
  tasks.addTask(6, new EntityAITempt(this, 1.2D, Items.chicken, false))
  tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F))
  tasks.addTask(10, new EntityAIWander(this, 0.8D))
  tasks.addTask(11, new EntityAIWatchClosest(this, classOf[EntityPlayer], 10.0F))
  targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityChicken], 750, false))

  override def updateAITick() {
    if (this.getMoveHelper().isUpdating()) {
      val d0 = this.getMoveHelper().getSpeed()
      if (d0 == 1.33D) this.setSprinting(true)
      else {
        this.setSneaking(false)
        this.setSprinting(false)
      }
    } else {
      this.setSneaking(false)
      this.setSprinting(false)
    }
  }
  override protected def canDespawn(): Boolean = false
  override def isAIEnabled(): Boolean = true
  override protected def applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
  }
  override def attackEntityAsMob(entity: Entity): Boolean = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F)
  override protected def dropFewItems(playerAttack: Boolean, loot: Int) {
    var dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    var drops: Int = 0
    while (drops < (dropChance)) {
      entityDropItem(new ItemStack(Objs.animalParts, 1, AnimalPartInfo.FUR_WHITE), 0)
      drops += 1
    }
    dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    drops = 0
    while (drops < dropChance) {
      if (isBurning) entityDropItem(new ItemStack(Objs.venisonCooked), 0)
      else entityDropItem(new ItemStack(Objs.venisonRaw), 0)
      drops += 1
    }
  }
  override def createChild(entity: EntityAgeable): EntityAgeable = new Fox(this.worldObj)
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.chicken
}