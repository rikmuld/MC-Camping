package com.rikmuld.camping.features.entities.fox

import com.google.common.base.Predicate
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.PartsAnimal
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.registers.Registry._
import net.minecraft.entity.ai._
import net.minecraft.entity.passive.{EntityAnimal, EntityChicken}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityAgeable, EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.util.{DamageSource, SoundEvent}
import net.minecraft.world.World

class EntityFox(worldIn: World) extends EntityAnimal(worldIn) {

  setSize(0.8F, 0.5F)
  getNavigator.asInstanceOf[PathNavigateGround].setCanSwim(true)
  tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false))
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(5, new EntityAITempt(this, 1.2D, Items.CHICKEN, false))
  tasks.addTask(5, new EntityAIAvoidEntity(this, classOf[EntityPlayer].asInstanceOf[Class[EntityLivingBase]], new Predicate[EntityLivingBase]() {
      override def apply(entity:EntityLivingBase):Boolean = entity.isInstanceOf[EntityPlayer]
  }, 16.0F, 0.8D, 1.33D))
  tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F))
  tasks.addTask(10, new EntityAIWander(this, 0.8D))
  tasks.addTask(11, new EntityAIWatchClosest(this, classOf[EntityPlayer], 10.0F))
  targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityChicken], false))

  override def updateAITasks() {
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
  override protected def applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D)
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D)
  }
  override def onUpdate() {
    super.onUpdate()
    if (!world.isRemote && !CampingMod.config.useFoxes) setDead()
  }
  override def attackEntityAsMob(entity: Entity): Boolean = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F)
  override protected def dropFewItems(playerAttack: Boolean, loot: Int) {
    var dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    var drops: Int = 0
    while (drops < (dropChance)) {
      entityDropItem(new ItemStack(ObjRegistry.animalParts, 1, PartsAnimal.FUR_WHITE), 0)
      drops += 1
    }
    dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    drops = 0
    while (drops < dropChance) {
      if (isBurning) entityDropItem(new ItemStack(ObjRegistry.venisonCooked), 0)
      else entityDropItem(new ItemStack(ObjRegistry.venisonRaw), 0)
      drops += 1
    }
  }
  override def createChild(entity: EntityAgeable): EntityAgeable = new EntityFox(this.world)
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.CHICKEN
  override def getAmbientSound:SoundEvent = foxAmb
  override def getHurtSound(source: DamageSource):SoundEvent = foxDeath
  override def getDeathSound:SoundEvent = foxDeath
}