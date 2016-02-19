package com.rikmuld.camping.objs.entity

import org.lwjgl.opengl.GL11
import com.google.common.base.Predicate
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIAttackOnCollide
import net.minecraft.entity.ai.EntityAIAvoidEntity
import net.minecraft.entity.ai.EntityAILeapAtTarget
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAITempt
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.CampingMod

class Fox(world: World) extends EntityAnimal(world) {

  setSize(0.8F, 0.5F)
  getNavigator.asInstanceOf[PathNavigateGround].func_179688_b(true)
  getNavigator.asInstanceOf[PathNavigateGround].func_179690_a(true)
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[EntityChicken], 1.0D, false))
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(5, new EntityAITempt(this, 1.2D, Items.chicken, false))
  tasks.addTask(5, new EntityAIAvoidEntity(this, new Predicate[Object]() {
      override def apply(entity:Object):Boolean = entity.isInstanceOf[EntityPlayer]
  }, 16.0F, 0.8D, 1.33D))
  tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F))
  tasks.addTask(10, new EntityAIWander(this, 0.8D))
  tasks.addTask(11, new EntityAIWatchClosest(this, classOf[EntityPlayer], 10.0F))
  targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityChicken], false))

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
  override protected def applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D)
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D)
  }
  override def onUpdate() {
    super.onUpdate()
    if (!worldObj.isRemote && !CampingMod.config.useFoxes) setDead()
  }
  override def attackEntityAsMob(entity: Entity): Boolean = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F)
  override protected def dropFewItems(playerAttack: Boolean, loot: Int) {
    var dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    var drops: Int = 0
    while (drops < (dropChance)) {
      entityDropItem(nwsk(animalParts, PartsAnimal.FUR_WHITE), 0)
      drops += 1
    }
    dropChance = rand.nextInt(3) + rand.nextInt(1 + loot)
    drops = 0
    while (drops < dropChance) {
      if (isBurning) entityDropItem(nwsk(venisonCooked), 0)
      else entityDropItem(nwsk(venisonRaw), 0)
      drops += 1
    }
  }
  override def createChild(entity: EntityAgeable): EntityAgeable = new Fox(this.worldObj)
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.chicken
  override def getLivingSound = MOD_ID + ":mob.fox.say"
  override def getHurtSound = MOD_ID + ":mob.fox.dead"
  override def getDeathSound = MOD_ID + ":mob.fox.dead"
}

@SideOnly(Side.CLIENT)
class FoxRenderer(model: ModelBase) extends RenderLiving(Minecraft.getMinecraft.getRenderManager, model, .4f) {
  override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity.asInstanceOf[EntityLiving], d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(par1Entity: Entity): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_FOX)
}