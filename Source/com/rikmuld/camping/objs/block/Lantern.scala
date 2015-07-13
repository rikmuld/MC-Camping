package com.rikmuld.camping.objs.block

import scala.collection.JavaConversions._
import scala.collection.JavaConversions._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.Utils._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.Objs.ModBlocks.MetaLookup._
import com.rikmuld.camping.objs.Objs.ModBlocks.MetaLookup
import com.rikmuld.camping.objs.Objs.ModItems.MetaLookup._
import com.rikmuld.camping.objs.block.Lantern._
import com.rikmuld.camping.objs.tile.TileLantern
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.RMItemBlock
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.corerm.objs.WithInstable
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.IStringSerializable
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.block.state.BlockState
import net.minecraft.block.properties.IProperty
import net.minecraft.init.Blocks
import net.minecraft.util.EnumWorldBlockLayer
import net.minecraft.block.properties.PropertyBool
import com.sun.org.apache.xalan.internal.xsltc.compiler.WithParam
import com.rikmuld.corerm.objs.WithProperties
import com.rikmuld.corerm.objs.RMBoolProp
import com.rikmuld.camping.objs.Objs.ModBlocks
import net.minecraft.entity.EntityLivingBase
    
object Lantern {
  val LIT = PropertyBool.create("lit")
  val TOP = PropertyBool.create("top")
}

class Lantern(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable with WithProperties {
  var burnTime:Option[Int] = None 
  var facing:EnumFacing = _
  
  setDefaultState(getStateFromMeta(0))
  
  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) = setTop((world, pos))
  override def getProps = Array(new RMBoolProp(LIT, 0), new RMBoolProp(TOP, 1))
  override def breakBlock(world: World, pos:BlockPos, state:IBlockState) {
    burnTime = if (Option((world, pos).tile).isDefined) Some((world, pos).tile.asInstanceOf[TileLantern].burnTime) else None
    super.breakBlock(world, pos, state)
  }
  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = ((world, pos).block == null || (world, pos).isReplaceable) && canStay((world, pos))
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileLantern()
  override def getDrops(world:IBlockAccess, pos:BlockPos, state:IBlockState, forture:Int):java.util.List[ItemStack] = {
    val stack = new ItemStack(this, 1, if(isLit(world, pos)) MetaLookup.Lantern.ON else MetaLookup.Lantern.OFF)
    burnTime.map { time => 
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound.setInteger("time", time)
    }
    List(stack)
  }
  override def canStay(bd:BlockData) = bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.UP) || bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.DOWN)
  @SideOnly(Side.CLIENT)
  override def getLightOpacity(world: IBlockAccess, pos:BlockPos): Int = {
    if (world.getBlockState(pos).getBlock == Objs.lantern&&isLit(world, pos))255 else 0
  }
  override def setBlockBoundsBasedOnState(world:IBlockAccess, pos:BlockPos) {
    if(isTop(world, pos))setBlockBounds(0.3F, 0, 0.3F, 0.7F, 1, 0.7F)
    else setBlockBounds(0.3F, 0, 0.3F, 0.7F, 0.75625F, 0.7F)
  }
  def isLit(world:IBlockAccess, pos:BlockPos) = world.getBlockState(pos).getValue(Lantern.LIT).asInstanceOf[Boolean]
  def isTop(world:IBlockAccess, pos:BlockPos) = world.getBlockState(pos).getValue(Lantern.TOP).asInstanceOf[Boolean]
  override def getLightValue(world: IBlockAccess, pos:BlockPos): Int = if(isLit(world, pos)) 15 else 0
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if (!world.isRemote) {
      val bd = (world, pos)
      if (!state.getValue(LIT).asInstanceOf[Boolean] && (player.getCurrentEquippedItem != null && player.getCurrentEquippedItem.getItem == Items.glowstone_dust)) {
        bd.setState(state.withProperty(LIT, true))
        bd.update
        bd.tile.asInstanceOf[TileLantern].burnTime = 1500
        return true
      }
    }
    false
  }
  def setTop(bd:BlockData) = bd.setState(bd.state.withProperty(Lantern.TOP, bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.UP) && !bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.DOWN)))
  override def getBlockLayer = EnumWorldBlockLayer.CUTOUT
  override def couldStay(bd:BlockData) = setTop(bd)
}

class LanternItem(block: Block) extends RMItemBlock(MOD_ID, Objs.ModBlocks.LANTERN, block) {
  override def addInformation(stack: ItemStack, player: EntityPlayer, list: java.util.List[_], par4: Boolean) {
    if (stack.hasTagCompound()) list.asInstanceOf[java.util.List[String]].add("Burning time left: " + (stack.getTagCompound.getInteger("time") / 2) + " seconds")
  }
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, creativetabs: CreativeTabs, stackList: java.util.List[_]) {
    val lantern = new ItemStack(item, 1, MetaLookup.Lantern.ON)
    lantern.setTagCompound(new NBTTagCompound())
    lantern.getTagCompound.setInteger("time", 1500)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(lantern)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(item, 1, MetaLookup.Lantern.OFF))
  }
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos:BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, state:IBlockState): Boolean = {
    if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state)) {
      (world, pos).tile.asInstanceOf[TileLantern].burnTime = if (stack.hasTagCompound() && (stack.getItemDamage == MetaLookup.Lantern.ON)) stack.getTagCompound.getInteger("time") else 0
      return true
    }
    false
  }
}