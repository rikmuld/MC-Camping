package com.rikmuld.camping.objs.entity

import org.lwjgl.opengl.GL11
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.EnumCreatureAttribute
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIAttackOnCollide
import net.minecraft.entity.ai.EntityAIHurtByTarget
import net.minecraft.entity.ai.EntityAILookIdle
import net.minecraft.entity.ai.EntityAIMate
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAITempt
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.CampingMod

class Bear(world: World) extends EntityAnimal(world) {
  setSize(1F, 1.125F)
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[EntityPlayer], 1.0D, false));
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[EntityZombie], 1.0D, false))
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[EntityVillager], 1.0D, false))
  tasks.addTask(1, new EntityAIAttackOnCollide(this, classOf[Camper], 1.0D, false))
  tasks.addTask(2, new EntityAIWander(this, 1.0D))
  tasks.addTask(3, new EntityAIWatchClosest(this, classOf[EntityPlayer], 8.0F))
  tasks.addTask(4, new EntityAILookIdle(this))
  tasks.addTask(5, new EntityAISwimming(this))
  tasks.addTask(6, new EntityAITempt(this, 1.2D, Items.fish, false))
  tasks.addTask(7, new EntityAIMate(this, 1.0D))
  targetTasks.addTask(0, new EntityAIHurtByTarget(this, true))
  targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityPlayer], true))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[EntityZombie], false))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[EntityVillager], false))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[Camper], false))

  protected override def applyEntityAttributes() {
    super.applyEntityAttributes()
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D)
    getAttributeMap.registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D)
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D)
  }
  protected override def canDespawn(): Boolean = false
  override def createChild(entityageable: EntityAgeable): EntityAgeable = new Bear(worldObj)
  protected override def dropFewItems(par1: Boolean, par2: Int) {
    var dropChance = rand.nextInt(3) + rand.nextInt(1 + par2)
    var drops: Int = 0
    while (drops < (dropChance * 2)) {
      entityDropItem(new ItemStack(animalParts, 1, PartsAnimal.FUR_BROWN), 0)
      drops += 1
    }
    dropChance = rand.nextInt(5) + 1 + rand.nextInt(1 + par2)
    drops = 0
    while (drops < dropChance) {
      if (isBurning) entityDropItem(nwsk(venisonCooked), 0)
      else entityDropItem(nwsk(venisonRaw), 0)
      drops += 1
    }
  }
  override def getCanSpawnHere(): Boolean = (world.getWorldInfo.getDifficulty.getDifficultyId > 0) && super.getCanSpawnHere
  override def getCreatureAttribute(): EnumCreatureAttribute = EnumCreatureAttribute.UNDEFINED
  override def getTotalArmorValue(): Int = 10
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.fish
  override def onUpdate() {
    super.onUpdate()
    if (!worldObj.isRemote && ((world.getWorldInfo.getDifficulty.getDifficultyId() == 0) || !CampingMod.config.useBears)) setDead()
  }
  override def attackEntityAsMob(entity: Entity): Boolean = {
    var f = this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue.toFloat
    var i = 0
    if (entity.isInstanceOf[EntityLivingBase]) {
      f += EnchantmentHelper.func_152377_a(getHeldItem, entity.asInstanceOf[EntityLivingBase].getCreatureAttribute)
      i += EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, getHeldItem)
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
  override def getLivingSound = MOD_ID + ":mob.bear.say"
  override def getHurtSound = MOD_ID + ":mob.bear.say"
  override def getDeathSound = MOD_ID + ":mob.bear.dead"
}

@SideOnly(Side.CLIENT)
class BearRenderer(model: ModelBase) extends RenderLiving(Minecraft.getMinecraft.getRenderManager, model, 1) {
  override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity.asInstanceOf[EntityLiving], d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(par1Entity: Entity): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_BEAR)
}