package com.rikmuld.camping.objs.entity

import java.util.ArrayList
import java.util.Random
import scala.collection.mutable.HashMap
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IMerchant
import net.minecraft.entity.INpc
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIAttackOnCollide
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAIWatchClosest2
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.pathfinding.PathNavigateGround
import net.minecraft.util.ChatComponentText
import net.minecraft.util.DamageSource
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Tuple
import net.minecraft.village.MerchantRecipe
import net.minecraft.village.MerchantRecipeList
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.util.BlockPos
import com.rikmuld.corerm.misc.WorldBlock._
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor
import com.rikmuld.camping.objs.block.Tent

object Camper {
  val recipeListRaw = new HashMap[Item, Tuple]()

  recipeListRaw(venisonCooked) = new Tuple(Integer.valueOf(-4), Integer.valueOf(1))
  recipeListRaw(knife) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
  recipeListRaw(backpack) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
  recipeListRaw(furLeg) = new Tuple(Integer.valueOf(4), Integer.valueOf(6))
  recipeListRaw(furHead) = new Tuple(Integer.valueOf(3), Integer.valueOf(5))
  recipeListRaw(furChest) = new Tuple(Integer.valueOf(5), Integer.valueOf(8))
  recipeListRaw(furBoot) = new Tuple(Integer.valueOf(2), Integer.valueOf(4))
  recipeListRaw(parts) = new Tuple(Integer.valueOf(1), Integer.valueOf(2))
}

class Camper(world: World) extends EntityCreature(world) with IMerchant with INpc {
  setGender(rand.nextInt(2))
  setSize(0.6F, 1.8F)
  getNavigator.asInstanceOf[PathNavigateGround].func_179688_b(true)
  getNavigator.asInstanceOf[PathNavigateGround].func_179690_a(true)
  tasks.addTask(1, new EntityAISwimming(this))
  tasks.addTask(2, new EntityAIAttackOnCollide(this, classOf[Bear], 1.0F, false))
  tasks.addTask(3, new EntityAIAttackOnCollide(this, classOf[EntityMob], 1.0F, false))
  tasks.addTask(4, new EntityAIWatchClosest2(this, classOf[EntityPlayer], 3.0F, 1.0F))
  tasks.addTask(6, new EntityAIWander(this, 0.6D))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[EntityMob], false))
  targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, classOf[Bear], false))

  var playerBuy: EntityPlayer = null
  var recipeList: MerchantRecipeList = _
  var camp:Option[Campsite] = None

  def setGender(gender: Int) = dataWatcher.updateObject(16, Integer.valueOf(gender))
  def getGender: Int = dataWatcher.getWatchableObjectInt(16)
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
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D)
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D)
  }
  override def canDespawn = false
  override def entityInit {
    super.entityInit
    dataWatcher.addObject(16, Integer.valueOf(0))
  }
  override def onUpdate {
    super.onUpdate
    camp.map {campsite => campsite.update }
  }
  protected override def getDeathSound = "mob.villager.death"
  protected override def getHurtSound = "mob.villager.hit"
  protected override def getLivingSound = "mob.villager.idle"
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
  override def interact(player: EntityPlayer): Boolean = {
    val itemstack = player.inventory.getCurrentItem
    val flag = (itemstack != null) && (itemstack.getItem == Items.spawn_egg)
    if (!flag && isEntityAlive && !isTrading && !player.isSneaking) {
      if (!worldObj.isRemote) {
        setCustomer(player)
        player.triggerAchievement(Objs.achExplorer)
        player.displayVillagerTradeGui(this)
      }
      true
    } else super.interact(player)
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
      addBlacksmithItem(recipeList, furBoot, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, furChest, 0, rand, 0.3F)
      addBlacksmithItem(recipeList, furHead, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, furLeg, 0, rand, 0.4F)
      addBlacksmithItem(recipeList, knife, 0, rand, 0.6F)
      addBlacksmithItem(recipeList, backpack, 0, rand, 0.5F)
    } else {
      addBlacksmithItem(recipeList, venisonCooked, 0, rand, 0.6F)
      addBlacksmithItem(recipeList, parts, Parts.PAN, rand, 0.6F)
    }
    if (recipeList.isEmpty()) {
      addBlacksmithItem(recipeList, knife, 0, rand, 0.6F)
    }
    recipeList
  }
  override def getDisplayName = new ChatComponentText("Camper")
  def addBlacksmithItem(merchantRecipeList: MerchantRecipeList, item: Item, meta: Int, random: Random, par3: Float) {
    if (random.nextFloat() < par3) {
      val j = randomCount(item, random)
      var itemstack: ItemStack = null
      var itemstack1: ItemStack = null
      if (j < 0) {
        itemstack = new ItemStack(Items.emerald, 1, 0)
        itemstack1 = new ItemStack(item, -j, meta)
      } else {
        itemstack = new ItemStack(Items.emerald, j, 0)
        itemstack1 = new ItemStack(item, 1, meta)
      }
      merchantRecipeList.asInstanceOf[ArrayList[MerchantRecipe]].add(new MerchantRecipe(itemstack, itemstack1))
    }
  }
  private def randomCount(item: Item, random: Random): Int = {
    val tuple = Camper.recipeListRaw(item).asInstanceOf[Tuple]
    if (tuple == null) 1 else (if (tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() >= tuple.getSecond.asInstanceOf[java.lang.Integer].intValue()) tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() else tuple.getFirst.asInstanceOf[java.lang.Integer].intValue() + random.nextInt(tuple.getSecond.asInstanceOf[java.lang.Integer].intValue() - tuple.getFirst.asInstanceOf[java.lang.Integer].intValue()))
  }
  def setCampsite(campsite:Option[Campsite]) = camp = campsite
}

@SideOnly(Side.CLIENT)
class CamperRender(model: ModelBase) extends RenderLiving(Minecraft.getMinecraft().getRenderManager(), model, 0.5f) {
  override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    super.doRender(entity.asInstanceOf[EntityLiving], d0, d1, d2, f, f1)
  }
  protected override def getEntityTexture(entity: Entity): ResourceLocation = new ResourceLocation(if (entity.asInstanceOf[Camper].getGender == 0) TextureInfo.MODEL_CAMPER_MALE else TextureInfo.MODEL_CAMPER_FEMALE)
}

object Campsite {
  def fromNBT(camper:Camper, tag:NBTTagCompound):Campsite = {
    val center = tag.getIntArray("center")
    val tent = tag.getIntArray("tent")
    println("hallo")
    new Campsite(camper, new BlockPos(center(0), center(1), center(2)), new BlockPos(tent(0), tent(1), tent(2)))
  }
}

class Campsite(camper:Camper, center:BlockPos, tent:BlockPos) {  
  def update {
    if(!(getWorld, tent).block.isInstanceOf[Tent])camper.setCampsite(None)
    else if(Math.sqrt(camper.getDistanceSqToCenter(center)) > 15 && !camper.getMoveHelper.isUpdating)camper.getMoveHelper.setMoveTo(center.getX, center.getY, center.getZ, .8)
  }
  def getWorld = camper.worldObj
  def toNBT(tag:NBTTagCompound):NBTTagCompound = {
    tag.setIntArray("center", Array(center.getX, center.getY, center.getZ))
    tag.setIntArray("tent", Array(tent.getX, tent.getY, tent.getZ))
    tag
  }
}