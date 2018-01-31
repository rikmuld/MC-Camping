//package com.rikmuld.camping.objs.blocks
//
//import com.rikmuld.camping.CampingMod._
//import com.rikmuld.camping.tileentity.TileLantern
//import com.rikmuld.camping.objs.BlockDefinitions
//import com.rikmuld.corerm.objs.ObjDefinition
//import com.rikmuld.corerm.objs.blocks._
//import com.rikmuld.corerm.objs.items.ItemBlockRM
//import net.minecraft.block.state.IBlockState
//import net.minecraft.block.{Block, BlockFence}
//import net.minecraft.client.util.ITooltipFlag
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.init.{Blocks, Items}
//import net.minecraft.item.ItemStack
//import net.minecraft.nbt.NBTTagCompound
//import net.minecraft.util._
//import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
//import net.minecraft.world.{IBlockAccess, World}
//import com.rikmuld.camping.misc.WorldBlock._
//import com.rikmuld.camping.objs.BlockDefinitions.Lantern
//
//import scala.collection.JavaConversions._
//
//class Lantern(modId: String, info: ObjDefinition) extends BlockSimple(modId, info) {
//  var burnTime: Option[Int] = None
//  var facing: EnumFacing = _
//
//  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, entity: EntityLivingBase, stack: ItemStack) =
//    setTop((world, pos))
//
//  override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
//    burnTime = if (Option((world, pos).tile).isDefined) Some((world, pos).tile.asInstanceOf[TileLantern].burnTime) else None
//    super.breakBlock(world, pos, state)
//  }
//
//  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, forture: Int): java.util.List[ItemStack] = {
//    val stack = new ItemStack(this, 1, BlockDefinitions.Lantern.OFF)
//    burnTime.map { time =>
//      if (time > 0) {
//        stack.setItemDamage(BlockDefinitions.Lantern.ON)
//        stack.setTagCompound(new NBTTagCompound())
//        stack.getTagCompound.setInteger("time", time)
//      }
//    }
//    List(stack)
//  }
//
//  override def canStay(world: World, pos: BlockPos): Boolean =
//    super.canStay(world, pos) || isTouchingBlockSolidForSide(world, pos, EnumFacing.UP)
//
//  override def dropIfCantStay(world: World, pos: BlockPos): Unit =
//    if(!super.dropIfCantStay(world, pos))
//      setTop(world, pos)
//
//  def setTop(bd:BlockData) = bd.setState(bd.state.withProperty(Lantern.TOP, (isTouchingBlockSolidForSide(bd.world, bd.pos, EnumFacing.UP) && !isTouchingBlockSolidForSide(bd.world, bd.pos, EnumFacing.DOWN)).asInstanceOf[java.lang.Boolean]))
//
//  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB =
//    if(isHanging(state))
//      new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 1, 0.7F)
//    else
//      new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 0.75625F, 0.7F)
//
//  def isLit(state: IBlockState): Boolean =
//    states.get.is(Lantern.STATE_LIT, true, state)
//
//  def isHanging(state: IBlockState): Boolean =
//    states.get.is(Lantern.STATE_HANGING, true, state)
//
//  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
//    if(isLit(state))
//      15
//    else
//      0
//
//  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
//    if (!world.isRemote) {
//      val stack = player.getHeldItem(hand)
//      val bd = (world, pos)
//      if (!isLit(state) && (stack != null && stack.getItem == Items.GLOWSTONE_DUST)) {
//        bd.setState(states.get.set(Lantern.STATE_LIT, true, state))
//        bd.update
//        bd.tile.asInstanceOf[TileLantern].burnTime = 1500
//        stack.setCount(stack.getCount - 1)
//        return true
//      }
//    }
//    false
//  }
//
//  override def getBlockLayer = BlockRenderLayer.CUTOUT
//
//  def isTouchingBlockSolidForSide(world: World, pos:BlockPos, facing: EnumFacing): Boolean = {
//    val bd = (world, pos.offset(facing))
//    val test =
//      bd.world.isSideSolid(bd.pos, facing.getOpposite) ||
//        bd.block.isInstanceOf[BlockFence]
//
//    bd.block != Blocks.AIR && test
//  }
//}
//
//class LanternItem(block: Block) extends ItemBlockRM(MOD_ID, BlockDefinitions.LANTERN, block) {
//  override def addInformation(stack: ItemStack, player: World, list: java.util.List[String], flag: ITooltipFlag) {
//    if (stack.hasTagCompound()) list.asInstanceOf[java.util.List[String]].add("Burning time left: " + (stack.getTagCompound.getInteger("time") / 2) + " seconds")
//  }
//
//  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos:BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, state:IBlockState): Boolean = {
//    if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state)) {
//      (world, pos).tile.asInstanceOf[TileLantern].burnTime = if (stack.hasTagCompound() && (stack.getItemDamage == BlockDefinitions.Lantern.ON)) stack.getTagCompound.getInteger("time") else 0
//      return true
//    }
//    false
//  }
//}