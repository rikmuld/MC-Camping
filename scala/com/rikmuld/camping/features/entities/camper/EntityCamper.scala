package com.rikmuld.camping.features.entities.camper

import java.util.{ArrayList, Random}

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Parts
import com.rikmuld.camping.Library._
import com.rikmuld.camping.features.entities.bear.EntityBear
import com.rikmuld.corerm.advancements.TriggerHelper
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity._
import net.minecraft.entity.ai._
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.{Items, SoundEvents}
import net.minecraft.item.{Item, ItemAxe, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.{DataParameter, DataSerializers, EntityDataManager}
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.util._
import net.minecraft.util.math.{BlockPos, MathHelper}
import net.minecraft.util.text.TextComponentString
import net.minecraft.village.{MerchantRecipe, MerchantRecipeList}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable.HashMap

object EntityCamper {
  final val GENDER:DataParameter[Integer] = EntityDataManager.createKey(classOf[EntityCamper], DataSerializers.VARINT);

  val recipeListRaw = new HashMap[Item, Tuple[Integer, Integer]]()

  recipeListRaw(CampingMod.OBJ.venisonCooked) = new Tuple(Integer.valueOf(-4), Integer.valueOf(1))
  recipeListRaw(CampingMod.OBJ.knife) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
  recipeListRaw(CampingMod.OBJ.backpack) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
  recipeListRaw(CampingMod.OBJ.furLeg) = new Tuple(Integer.valueOf(4), Integer.valueOf(6))
  recipeListRaw(CampingMod.OBJ.furHead) = new Tuple(Integer.valueOf(3), Integer.valueOf(5))
  recipeListRaw(CampingMod.OBJ.furChest) = new Tuple(Integer.valueOf(5), Integer.valueOf(8))
  recipeListRaw(CampingMod.OBJ.furBoot) = new Tuple(Integer.valueOf(2), Integer.valueOf(4))
  recipeListRaw(CampingMod.OBJ.parts) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
}

class EntityCamper(worldIn: World) extends EntityCreature(worldIn) with IMerchant with INpc {
  setGender(rand.nextInt(2))
  setSize(0.6F, 1.8F)
  getNavigator.asInstanceOf[PathNavigateGround].setCanSwim(true)
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(2, new EntityAIAttackMelee(this, 1.0F, false))
  tasks.addTask(4, new EntityAIWatchClosest2(this, classOf[EntityPlayer], 3.0F, 1.0F))
  tasks.addTask(6, new EntityAIWander(this, 0.6D))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[EntityMob], false))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[EntityBear], false))

  var playerBuy: EntityPlayer = null
  var recipeList: MerchantRecipeList = _
  var camp:Option[Campsite] = None

  override def getWorld: World = world
  def setGender(gender: Int) = dataManager.set(EntityCamper.GENDER, Integer.valueOf(gender))
  def getGender: Int = dataManager.get(EntityCamper.GENDER)
  override def getPos: BlockPos = new BlockPos(this)
  override def writeEntityToNBT(tag: NBTTagCompound) {
    super.writeEntityToNBT(tag)
    tag.setInteger("gender", getGender)
    camp.map { campsite =>
      tag.setBoolean("hasCamp", true)
      tag.setTag("campsite", campsite.toNBT(new NBTTagCompound))
    }
  }
  override def readEntityFromNBT(tag: NBTTagCompound) {
    super.writeEntityToNBT(tag)
    setGender(tag.getInteger("gender"))
    if(tag.hasKey("hasCamp")) camp = Some(Campsite.fromNBT(this, tag.getCompoundTag("campsite")))
  }
  protected override def applyEntityAttributes {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D)
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D)
  }
  override def canDespawn = false
  override def entityInit {
    super.entityInit
    dataManager.register(EntityCamper.GENDER, Integer.valueOf(0))
  }
  override def onUpdate {
    super.onUpdate
    camp.map {campsite => campsite.update }
  }
  protected override def getDeathSound:SoundEvent = SoundEvents.ENTITY_VILLAGER_DEATH
  protected override def getHurtSound(source: DamageSource):SoundEvent = SoundEvents.ENTITY_VILLAGER_HURT
  protected override def getAmbientSound:SoundEvent = SoundEvents.ENTITY_VILLAGER_AMBIENT
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
      if (entity.isInstanceOf[EntityPlayer]) {
          val player = entity.asInstanceOf[EntityPlayer]
          val itemstack = this.getHeldItemMainhand();
          val itemstack1 = if(player.isHandActive()) player.inventory.getCurrentItem else null;

          if (itemstack != null && itemstack1 != null && itemstack.getItem.isInstanceOf[ItemAxe] && itemstack1.getItem() == Items.SHIELD) {
              val f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

              if (this.rand.nextFloat() < f1)
              {
                  player.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                  this.world.setEntityState(player, 30.toByte);
              }
          }
      }
      if (j > 0) entity.setFire(j * 4)
      this.applyEnchantments(this, entity)
    }
    flag
  }
  override def processInteract(player: EntityPlayer, hand:EnumHand): Boolean = {
    val itemstack = player.inventory.getCurrentItem
    val flag = (itemstack != null) && (itemstack.getItem == Items.SPAWN_EGG)
    if (!flag && isEntityAlive && !isTrading && !player.isSneaking) {
      if (!world.isRemote) {
        setCustomer(player)
        player.displayVillagerTradeGui(this)
        TriggerHelper.trigger(AdvancementInfo.CAMPER_INTERACT, player, this)
      }
      true
    } else super.processInteract(player, hand)
  }
  override def setCustomer(player: EntityPlayer) = playerBuy = player
  override def getCustomer(): EntityPlayer = playerBuy
  override def getRecipes(player: EntityPlayer): MerchantRecipeList = if (recipeList == null) setRecipeList else recipeList
  @SideOnly(Side.CLIENT)
  override def setRecipes(recipes: MerchantRecipeList) {}
  override def useRecipe(resipes: MerchantRecipe) {}
  override def verifySellingItem(stack:ItemStack) {}
  def isTrading: Boolean = getCustomer != null
  def setRecipeList: MerchantRecipeList = {
    recipeList = new MerchantRecipeList()
    if (getGender == 0) {
      addBlacksmithItem(recipeList, CampingMod.OBJ.furBoot, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.furChest, 0, rand, 0.3F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.furHead, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.furLeg, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.knife, 0, rand, 0.6F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.backpack, 0, rand, 0.5F)
    } else {
      addBlacksmithItem(recipeList, CampingMod.OBJ.venisonCooked, 0, rand, 0.6F)
      addBlacksmithItem(recipeList, CampingMod.OBJ.parts, Parts.PAN, rand, 0.6F)
    }
    if (recipeList.isEmpty()) {
      addBlacksmithItem(recipeList, CampingMod.OBJ.knife, 0, rand, 0.6F)
    }
    recipeList
  }
  override def getDisplayName = new TextComponentString("Camper")
  def addBlacksmithItem(merchantRecipeList: MerchantRecipeList, item: Item, meta: Int, random: Random, par3: Float) {
    if (random.nextFloat() < par3) {
      val j = randomCount(item, random)
      var itemstack: ItemStack = null
      var itemstack1: ItemStack = null
      if (j < 0) {
        itemstack = new ItemStack(Items.EMERALD, 1, 0)
        itemstack1 = new ItemStack(item, -j, meta)
      } else {
        itemstack = new ItemStack(Items.EMERALD, j, 0)
        itemstack1 = new ItemStack(item, 1, meta)
      }
      merchantRecipeList.asInstanceOf[ArrayList[MerchantRecipe]].add(new MerchantRecipe(itemstack, itemstack1))
    }
  }
  private def randomCount(item: Item, random: Random): Int = {
    val tuple = EntityCamper.recipeListRaw(item).asInstanceOf[Tuple[Integer, Integer]]
    if (tuple == null) 1 else (if (tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() >= tuple.getSecond.asInstanceOf[java.lang.Integer].intValue()) tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() else tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() + random.nextInt(tuple.getSecond.asInstanceOf[java.lang.Integer].intValue() - tuple.getFirst.asInstanceOf[java.lang.Integer].intValue()))
  }
  def setCampsite(campsite:Option[Campsite]) = camp = campsite
}