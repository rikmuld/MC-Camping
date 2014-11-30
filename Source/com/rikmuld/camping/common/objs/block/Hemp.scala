package com.rikmuld.camping.common.objs.block

import java.util.ArrayList
import java.util.Random
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.Utils._
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.util.ForgeDirection
import cpw.mods.fml.relauncher.Side
import com.rikmuld.corerm.common.objs.block.BlockMain
import com.rikmuld.corerm.common.objs.block.BlockWithModel
import com.rikmuld.camping.core.ModInfo

class Hemp(infoClass: Class[_]) extends BlockMain(infoClass, Objs.tab, ModInfo.MOD_ID, Material.plants) with IPlantable with BlockWithModel {
  setTickRandomly(true)

  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean = {
    val block = world.getBlock(x, y - 1, z)
    block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this)
  }
  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) = this.dropIfCantStay(world, x, y, z)
  protected def dropIfCantStay(world: World, x: Int, y: Int, z: Int): Boolean = {
    if (!this.canBlockStay(world, x, y, z)) {
      this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0)
      world.setBlockToAir(x, y, z)
      false
    } else true
  }
  override def canBlockStay(world: World, x: Int, y: Int, z: Int): Boolean = this.canPlaceBlockAt(world, x, y, z)
  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null
  override def getItemDropped(par1: Int, random: Random, par2: Int): Item = Objs.hempItem
  override def getRenderType(): Int = 1
  @SideOnly(Side.CLIENT)
  override def getItem(world: World, x: Int, y: Int, z: Int): Item = Objs.hempItem
  override def getPlantType(world: IBlockAccess, x: Int, y: Int, z: Int): EnumPlantType = EnumPlantType.Beach
  override def getPlant(world: IBlockAccess, x: Int, y: Int, z: Int): Block = this
  override def getPlantMetadata(world: IBlockAccess, x: Int, y: Int, z: Int): Int = world.getBlockMetadata(x, y, z)
  override def setBlockBoundsBasedOnState(iBlockAccess: IBlockAccess, x: Int, y: Int, z: Int) {
    val metadata = iBlockAccess.getBlockMetadata(x, y, z)
    if (metadata == 4) setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F)
    else if (metadata == 3) setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.8F, 0.7F)
    else if ((metadata == 2) || (metadata == 5)) setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F)
    else if (metadata == 1) setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F)
    else if (metadata == 0) setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.2F, 0.7F)
  }
  override def updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
    super.updateTick(world, x, y, z, random)
    if (world.getBlockLightValue(x, y + 1, z) >= 9) {
      var meta = world.getBlockMetadata(x, y, z)
      val speed = Math.min(25, Math.max(1, getGrowthRate(world, x, y, z)))
      if (meta < 3) {
        if (random.nextInt((25.0F / speed).toInt + 1) == 0) {
          meta += 1
          world.setBlockMetadataWithNotify(x, y, z, meta, 2)
        }
      } else if (meta == 3) {
        if (random.nextInt((25.0F / speed).toInt + 1) == 0) {
          if (world.getBlock(x, y + 1, z) == Blocks.air) {
            world.setBlock(x, y + 1, z, this, 5, 2)
            world.setBlockMetadataWithNotify(x, y, z, 4, 2)
          }
        }
      }
    }
  }
  def getGrowthRate(world: World, x: Int, y: Int, z: Int): Float = {
    var water = if (world.getBlock(x + 1, y - 1, z).getMaterial() == Material.water) 1 else 0
    water += (if (world.getBlock(x - 1, y - 1, z).getMaterial() == Material.water) 1 else 0)
    water += (if (world.getBlock(x, y - 1, z + 1).getMaterial() == Material.water) 1 else 0)
    water += (if (world.getBlock(x, y - 1, z - 1).getMaterial() == Material.water) 1 else 0)
    var light = Math.max(1, (world.getBlockLightValue(x, y + 1, z) - 9) / 3f)
    var ground = if (world.getBlock(x, y - 1, z) == Blocks.grass || world.getBlock(x, y - 1, z) == Blocks.dirt) 2 else 1
    ground * water * light * Objs.config.hempSpeed;
  }
  override def getDrops(world: World, x: Int, y: Int, z: Int, meta: Int, fortune: Int): ArrayList[ItemStack] = {
    val retVal = super.getDrops(world, x, y, z, meta, fortune)
    if (meta > 3) retVal else new ArrayList[ItemStack];
  }
  def grow(world: World, x: Int, y: Int, z: Int): Boolean = {
    var grow = 0
    if (world.getBlockMetadata(x, y, z) < 3) {
      grow = new Random().nextInt(3 - world.getBlockMetadata(x, y, z)) + 1
      world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + grow, 2)
      true
    } else if (world.getBlockMetadata(x, y, z) == 3) {
      if (new Random().nextInt(3) == 0) {
        world.setBlock(x, y + 1, z, this, 5, 2)
        world.setBlockMetadataWithNotify(x, y, z, 4, 2)
      }
      true
    } else false
  }
}