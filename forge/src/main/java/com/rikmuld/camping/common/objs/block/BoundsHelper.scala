package com.rikmuld.camping.common.objs.block

import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import javax.swing.Icon
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.IBlockAccess
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import java.util.Random
import net.minecraft.entity.Entity
import net.minecraft.block.Block
import com.rikmuld.camping.common.objs.tile.TileEntityWithBounds
import net.minecraft.init.Blocks
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation
import net.minecraft.util.IIcon

object BoundsHelper {
  def isBlockHeadOfBed(meta: Int): Boolean = true
}

class BoundsHelper(infoClass: Class[_]) extends BlockMain(infoClass, Material.air) with BlockWithModel {
  setCreativeTab(null)

  override def addCollisionBoxesToList(world: World, x: Int, y: Int, z: Int, alignedBB: AxisAlignedBB, list: java.util.List[_], entity: Entity) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    if (tile.bounds != null) tile.bounds.setBlockCollision(this)
    super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity)
  }
  override def breakBlock(world: World, x: Int, y: Int, z: Int, par5: Block, par6: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    if (world.getBlock(tile.baseX, tile.baseY, tile.baseZ) != Blocks.air) {
      world.getBlock(tile.baseX, tile.baseY, tile.baseZ).breakBlock(world, tile.baseX, tile.baseY, tile.baseZ, par5, par6)
    }
    super.breakBlock(world, x, y, z, par5, par6)
  }
  override def createNewTileEntity(world: World, meta:Int): TileEntity = new TileEntityWithBounds()
  override def getBedDirection(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    world.getTileEntity(tile.baseX, tile.baseY, tile.baseZ).asInstanceOf[TileEntityWithRotation].rotation
  }
  override def getBlockHardness(world: World, x: Int, y: Int, z: Int): Float = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    if (world.getBlock(tile.baseX, tile.baseY, tile.baseZ) != Blocks.air) return world.getBlock(tile.baseX, tile.baseY, tile.baseZ).getBlockHardness(world, x, y, z)
    blockHardness
  }
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.wool.getIcon(0, 0)
  override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    world.getBlock(tile.baseX, tile.baseY, tile.baseZ).getPickBlock(target, world, x, y, z)
  }
  override def isBed(world: IBlockAccess, x: Int, y: Int, z: Int, player: EntityLivingBase): Boolean = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    world.getBlock(tile.baseX, tile.baseY, tile.baseZ).isBed(world, tile.baseX, tile.baseY, tile.baseZ, player)
  }
  override def isBedFoot(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = false
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, par6: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    world.getBlock(tile.baseX, tile.baseY, tile.baseZ).onBlockActivated(world, tile.baseX, tile.baseY, tile.baseZ, player, par6, par7, par8, par9)
  }
  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    if (world.getBlock(tile.baseX, tile.baseY, tile.baseZ) != Blocks.air) {
      world.getBlock(tile.baseX, tile.baseY, tile.baseZ).onNeighborBlockChange(world, tile.baseX, tile.baseY, tile.baseZ, block)
    }
  }
  override def quantityDropped(par1Random: Random): Int = 0
  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithBounds]
    if (tile.bounds != null) tile.bounds.setBlockBounds(this)
  }
}