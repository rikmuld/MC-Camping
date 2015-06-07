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
    
object Lantern {
  val LIT = PropertyBool.create("lit")
}

class Lantern(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable with WithProperties {
  var burnTime: Int = _

  setBlockBounds(0.3F, 0, 0.3F, 0.7F, 0.9F, 0.7F)
  setDefaultState(getStateFromMeta(0))
  
  override def getProps = Array(new RMBoolProp(LIT, 0))
  override def breakBlock(world: World, pos:BlockPos, state:IBlockState) {
    burnTime = if ((world, pos).tile != null) (world, pos).tile.asInstanceOf[TileLantern].burnTime else 0
    super.breakBlock(world, pos, state)
  }
  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = {
    ((world, pos).block == null || (world, pos).isReplaceable) && canStay((world, pos))
  }
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileLantern()
  override def getDrops(world:IBlockAccess, pos:BlockPos, state:IBlockState, forture:Int):java.util.List[ItemStack] = {
    val stack = new ItemStack(this, 1, getMetaFromState(state))
    stack.setTagCompound(new NBTTagCompound())
    stack.getTagCompound.setInteger("time", burnTime)
    List(stack)
  }
  override def damageDropped(state: IBlockState) = getMetaFromState(state)
  override def canStay(bd:BlockData) = bd.world.isTouchingBlockSolidForSideOrHasCorrectBounds(bd.pos, EnumFacing.UP, EnumFacing.DOWN)
  @SideOnly(Side.CLIENT)
  override def getBlockLayer = EnumWorldBlockLayer.CUTOUT
  override def getLightOpacity(world: IBlockAccess, pos:BlockPos): Int = {
    if (world.getBlockState(pos).getBlock == Objs.lantern&&getMetaFromState(world.getBlockState(pos))==MetaLookup.Lantern.ON) 255 else 0
  }
  override def getLightValue(world: IBlockAccess, pos:BlockPos): Int = if (getMetaFromState(world.getBlockState(pos))==MetaLookup.Lantern.ON) 15 else 0
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