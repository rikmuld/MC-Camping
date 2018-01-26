package com.rikmuld.camping.objs.block

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.block.Lantern._
import com.rikmuld.camping.objs.tile.TileLantern
import com.rikmuld.camping.objs.{BlockDefinitions, Objs}
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.blocks._
import com.rikmuld.corerm.objs.items.RMItemBlock
import com.rikmuld.corerm.tileentity.TileEntitySimple
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util._
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object Lantern {
  val LIT = PropertyBool.create("lit")
  val TOP = PropertyBool.create("top")
}

class Lantern(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable with WithProperties {
  var burnTime:Option[Int] = None 
  var facing:EnumFacing = _
  
  setDefaultState(getStateFromMeta(0))

  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) = setTop((world, pos))
  override def getProps = Array(new RMBoolProp(LIT, 0), new RMBoolProp(TOP, 1))
  override def breakBlock(world: World, pos:BlockPos, state:IBlockState) {
    burnTime = if (Option((world, pos).tile).isDefined) Some((world, pos).tile.asInstanceOf[TileLantern].burnTime) else None
    super.breakBlock(world, pos, state)
  }
  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = ((world, pos).block == null || (world, pos).isReplaceable) && canStay((world, pos))
  override def createNewTileEntity(world: World, meta: Int): TileEntitySimple = new TileLantern()
  override def getDrops(world:IBlockAccess, pos:BlockPos, state:IBlockState, forture:Int):java.util.List[ItemStack] = {
    val stack = new ItemStack(this, 1, BlockDefinitions.Lantern.OFF)
    burnTime.map { time => 
      if(time>0){
        stack.setItemDamage(BlockDefinitions.Lantern.ON)
        stack.setTagCompound(new NBTTagCompound())
        stack.getTagCompound.setInteger("time", time)
      }
    }
    List(stack)
  }
  override def canStay(bd:BlockData) = bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.UP) || bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.DOWN)
  @SideOnly(Side.CLIENT)
  override def getLightOpacity(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int = {
    if (world.getBlockState(pos).getBlock == Objs.lantern&&isLit(world, pos))255 else 0
  }
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    if(isTop(source, pos))new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 1, 0.7F)
    else new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 0.75625F, 0.7F)
  }
  def isLit(world:IBlockAccess, pos:BlockPos) = getStateValue(world, pos, Lantern.LIT)
  def isTop(world:IBlockAccess, pos:BlockPos) = getStateValue(world, pos, Lantern.TOP)
  def getStateValue(world:IBlockAccess, pos:BlockPos, prop:PropertyBool) = if(world.getBlockState(pos).getBlock.eq(this)) world.getBlockState(pos).getValue(prop).asInstanceOf[Boolean] else false
  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int = if(isLit(world, pos)) 15 else 0
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if (!world.isRemote) {
      val stack = player.getHeldItem(hand)
      val bd = (world, pos)
      if (!state.getValue(LIT).asInstanceOf[Boolean] && (stack != null && stack.getItem == Items.GLOWSTONE_DUST)) {
        bd.setState(state.withProperty(LIT, true.asInstanceOf[java.lang.Boolean]))
        bd.update
        bd.tile.asInstanceOf[TileLantern].burnTime = 1500
        stack.setCount(stack.getCount - 1)
        return true
      }
    }
    false
  }
  def setTop(bd:BlockData) = bd.setState(bd.state.withProperty(Lantern.TOP, (bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.UP) && !bd.world.isTouchingBlockSolidForSide(bd.pos, EnumFacing.DOWN)).asInstanceOf[java.lang.Boolean]))
  override def getBlockLayer = BlockRenderLayer.CUTOUT
  override def couldStay(bd:BlockData) = setTop(bd)
}

class LanternItem(block: Block) extends RMItemBlock(MOD_ID, BlockDefinitions.LANTERN, block) {
  override def addInformation(stack: ItemStack, player: World, list: java.util.List[String], flag: ITooltipFlag) {
    if (stack.hasTagCompound()) list.asInstanceOf[java.util.List[String]].add("Burning time left: " + (stack.getTagCompound.getInteger("time") / 2) + " seconds")
  }

  override def getSubItems(tab: CreativeTabs, stackList: NonNullList[ItemStack]) {
    if(!isInCreativeTab(tab)) return

    val lantern = new ItemStack(this, 1, BlockDefinitions.Lantern.ON)
    lantern.setTagCompound(new NBTTagCompound())
    lantern.getTagCompound.setInteger("time", 1500)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(lantern)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(this, 1, BlockDefinitions.Lantern.OFF))
  }
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos:BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, state:IBlockState): Boolean = {
    if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state)) {
      (world, pos).tile.asInstanceOf[TileLantern].burnTime = if (stack.hasTagCompound() && (stack.getItemDamage == BlockDefinitions.Lantern.ON)) stack.getTagCompound.getInteger("time") else 0
      return true
    }
    false
  }
}