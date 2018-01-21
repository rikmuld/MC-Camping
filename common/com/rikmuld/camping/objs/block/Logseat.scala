package com.rikmuld.camping.objs.block

import scala.Right
import com.rikmuld.camping.objs.tile.TileLogseat
import com.rikmuld.corerm.CoreUtils.IntegerUtils
import com.rikmuld.corerm.CoreUtils.LivingUtils
import com.rikmuld.corerm.misc.Rotation
import com.rikmuld.corerm.misc.WorldBlock.BlockData
import com.rikmuld.corerm.misc.WorldBlock.IMBlockData
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.RMBoolProp
import com.rikmuld.corerm.objs.RMIntProp
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.corerm.objs.WithModel
import com.rikmuld.corerm.objs.WithProperties
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.rikmuld.camping.objs.block.Logseat._
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumBlockRenderType

object Logseat {
  val IS_TURNED = PropertyBool.create("turned")
  val LONG = PropertyInteger.create("long", 0, 3)
}

class Logseat(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithProperties {
  setDefaultState(getStateFromMeta(0))
  Rotation.addRotationBlock(this, Right((-2, -1, 1)))
  
  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def getProps = Array(new RMBoolProp(IS_TURNED, 0), new RMIntProp(LONG, 2, 1))
  override def createNewTileEntity(world:World, meta:Int):RMTile = new TileLogseat
  override def isWood(world: IBlockAccess, pos:BlockPos): Boolean = true
  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) = updateState((world, pos), (entity.facing.getHorizontalIndex & 1) == 0)
  override def neighborChanged(state:IBlockState, world:World, pos:BlockPos, block:Block, fromPos: BlockPos) = updateState((world, pos), getTurn((world, pos).state))
  def getLong(state:IBlockState):Int = state.getValue(LONG).asInstanceOf[Int]
  def getTurn(state:IBlockState):Boolean = state.getValue(IS_TURNED).asInstanceOf[Boolean]
  def updateState(bd:BlockData, turn:Boolean){
    var long = 0
    long = long.bitPut(0, if((turn==false&&(bd.north.block==this&&bd.north.state.getValue(IS_TURNED)==turn))||(turn==true&&(bd.east.block==this&&bd.east.state.getValue(IS_TURNED)==turn))) 1 else 0) 
    long = long.bitPut(1, if((turn==false&&(bd.south.block==this&&bd.south.state.getValue(IS_TURNED)==turn))||(turn==true&&(bd.west.block==this&&bd.west.state.getValue(IS_TURNED)==turn))) 1 else 0)
    bd.setState(blockState.getBaseState.withProperty(IS_TURNED, turn.asInstanceOf[java.lang.Boolean]).withProperty(LONG, long.asInstanceOf[java.lang.Integer]))
  }
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    val long = getLong(state)
    if(getTurn(state)) new AxisAlignedBB((if(!((long & 2) > 0)) 2f/16f else 0), 0, 5f/16f, 1-(if(!((long & 1) > 0)) 2f/16f else 0), 6f/16f, 1-5f/16f)
    else new AxisAlignedBB(5f/16f, 0, (if(!((long & 1) > 0)) 2f/16f else 0), 1-5f/16f, 6f/16f, 1-(if(!((long & 2) > 0)) 2f/16f else 0))
  }
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if (!player.isRiding && (new Vec3d(pos.getX + 0.5F, pos.getY + 0.5F, pos.getZ + 0.5F).distanceTo(new Vec3d(player.posX, player.posY, player.posZ)) <= 2.5F)) {
      world.getTileEntity(pos).asInstanceOf[TileLogseat].mountable.tryAddPlayer(player)
    }
    true
  }
}