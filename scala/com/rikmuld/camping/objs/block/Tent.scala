package com.rikmuld.camping.objs.block

import java.util.{ArrayList, Random}

import com.google.common.base.Predicate
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.block.Tent._
import com.rikmuld.camping.objs.tile.{TileEntityTent, TileTent}
import com.rikmuld.camping.objs.{BlockDefinitions, Objs}
import com.rikmuld.corerm.features.bounds.BoundsTracker
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.blocks.{RMBoolProp, _}
import com.rikmuld.corerm.objs.items.RMItemBlock
import com.rikmuld.corerm.tileentity.RMTile
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.block.Block
import net.minecraft.block.properties.{IProperty, PropertyBool, PropertyDirection}
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand, NonNullList}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object Tent {
  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[EnumFacing]])
  val ON = PropertyBool.create("on")
}

class Tent(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithProperties with WithInstable {
  setDefaultState(getStateFromMeta(0))
    
  var facingFlag:Int = _

  def isOn(world:IBlockAccess, pos:BlockPos):Boolean = {
    val state = world.getBlockState(pos)
    if(state.getBlock == this) state.getValue(ON).asInstanceOf[Boolean]
    else false
  }
  def setOn(world:World, pos:BlockPos, on:Boolean) =
    (world, pos).setState((world, pos).state.withProperty(ON, on.asInstanceOf[java.lang.Boolean]))

  override def getProps = Array(new RMFacingHorizontalProp(FACING, 0), new RMBoolProp(ON, 2))
  override def getCollisionBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    TileEntityTent.bounds(getFacing(state)).getBlockCollision
  }
  def getFacing(state:IBlockState) = state.getValue(Tent.FACING).getHorizontalIndex
  override def breakBlock(world: World, pos:BlockPos, state:IBlockState) {
    val tileFlag = Option((world, pos).tile)
    if (tileFlag.isDefined&&tileFlag.get.isInstanceOf[TileTent]) {
      var tile = tileFlag.get.asInstanceOf[TileTent]
      if(tile.structures!=null&&tile.structures(getFacing(state))!=null)tile.structures(getFacing(state)).destroyStructure(world, tile.tracker(getFacing(state)))
      if (!world.isRemote && !tile.dropped) {
        tile.dropped = true
        val stacks = new ArrayList[ItemStack]
        dropBlockAsItem(world, pos, state, 1)
        stacks.addAll(tile.getContends)
        val stack = nwsk(this, tile.color)
        stacks.add(stack)
        world.dropItemsInWorld(stacks, pos.getX, pos.getY, pos.getZ, new Random())
      }
      super.breakBlock(world, pos, state)
    }
  }
  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = {
    val bd = (world, pos)
    ((bd.block == null) || bd.isReplaceable) && Objs.tentStructure(facingFlag).canBePlaced(world, new BoundsTracker(bd.x, bd.y, bd.z, TileEntityTent.bounds(facingFlag)))
  }
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileTent
  override def onBlockPlacedBy(world: World, pos:BlockPos, state:IBlockState, entityLiving: EntityLivingBase, itemStack: ItemStack) {
    (world, pos).tile.asInstanceOf[TileTent].setColor(if (itemStack.hasTagCompound()) itemStack.getTagCompound.getInteger("color") else 15)
    (world, pos).setState(state.withProperty(Tent.FACING, entityLiving.facing).withProperty(Tent.ON, false.asInstanceOf[java.lang.Boolean]))
    (world, pos).tile.asInstanceOf[TileTent].createStructure
  }
  override def quantityDropped(random: Random): Int = 0
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    val tile = source.getTileEntity(pos).asInstanceOf[TileTent]
    TileEntityTent.bounds(getFacing(state)).getBlockBounds
  }
  override def dropIfCantStay(bd:BlockData) {
    val tile = bd.tile.asInstanceOf[TileTent]
    if (Option(tile.structures).isDefined && !tile.structures(getFacing(bd.state)).hadSolidUnderGround(bd.world, tile.tracker(getFacing(bd.state)))) {
      breakBlock(bd.world, bd.pos, bd.state)
    }
  }
  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(isOn(world, pos)) 15 else 0

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if (!world.isRemote) {
      val bd = (world, pos)
      val stack = player.getHeldItem(hand)
      val tile = bd.tile.asInstanceOf[TileTent]
      if ((stack != null) && tile.addContends(stack)) {
        stack.setCount(stack.getCount - 1)
        if (stack.getCount < 0) player.setCurrentItem(null)
        return true
      } else if ((stack != null) && (stack.getItem() == Items.DYE) && (bd.tile.asInstanceOf[TileTent].color != stack.getItemDamage)) {
        bd.tile.asInstanceOf[TileTent].setColor(stack.getItemDamage)
        stack.setCount(stack.getCount - 1)
        return true
      } else super.onBlockActivated(world, pos, state, player, hand, side, xHit, yHit, zHit)
    }
    true
  }
}

class TentItem(block:Block) extends RMItemBlock(CampingMod.MOD_ID, BlockDefinitions.TENT, block) {  

  override def getSubItems(tab:CreativeTabs, subItems:NonNullList[ItemStack]) {
    if(!isInCreativeTab(tab)) return
    for(i <- 0 until 16)subItems.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(this, 1, i))
  }
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState): Boolean = {
    if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)){
      (world, pos).tile.asInstanceOf[TileTent].color = stack.getItemDamage
      (world, pos).tile.asInstanceOf[TileTent].setColor(stack.getItemDamage)
      true
    } else false
  }
}