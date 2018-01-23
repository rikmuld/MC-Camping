package com.rikmuld.camping.objs.entity

import com.google.common.base.Predicate
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.render.models.FoxModel
import com.rikmuld.corerm.utils.CoreUtils._
import net.minecraft.client.renderer.entity.{RenderLiving, RenderManager}
import net.minecraft.entity.{Entity, EntityAgeable, EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.entity.ai._
import net.minecraft.entity.passive.{EntityAnimal, EntityChicken}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.util.{DamageSource, ResourceLocation, SoundEvent}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11

class Fox(worldIn: World) extends EntityAnimal(worldIn) {

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
  override def createChild(entity: EntityAgeable): EntityAgeable = new Fox(this.world)
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.CHICKEN
  override def getAmbientSound:SoundEvent = foxAmb
  override def getHurtSound:SoundEvent = foxDeath
  override def getDeathSound:SoundEvent = foxDeath
}

@SideOnly(Side.CLIENT)
class FoxRenderer(manager: RenderManager) extends RenderLiving[Fox](manager, new FoxModel(), .4f) {
  override def doRender(entity: Fox, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity, d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(entity: Fox): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_FOX)
}