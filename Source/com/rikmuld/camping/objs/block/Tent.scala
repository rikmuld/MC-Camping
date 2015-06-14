package com.rikmuld.camping.objs.block

import com.rikmuld.corerm.objs.RMBlockContainer
import java.io.ObjectInput
import com.rikmuld.corerm.objs.ObjInfo
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.util.EnumFacing
import com.google.common.base.Predicate
import com.rikmuld.corerm.objs.WithProperties
import com.rikmuld.corerm.objs.WithModel
import com.rikmuld.corerm.objs.RMFacingHorizontalProp
import com.rikmuld.camping.objs.block.Tent._
import com.rikmuld.corerm.objs.WithInstable
import com.rikmuld.camping.objs.tile.TileEntityTent
import net.minecraft.util.BlockPos
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import net.minecraft.entity.Entity
import com.rikmuld.camping.objs.tile.TileEntityTent
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.objs.tile.TileTent
import net.minecraft.dispenser.IBlockSource
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import java.util.ArrayList
import java.util.Random
import net.minecraft.item.ItemStack
import com.rikmuld.camping.objs.Objs
import net.minecraft.util.MathHelper
import com.rikmuld.corerm.objs.RMTile
import net.minecraft.world.IBlockAccess
import net.minecraft.entity.EntityLivingBase
import com.rikmuld.camping.objs.tile.TileEntityTent
import net.minecraft.nbt.NBTTagCompound
import scala.collection.JavaConversions._
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.objs.tile.TileEntityTent
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.camping.objs.misc.OpenGui
import net.minecraft.init.Items
import com.rikmuld.camping.objs.tile.TileEntityTent
import com.rikmuld.camping.objs.misc.OpenGui
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.corerm.bounds.BoundsTracker
import com.rikmuld.camping.objs.Objs.ModBlocks.MetaLookup
import com.rikmuld.corerm.objs.RMItemBlock
import com.rikmuld.camping.CampingMod
import net.minecraft.block.Block
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import scala.collection.JavaConversions._

object Tent {
  val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.asInstanceOf[Predicate[_]])
}

class Tent(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithProperties with WithInstable {
  setDefaultState(getStateFromMeta(0))
    
  var facingFlag:Int = _
  
  override def getProps = Array(new RMFacingHorizontalProp(FACING, 0))
  override def addCollisionBoxesToList(world: World, pos:BlockPos, state:IBlockState, alignedBB: AxisAlignedBB, list: java.util.List[_], entity: Entity) {
    TileEntityTent.bounds(getFacing(state)).setBlockCollision(this)
    super.addCollisionBoxesToList(world, pos, state, alignedBB, list, entity)
  }
  def getFacing(state:IBlockState) = state.getValue(Tent.FACING).asInstanceOf[EnumFacing].getHorizontalIndex
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
    (world, pos).setState(state.withProperty(Tent.FACING, entityLiving.facing))
    (world, pos).tile.asInstanceOf[TileTent].createStructure
  }
  override def quantityDropped(random: Random): Int = 0
  override def setBlockBoundsBasedOnState(world: IBlockAccess, pos:BlockPos) {
    val tile = world.getTileEntity(pos).asInstanceOf[TileTent]
    TileEntityTent.bounds(getFacing(world.getBlockState(pos))).setBlockBounds(this)
  }
  override def dropIfCantStay(bd:BlockData) {
    val tile = bd.tile.asInstanceOf[TileTent]
    if (Option(tile.structures).isDefined && !tile.structures(getFacing(bd.state)).hadSolidUnderGround(bd.world, tile.tracker(getFacing(bd.state)))) {
      breakBlock(bd.world, bd.pos, bd.state)
    }
  }
  override def getLightValue(world: IBlockAccess, pos:BlockPos): Int = {
    val tile = world.getTileEntity(pos).asInstanceOf[TileTent]
    if ((tile.lanternDamage == MetaLookup.Lantern.ON) && (tile.lanterns > 0)) 15 else 0
  }
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if (!world.isRemote) {
      val bd = (world, pos)
      if ((player.getCurrentEquippedItem != null) && bd.tile.asInstanceOf[TileTent].addContends(player.getCurrentEquippedItem)) {
        player.getCurrentEquippedItem.stackSize -= 1
        if (player.getCurrentEquippedItem.stackSize < 0) player.setCurrentItem(null)
        return true
      } else if ((player.getCurrentEquippedItem != null) && (player.getCurrentEquippedItem.getItem() == Items.dye) && (bd.tile.asInstanceOf[TileTent].color != player.getCurrentEquippedItem.getItemDamage)) {
        bd.tile.asInstanceOf[TileTent].setColor(player.getCurrentEquippedItem.getItemDamage)
        player.getCurrentEquippedItem.stackSize -= 1
        return true
      } else super.onBlockActivated(world, pos, state, player, side, xHit, yHit, zHit)
    }
    true
  }
  override def openGui(bd:BlockData, player:EntityPlayer, id:Int) = PacketSender.toClient(new OpenGui(id, bd.x, bd.y, bd.z))
}

class TentItem(block:Block) extends RMItemBlock(CampingMod.MOD_ID, Objs.ModBlocks.TENT, block) {  
  @SideOnly(Side.CLIENT)
  override def getSubItems(itemIn:Item, tab:CreativeTabs, subItems:java.util.List[_]) {
    subItems.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(itemIn, 1, 15)) 
  }
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState): Boolean = {
    if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)){
      (world, pos).tile.asInstanceOf[TileTent].color = stack.getItemDamage
      (world, pos).tile.asInstanceOf[TileTent].setColor(stack.getItemDamage)
      true
    }
    false
  }
}