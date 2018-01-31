//package com.rikmuld.camping.objs.block
//
//import java.util.Random
//
//import com.google.common.base.Predicate
//import com.rikmuld.camping.CampingMod._
//import com.rikmuld.camping.objs.BlockDefinitions
//import com.rikmuld.camping.objs.block.SleepingBag._
//import com.rikmuld.corerm.objs.ObjInfo
//import com.rikmuld.corerm.objs.blocks._
//import com.rikmuld.corerm.objs.items.RMItemBlock
//import com.rikmuld.corerm.utils.MathUtils
//import net.minecraft.block.Block
//import net.minecraft.block.properties.{PropertyBool, PropertyDirection}
//import net.minecraft.block.state.IBlockState
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.entity.{Entity, EntityLivingBase}
//import net.minecraft.init.{Biomes, Blocks}
//import net.minecraft.item.ItemStack
//import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
//import net.minecraft.util.text.TextComponentTranslation
//import net.minecraft.util.{EnumBlockRenderType, EnumFacing, EnumHand}
//import net.minecraft.world.{IBlockAccess, World}
//import com.rikmuld.camping.misc.WorldBlock._
//
//object SleepingBag {
//  val IS_HEAD = PropertyBool.create("ishead")
//  val IS_OCCUPIED = PropertyBool.create("isoccupied")
//  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[EnumFacing]])
//}
//
//class SleepingBag(modId:String, info:ObjInfo) extends RMBlock(modId, info) with WithProperties with WithModel {
//  setDefaultState(getStateFromMeta(0))
//
//  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
//  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos) = new AxisAlignedBB(0, 0, 0, 1, 1f/16f, 1)
//  override def getProps = Array(new RMBoolProp(IS_HEAD, 0), new RMBoolProp(IS_OCCUPIED, 1), new RMFacingHorizontalProp(FACING, 2))
//  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) {
//    val bd = (world, pos)
//    bd.setState(blockState.getBaseState.withProperty(FACING, entity.getHorizontalFacing).withProperty(IS_HEAD, true.asInstanceOf[java.lang.Boolean]))
//    if(bd.nw(entity.getHorizontalFacing).block == Blocks.AIR || bd.nw(entity.getHorizontalFacing).isReplaceable) bd.nw(entity.getHorizontalFacing).setState(blockState.getBaseState.withProperty(IS_HEAD, false.asInstanceOf[java.lang.Boolean]).withProperty(FACING, entity.getHorizontalFacing.getOpposite))
//    dropIfCantStay(bd)
//  }
//  def isHead(state:IBlockState):Boolean = state.getValue(IS_HEAD).asInstanceOf[Boolean]
//  def isOccupied(state:IBlockState):Boolean = state.getValue(IS_OCCUPIED).asInstanceOf[Boolean]
//  def getFacing(state:IBlockState):EnumFacing = state.getValue(FACING).asInstanceOf[EnumFacing]
//  def canStay(bd:BlockData): Boolean = {
//    val facing = getFacing(bd.state)
//    bd.solidBelow && bd.nw(facing).block == this && bd.nw(facing).state.getValue(FACING)==facing.getOpposite && isHead(bd.nw(facing).state) == (if(isHead(bd.state)) false else true)
//  }
//  def dropIfCantStay(bd:BlockData) {
//    if (!canStay(bd)) {
//      breakBlock(bd.world, bd.pos, bd.state)
//      dropBlockAsItemWithChance(bd.world, bd.pos, bd.state, 1, 1)
//      bd.toAir
//      bd.update
//    }
//  }
//  override def getCollisionBoundingBox(state:IBlockState, world: IBlockAccess, pos:BlockPos): AxisAlignedBB = null
//  override def canPlaceBlockAt(world: World, pos:BlockPos) = (world, pos).canInstableStand
//  override def neighborChanged(state:IBlockState, world: World, pos:BlockPos, block: Block, fromPos: BlockPos) = if (!world.isRemote) dropIfCantStay((world, pos))
//  override def quantityDropped(state:IBlockState, fortune:Int, random:Random) = MathUtils.toInt(isHead(state))
//  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
//    if(!world.isRemote){
//      var bd = (world, pos)
//
//      if(!isHead(state)){
//        bd = bd.nw(getFacing(state))
//        if(bd.block!=this)return true
//      }
//
//      if(bd.world.provider.canRespawnHere && bd.world.getBiomeForCoordsBody(bd.pos) != Biomes.HELL){
//        if(isOccupied(bd.state)){
//          val sleepingPlayer = getPlayerInBed((bd.world, bd.pos))
//          if(sleepingPlayer != null) {
//            player.sendMessage(new TextComponentTranslation("tile.bed.occupied", new Object))
//            return true
//          }
//          bd.setState(bd.state.withProperty(IS_OCCUPIED, false.asInstanceOf[java.lang.Boolean]))
//        }
//        val sleepState = player.trySleep(bd.pos)
//        if(sleepState == EntityPlayer.SleepResult.OK)bd.setState(bd.state.withProperty(IS_OCCUPIED, true.asInstanceOf[java.lang.Boolean]), 4)
//        else {
//          if(sleepState == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
//          else if(sleepState == EntityPlayer.SleepResult.NOT_SAFE) player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
//        }
//      } else {
//        bd.toAir
//        bd.world.newExplosion(null, bd.x + 0.5, bd.y + 0.5, bd.z + 0.5, 5.0f, true, true)
//      }
//    }
//    true
//  }
//  def getPlayerInBed(bd:BlockData):EntityPlayer = {
//    val players = bd.world.playerEntities.iterator
//    var sleepingPlayer:EntityPlayer = null
//
//    do {
//      if(!players.hasNext)return null
//      sleepingPlayer = players.next
//    } while (!sleepingPlayer.isPlayerSleeping || !new BlockPos(sleepingPlayer.posX, sleepingPlayer.posY, sleepingPlayer.posZ).equals(bd.pos))
//
//    sleepingPlayer
//  }
//  override def isBed(state:IBlockState, world:IBlockAccess, pos:BlockPos, player:Entity) = true
//  override def setBedOccupied(world:IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied:Boolean) = if (world.isInstanceOf[World]) (world.asInstanceOf[World], pos).setState((world.asInstanceOf[World], pos).state.withProperty(IS_OCCUPIED, occupied.asInstanceOf[java.lang.Boolean]))
//  override def getBedDirection(state:IBlockState, world:IBlockAccess, pos:BlockPos) = getFacing(world.getBlockState(pos)).getOpposite
//  override def isBedFoot(world:IBlockAccess, pos:BlockPos) = !isHead(world.getBlockState(pos))
//}
//
//class SleepingBagItem(block:Block) extends RMItemBlock(MOD_ID, BlockDefinitions.SLEEPING_BAG, block){}