package com.rikmuld.camping.objs.block

import com.rikmuld.corerm.bounds.IBoundsBlock
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.util.BlockPos
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.entity.Entity
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.corerm.bounds.TileBounds
import net.minecraft.util.EnumFacing
import com.rikmuld.corerm.misc.WorldBlock._
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.world.biome.BiomeGenBase
import com.rikmuld.corerm.objs.WithProperties
import net.minecraft.block.properties.PropertyDirection
import com.google.common.base.Predicate
import com.rikmuld.corerm.objs.RMFacingHorizontalProp
import net.minecraft.block.properties.PropertyBool
import com.rikmuld.corerm.objs.RMBoolProp
import net.minecraft.block.state.IBlockState
import com.rikmuld.camping.objs.tile.TileTent
import net.minecraft.world.IWorldAccess

object TentBounds {
  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[EnumFacing]])
}

class TentBounds(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with IBoundsBlock with WithModel with WithProperties {
  setDefaultState(getStateFromMeta(0))
   
  override def getProps = Array(new RMFacingHorizontalProp(TentBounds.FACING, 0))
  override def isBed(world:IBlockAccess, pos:BlockPos, player:Entity) = true    
  override def setBedOccupied(world:IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied:Boolean) = getBaseTile(world, pos).setOccupied(occupied)
  override def getBedDirection(world:IBlockAccess, pos:BlockPos) = world.getBlockState(world.getTileEntity(pos).asInstanceOf[TileBounds].basePos).getValue(Tent.FACING).asInstanceOf[EnumFacing]
  override def isBedFoot(world:IBlockAccess, pos:BlockPos) = false
  def isOccupied(bd:BlockData) = getBaseTile(bd).isOccupied
  def getBaseTile(world:IBlockAccess, pos:BlockPos) = world.getTileEntity(world.getTileEntity(pos).asInstanceOf[TileBounds].basePos).asInstanceOf[TileTent]
  def getBaseTile(bd:BlockData) = (bd.world, bd.tile.asInstanceOf[TileBounds].basePos).tile.asInstanceOf[TileTent]
  def sleep(bd:BlockData, player:EntityPlayer){
    if(bd.world.provider.canRespawnHere && bd.world.getBiomeGenForCoords(bd.pos) != BiomeGenBase.hell){ 
      if(isOccupied(bd)){
        val sleepingPlayer = getPlayerInBed((bd.world, bd.pos))
        if(sleepingPlayer != null) {
          player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object))
          return
        }
       getBaseTile(bd).setOccupied(false)
      }
      val sleepState = player.trySleep(bd.pos)
      if(sleepState == EntityPlayer.EnumStatus.OK)getBaseTile(bd).setOccupied(true)
      else {
        if(sleepState == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object))
        else if(sleepState == EntityPlayer.EnumStatus.NOT_SAFE) player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object))
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
      sleepingPlayer = players.next.asInstanceOf[EntityPlayer]
    } while (!sleepingPlayer.isPlayerSleeping || !sleepingPlayer.playerLocation.equals(bd.pos))
    
    sleepingPlayer
  }
}