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
import net.minecraft.util.math.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.CampingMod
import net.minecraft.entity.ai.EntityAIAttackMelee
import net.minecraft.init.Enchantments
import net.minecraft.util.SoundEvent
import net.minecraft.item.ItemAxe

class Bear(world: World) extends EntityAnimal(world) {
  setSize(1F, 1.125F)
  tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false))
  tasks.addTask(2, new EntityAIWander(this, 1.0D))
  tasks.addTask(3, new EntityAIWatchClosest(this, classOf[EntityPlayer], 8.0F))
  tasks.addTask(4, new EntityAILookIdle(this))
  tasks.addTask(5, new EntityAISwimming(this))
  tasks.addTask(6, new EntityAITempt(this, 1.2D, Items.FISH, false))
  tasks.addTask(7, new EntityAIMate(this, 1.0D))
  targetTasks.addTask(0, new EntityAIHurtByTarget(this, true))
  targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityPlayer], true))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[EntityZombie], false))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[EntityVillager], false))
  targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, classOf[Camper], false))

  protected override def applyEntityAttributes() {
    super.applyEntityAttributes()
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D)
    getAttributeMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D)
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D)
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
  override def isBreedingItem(stack: ItemStack): Boolean = stack.getItem() == Items.FISH
  override def onUpdate() {
    super.onUpdate()
    if (!worldObj.isRemote && ((world.getWorldInfo.getDifficulty.getDifficultyId() == 0) || !CampingMod.config.useBears)) setDead()
  }
  override def attackEntityAsMob(entity: Entity): Boolean = {
    var f = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue.toFloat
    var i = 0
    if (entity.isInstanceOf[EntityLivingBase]) {
      f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), entity.asInstanceOf[EntityLivingBase].getCreatureAttribute());
      i += EnchantmentHelper.getKnockbackModifier(this);
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
      if (entity.isInstanceOf[EntityPlayer]) {
          val player = entity.asInstanceOf[EntityPlayer]
          val itemstack = this.getHeldItemMainhand();
          val itemstack1 = if(player.isHandActive()) player.inventory.getCurrentItem else null;

          if (itemstack != null && itemstack1 != null && itemstack.getItem.isInstanceOf[ItemAxe] && itemstack1.getItem() == Items.SHIELD) {
              val f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

              if (this.rand.nextFloat() < f1)
              {
                  player.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                  this.worldObj.setEntityState(player, 30.toByte);
              }
          }
      }
      this.applyEnchantments(this, entity)
    }
    flag
  }
        
  override def getAmbientSound:SoundEvent = bearAmb
  override def getHurtSound:SoundEvent = bearAmb
  override def getDeathSound:SoundEvent = bearDeath
}

@SideOnly(Side.CLIENT)
class BearRenderer(model: ModelBase) extends RenderLiving[Bear](Minecraft.getMinecraft.getRenderManager, model, 1) {
  override def doRender(entity: Bear, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity, d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(par1Entity: Bear): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_BEAR)
}