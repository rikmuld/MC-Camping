package com.rikmuld.camping.objs.block

import com.rikmuld.corerm.objs.WithInstable
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.RMBlock
import com.rikmuld.corerm.objs.Properties
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.client.renderer.EnumFaceDirection
import net.minecraft.util.EnumFacing
import com.google.common.base.Predicate
import com.rikmuld.camping.objs.block.SleepingBag._
import net.minecraft.block.state.BlockState
import net.minecraft.block.state.IBlockState
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.objs.WithProperties
import com.rikmuld.corerm.objs.RMBoolProp
import com.rikmuld.corerm.objs.RMFacingHorizontalProp
import net.minecraft.util.BlockPos
import net.minecraft.item.ItemStack
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.World
import com.rikmuld.corerm.misc.WorldBlock._
import net.minecraft.init.Blocks
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.block.Block
import net.minecraft.util.AxisAlignedBB
import java.util.Random
import com.rikmuld.corerm.objs.RMItemBlock
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.CampingMod._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.block.BlockBed
import net.minecraft.world.IBlockAccess
import net.minecraft.entity.Entity

object SleepingBag {
  val IS_HEAD = PropertyBool.create("isHead")
  val IS_OCCUPIED = PropertyBool.create("isOccupied")
  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[_]])
}

class SleepingBag(modId:String, info:ObjInfo) extends RMBlock(modId, info) with WithProperties with WithModel {
  setDefaultState(getStateFromMeta(0))
  setBlockBounds(0, 0, 0, 1, 1f/16f, 1)
    
  override def getProps = Array(new RMBoolProp(IS_HEAD, 0), new RMBoolProp(IS_OCCUPIED, 1), new RMFacingHorizontalProp(FACING, 2))
  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) {
    val bd = (world, pos)
    bd.setState(blockState.getBaseState.withProperty(FACING, entity.facing).withProperty(IS_HEAD, true))
    if(bd.nw(entity.facing).block == Blocks.air || bd.nw(entity.facing).isReplaceable) bd.nw(entity.facing).setState(blockState.getBaseState.withProperty(IS_HEAD, false).withProperty(FACING, entity.facing.getOpposite))
    dropIfCantStay(bd)
  }
  def isHead(state:IBlockState):Boolean = state.getValue(IS_HEAD).asInstanceOf[Boolean]
  def isOccupied(state:IBlockState):Boolean = state.getValue(IS_OCCUPIED).asInstanceOf[Boolean]
  def getFacing(state:IBlockState):EnumFacing = state.getValue(FACING).asInstanceOf[EnumFacing]
  def canStay(bd:BlockData): Boolean = {
    val facing = getFacing(bd.state)
    bd.solidBelow && bd.nw(facing).block == this && bd.nw(facing).state.getValue(FACING)==facing.getOpposite && isHead(bd.nw(facing).state) == (if(isHead(bd.state)) false else true)
  }
  def dropIfCantStay(bd:BlockData) {
    if (!canStay(bd)) {
      breakBlock(bd.world, bd.pos, bd.state)
      dropBlockAsItemWithChance(bd.world, bd.pos, bd.state, 1, 1)
      bd.toAir
      bd.update
    }
  }
  override def getCollisionBoundingBox(world: World, pos:BlockPos, state:IBlockState): AxisAlignedBB = null
  override def canPlaceBlockAt(world: World, pos:BlockPos) = (world, pos).canInstableStand
  override def onNeighborBlockChange(world: World, pos:BlockPos, state:IBlockState, block: Block) = if (!world.isRemote) dropIfCantStay((world, pos))
  override def quantityDropped(state:IBlockState, fortune:Int, random:Random) = isHead(state).intValue
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if(!world.isRemote){
      var bd = (world, pos)

      if(!isHead(state)){
        bd = bd.nw(getFacing(state))
        if(bd.block!=this)return true
      }
      
      if(bd.world.provider.canRespawnHere && bd.world.getBiomeGenForCoords(bd.pos) != BiomeGenBase.hell){ 
        if(isOccupied(bd.state)){
          val sleepingPlayer = getPlayerInBed((bd.world, bd.pos))
          if(sleepingPlayer != null) {
            player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object))
            return true
          }
          bd.setState(bd.state.withProperty(IS_OCCUPIED, false))
        }
        val sleepState = player.trySleep(bd.pos)
        if(sleepState == EntityPlayer.EnumStatus.OK)bd.setState(bd.state.withProperty(IS_OCCUPIED, true), 4)
        else {
          if(sleepState == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object))
          else if(sleepState == EntityPlayer.EnumStatus.NOT_SAFE) player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object))
        }
      } else {
        bd.toAir
        bd.world.newExplosion(null, bd.x + 0.5, bd.y + 0.5, bd.z + 0.5, 5.0f, true, true)
      }
    }
    true
  }
  def getPlayerInBed(bd:BlockData):EntityPlayer = {
    val players = bd.world.playerEntities.iterator
    var sleepingPlayer:EntityPlayer = null
    
    do {
      if(!players.hasNext)return null
      sleepingPlayer = players.next.asInstanceOf[EntityPlayer]
    } while (!sleepingPlayer.isPlayerSleeping || !sleepingPlayer.playerLocation.equals(bd.pos))
    
    sleepingPlayer
  }
  override def isBed(world:IBlockAccess, pos:BlockPos, player:Entity) = true    
  override def setBedOccupied(world:IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied:Boolean) = if (world.isInstanceOf[World]) (world.asInstanceOf[World], pos).setState((world.asInstanceOf[World], pos).state.withProperty(IS_OCCUPIED, occupied))
  override def getBedDirection(world:IBlockAccess, pos:BlockPos) = getFacing(world.getBlockState(pos)).getOpposite
  override def isBedFoot(world:IBlockAccess, pos:BlockPos) = !isHead(world.getBlockState(pos))
}

class SleepingBagItem(block:Block) extends RMItemBlock(MOD_ID, Objs.ModBlocks.SLEEPING_BAG, block){}