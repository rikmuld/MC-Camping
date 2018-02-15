package com.rikmuld.camping.tileentity

import java.util.{Random, UUID}

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib.AdvancementInfo._
import com.rikmuld.camping.misc.ItemsData
import com.rikmuld.camping.objs.Definitions.Trap._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.camping.objs.blocks.Trap
import com.rikmuld.camping.registers.Objs
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.tileentity.TileEntityInventory
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.monster._
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{EntityLiving, EntityLivingBase}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ITickable
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World

import scala.collection.JavaConversions._

object TileTrap {
  final val UUIDSpeedTrap =
    new UUID(new Random(242346763).nextLong, new Random(476456556).nextLong)

  final val random =
    new Random()

  final val monsterLures = Array(
    Items.CHICKEN,
    Items.BEEF,
    Items.PORKCHOP,
    Registry.venisonRaw,
    Items.MUTTON,
    Items.RABBIT,
    Items.FISH
  )
}

class TileTrap extends TileEntityInventory with ITickable {
  var trappedEntity: Option[EntityLivingBase] =
    None

  var closeCooldown: Int =
    0

  var lastPlayer: Option[EntityPlayer] =
    None

  override def openInventory(player: EntityPlayer): Unit =
    lastPlayer = Some(player)

  override def getSizeInventory: Int =
    1

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState): Boolean =
    oldState.getBlock != newSate.getBlock

  override def onChange(slot: Int): Unit =
    if (!world.isRemote)
      PacketSender.sendToClient(
        new ItemsData(0, pos.getX, pos.getY, pos.getZ, getStackInSlot(0))
      )

  override def getName: String =
    "camping:trap"

  def setTrapped(entity: Option[EntityLivingBase]): Unit = {
    trappedEntity =
      if (entity.exists(!_.isInstanceOf[EntityPlayer] || config.trapPlayer))
        entity
      else
        None

    entity.foreach(trapped => {
      lastPlayer.foreach(player => TriggerHelper.trigger(ENTITY_TRAPPED, player, trapped))
      removeLureFor(trapped)
    })
  }

  def removeLureFor(entity: EntityLivingBase): Unit = entity match {
    case animal: EntityAnimal =>
      if(animal.isBreedingItem(getStackInSlot(0)))
        decrStackSize(0, 1)
    case mob: EntityMob =>
      if(TileTrap.monsterLures.contains(getStackInSlot(0).getItem))
        decrStackSize(0, 1)
    case _ =>
  }

  def setOpen(op: Boolean): Unit = {
    closeCooldown = 5

    getBlock.setState(world, pos, STATE_OPEN, op)

    if(op)
      setTrapped(None)
  }

  def isOpen: Boolean =
    getBlock.getBool(world, pos, STATE_OPEN)

  def getBlock: Trap =
    world.getBlockState(pos).getBlock.asInstanceOf[Trap]

  def getCaptureBounds: AxisAlignedBB =
    world.getBlockState(pos).getBoundingBox(world, pos).offset(pos)

  def getLureBounds: AxisAlignedBB =
    getCaptureBounds.grow(20, 10, 20)

  def getCaptureEntities: Seq[EntityLivingBase] =
    world.getEntitiesWithinAABB(classOf[EntityLivingBase], getCaptureBounds)

  def getLureEntities: Seq[EntityLiving] =
    world.getEntitiesWithinAABB(classOf[EntityLiving], getLureBounds)

  override def update():Unit =
    if(!world.isRemote) {
      if (closeCooldown > 0) closeCooldown -= 1

      val entities = getCaptureEntities

      if (isOpen)
        tryCatch(entities)
      else
        trappedEntity.foreach(entity => updateTrappedEntity(entity, entities))
    }

  private def tryCatch(entities: Seq[EntityLivingBase]): Unit = //do from hitten bounds in block, not checking every tick
    if (entities.nonEmpty && closeCooldown == 0) {
      setTrapped(Some(entities.head))
      setOpen(false)
    } else
      lure(getLureEntities)

  private def updateTrappedEntity(entity: EntityLivingBase, entities: Seq[EntityLivingBase]): Unit =
    if (entity.isDead || !entities.contains(entity))
      setTrapped(None)
    else
      applyEffects(entity)

  private def applyEffects(entity: EntityLivingBase): Unit = {
    if(TileTrap.random.nextInt(50) == 0) {
      val effect = new PotionEffect(Objs.bleeding, 200, 1)

      effect.getCurativeItems.clear()
      entity.addPotionEffect(effect)
    }

    entity match {
      case player: EntityPlayer =>
        player.getEntityData.setInteger("isInTrap", 2)

        val speed = player.getEntityAttribute(MOVEMENT_SPEED)

        if (Option(speed.getModifier(TileTrap.UUIDSpeedTrap)).isEmpty)
          speed.applyModifier(
            new AttributeModifier(TileTrap.UUIDSpeedTrap, "trap.speedNeg", -0.95f, 2)
          )
      case other =>
        other.setPositionAndUpdate(pos.getX + 0.5F, pos.getY, pos.getZ + 0.5F)
    }

    entity.setInWeb()//TODO make sure cannot jump
  }

  private def lure(entities: Seq[EntityLiving]): Unit =
    for(entity <- entities)
      lure(entity, getStackInSlot(0))

  private def lure(entity: EntityLiving, item: ItemStack): Unit = entity match {
    case monster: EntityMob =>
      if(TileTrap.monsterLures.contains(item.getItem))
        doLure(monster)
    case animal: EntityAnimal =>
      if(animal.isBreedingItem(item))
        doLure(animal)
    case _ =>
  }

  private def doLure(entity: EntityLiving): Unit =
    entity.getMoveHelper.setMoveTo(pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5, 1)
}