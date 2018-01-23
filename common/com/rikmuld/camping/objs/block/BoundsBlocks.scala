package com.rikmuld.camping.objs.block

import com.google.common.base.Predicate
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.corerm.features.bounds.{IBoundsBlock, TileBounds}
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.blocks.{RMBlockContainer, RMFacingHorizontalProp, WithModel, WithProperties}
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Biomes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.{EnumBlockRenderType, EnumFacing}
import net.minecraft.world.IBlockAccess

object TentBounds {
  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[EnumFacing]])
}

class TentBounds(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with IBoundsBlock with WithModel with WithProperties {
  setDefaultState(getStateFromMeta(0))

  override def getRenderType(state:IBlockState) = EnumBlockRenderType.INVISIBLE
  override def getProps = Array(new RMFacingHorizontalProp(TentBounds.FACING, 0))
  override def isBed(state:IBlockState, world:IBlockAccess, pos:BlockPos, player:Entity) = true    
  override def setBedOccupied(world:IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied:Boolean) = getBaseTile(world, pos).setOccupied(occupied)
  override def getBedDirection(state:IBlockState, world:IBlockAccess, pos:BlockPos) = world.getBlockState(world.getTileEntity(pos).asInstanceOf[TileBounds].basePos).getValue(Tent.FACING).asInstanceOf[EnumFacing]
  override def isBedFoot(world:IBlockAccess, pos:BlockPos) = false
  def isOccupied(bd:BlockData) = getBaseTile(bd).isOccupied
  def getBaseTile(world:IBlockAccess, pos:BlockPos) = world.getTileEntity(world.getTileEntity(pos).asInstanceOf[TileBounds].basePos).asInstanceOf[TileTent]
  def getBaseTile(bd:BlockData) = (bd.world, bd.tile.asInstanceOf[TileBounds].basePos).tile.asInstanceOf[TileTent]
  def sleep(bd:BlockData, player:EntityPlayer){
    if(bd.world.provider.canRespawnHere && bd.world.getBiomeForCoordsBody(bd.pos) != Biomes.HELL){
      if(isOccupied(bd)){
        val sleepingPlayer = getPlayerInBed((bd.world, bd.pos))
        if(sleepingPlayer != null) {
          player.sendMessage(new TextComponentTranslation("tile.bed.occupied", new Object))
          return
        }
       getBaseTile(bd).setOccupied(false)
      }
      val sleepState = player.trySleep(bd.pos)
      if(sleepState == EntityPlayer.SleepResult.OK)getBaseTile(bd).setOccupied(true)
      else {
        if(sleepState == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
        else if(sleepState == EntityPlayer.SleepResult.NOT_SAFE) player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
      }
    } else {
      bd.toAir
      bd.world.newExplosion(null, bd.x + 0.5, bd.y + 0.5, bd.z + 0.5, 5.0f, true, true)
    }
  }
  def getPlayerInBed(bd:BlockData):EntityPlayer = {
    val players = bd.world.playerEntities.iterator
    var sleepingPlayer:EntityPlayer = null

    do {
      if(!players.hasNext)return null
      sleepingPlayer = players.next
    } while (!sleepingPlayer.isPlayerSleeping || !new BlockPos(sleepingPlayer.posX, sleepingPlayer.posY, sleepingPlayer.posZ).equals(bd.pos))
    
    sleepingPlayer
  }
}