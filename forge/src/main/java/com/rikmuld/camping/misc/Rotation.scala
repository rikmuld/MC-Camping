package com.rikmuld.camping.misc

import net.minecraft.block.Block
import scala.collection.mutable.HashMap
import net.minecraft.init.Blocks
import scala.actors.threadpool.Arrays
import net.minecraft.world.World
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation

object Rotation {

  private type Rotation = (World, Int, Int, Int, Block) => _
  private type RotationMeta = (World, Int, Int, Int, Block) => Array[Int]

  val blocksRot: HashMap[Block, List[Int]] = HashMap(
    (Blocks.brick_stairs -> List(8, 1, 0)),
    (Blocks.acacia_stairs -> List(8, 1, 0)),
    (Blocks.dark_oak_stairs -> List(8, 1, 0)),
    (Blocks.stone_stairs -> List(8, 1, 0)),
    (Blocks.stone_brick_stairs -> List(8, 1, 0)),
    (Blocks.nether_brick_stairs -> List(8, 1, 0)),
    (Blocks.quartz_stairs -> List(8, 1, 0)),
    (Blocks.sandstone_stairs -> List(8, 1, 0)),
    (Blocks.birch_stairs -> List(8, 1, 0)),
    (Blocks.jungle_stairs -> List(8, 1, 0)),
    (Blocks.oak_stairs -> List(8, 1, 0)),
    (Blocks.spruce_stairs -> List(8, 1, 0)),
    (Blocks.chest -> List(6, 1, 2)),
    (Blocks.trapped_chest -> List(6, 1, 2)),
    (Blocks.log -> List(16, 4, -1)),
    (Blocks.log2 -> List(16, 4, -1)),
    (Blocks.pumpkin -> List(4, 1, 0)),
    (Blocks.dispenser -> List(6, 1, 0)),
    (Blocks.dropper -> List(6, 1, 0)),
    (Blocks.piston -> List(-1, -1, -1)),
    (Blocks.sticky_piston -> List(-1, -1, -1)),
    (Blocks.ender_chest -> List(6, 1, 2)),
    (Blocks.anvil -> List(-1, -1, -1)),
    (Blocks.standing_sign -> List(16, 1, 0)),
    (Blocks.lit_pumpkin -> List(4, 1, 0)),
    (Blocks.powered_comparator -> List(-1, -1, -1)),
    (Blocks.powered_repeater -> List(-1, -1, -1)),
    (Blocks.unpowered_comparator -> List(-1, -1, -1)),
    (Blocks.unpowered_repeater -> List(-1, -1, -1)),
    (Blocks.furnace -> List(6, 1, 2)),
    (Blocks.lit_furnace -> List(6, 1, 2)),
    (Blocks.fence_gate -> List(-1, -1, -1)))

  def hasRotation(block: Block): Boolean = blocksRot.contains(block)
  private def getList(start: Int, end: Int): Array[Int] = {
    val list = new Array[Int](end - start)
    for (i <- 0 to list.length - 2) list(i) = start + i
    list
  }
  def rotateBlock(world: World, x: Int, y: Int, z: Int): Boolean = {
    val block = world.getBlock(x, y, z)
    if (hasRotation(block)) {
      getRotationType(block)(world, x, y, z, block)
      true
    } else if (world.getTileEntity(x, y, z).isInstanceOf[TileEntityWithRotation]) world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].cycleRotation()
    false
  }
  private def getRotationType(block: Block): Rotation = block match {
    case Blocks.anvil => rotAnvil
    case Blocks.piston | Blocks.sticky_piston => rotPistion
    case Blocks.fence_gate | Blocks.powered_repeater | Blocks.unpowered_repeater | Blocks.powered_comparator | Blocks.unpowered_comparator => rotStateQuad
    case _ => rotBasic
  }
  private def rotPistion(world: World, x: Int, y: Int, z: Int, block: Block) {
    if (world.getBlockMetadata(x, y, z) > 4) world.setBlockMetadataWithNotify(x, y, z, 0, 2)
    else world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + 1, 2)
    val meta = world.getBlockMetadata(x, y, z)

    world.setBlock(x, y, z, Blocks.air)
    world.notifyBlocksOfNeighborChange(x, y, z, block)
    world.setBlock(x, y, z, block, meta, 2)
    world.notifyBlockOfNeighborChange(x, y, z, block)
  }
  private def rotAnvil(world: World, x: Int, y: Int, z: Int, block: Block) {
    val meta = world.getBlockMetadata(x, y, z)
    if ((meta == 0) || ((meta % 2) == 0)) world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2)
    else world.setBlockMetadataWithNotify(x, y, z, meta - 1, 2)
  }
  private def rotStateQuad(world: World, x: Int, y: Int, z: Int, block: Block) {
    val meta = world.getBlockMetadata(x, y, z)
    if ((meta == 3) || ((meta % 4) == 3)) world.setBlockMetadataWithNotify(x, y, z, meta - 3, 2)
    else world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2)
  }
  private def rotBasic(world: World, x: Int, y: Int, z: Int, block: Block) {
    val rotData = blocksRot.get(block)
    if ((world.getBlockMetadata(x, y, z) > rotData.get(0) - 2) && (rotData.get(2).!=(-1))) world.setBlockMetadataWithNotify(x, y, z, rotData.get(2), 2)
    else world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) + rotData.get(1), 2)
  }
  def getMetas(world: World, x: Int, y: Int, z: Int): Array[Int] = {
    val block = world.getBlock(x, y, z)
    if (hasRotation(block)) {
      return getRotationMetaType(block)(world, x, y, z, block)
    }
    null
  }
  private def getRotationMetaType(block: Block): RotationMeta = block match {
    case Blocks.anvil => rotMetaAnvil
    case Blocks.piston | Blocks.sticky_piston => rotMetaPistion
    case Blocks.fence_gate | Blocks.powered_repeater | Blocks.unpowered_repeater | Blocks.powered_comparator | Blocks.unpowered_comparator => rotMetaStateQuad
    case Blocks.log | Blocks.log2 => rotMetaLog
    case _ => rotMetaBasic
  }
  private def rotMetaPistion(world: World, x: Int, y: Int, z: Int, block: Block): Array[Int] = getList(0, 6)
  private def rotMetaAnvil(world: World, x: Int, y: Int, z: Int, block: Block): Array[Int] = {
    val meta = world.getBlockMetadata(x, y, z)
    for (i <- 0 to 5 if meta < 2 + i * 2) return getList(i * 2, i * 2 + 2)
    null
  }
  private def rotMetaStateQuad(world: World, x: Int, y: Int, z: Int, block: Block): Array[Int] = {
    val meta = world.getBlockMetadata(x, y, z)
    for (i <- 0 to 3 if meta < 4 + i * 4) return getList(i * 4, i * 4 + 4)
    null
  }
  private def rotMetaLog(world: World, x: Int, y: Int, z: Int, block: Block): Array[Int] = {
    val meta = world.getBlockMetadata(x, y, z)
    for (i <- 0 to 3 if meta % 4 == i) return Array[Int](i, 4 + i, 8 + i, 12 + i)
    null
  }
  private def rotMetaBasic(world: World, x: Int, y: Int, z: Int, block: Block): Array[Int] = {
    val rotData = blocksRot.get(block)
    getList(rotData.get(2), rotData.get(0))
  }
}