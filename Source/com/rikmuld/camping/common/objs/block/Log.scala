package com.rikmuld.camping.common.objs.block

import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.init.Items
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.LanternInfo
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.item.Item
import cpw.mods.fml.relauncher.Side
import net.minecraft.util.Vec3
import net.minecraft.util.AxisAlignedBB
import com.rikmuld.camping.core.ObjRegistry
import net.minecraft.util.IIcon
import net.minecraft.entity.Entity
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.objs.tile.TileEntityLog
import com.rikmuld.corerm.common.objs.block.BlockWithRotation
import com.rikmuld.corerm.common.objs.block.BlockMain
import com.rikmuld.corerm.common.objs.block.BlockWithModel
import com.rikmuld.camping.core.ModInfo
import com.rikmuld.corerm.common.network.PacketSender
import com.rikmuld.camping.common.network.PlayerExitLog

class Log(infoClass: Class[_]) extends BlockMain(infoClass, Objs.tab, ModInfo.MOD_ID, Material.wood, false, false) with BlockWithModel with BlockWithRotation {
  setStepSound(Block.soundTypeWood)
  setHardness(2.0F)
  setHarvestLevel("axe", 0)

  override def addCollisionBoxesToList(world: World, x: Int, y: Int, z: Int, par5AxisAlignedBB: AxisAlignedBB, par6List: java.util.List[_], par7Entity: Entity) {
    setBlockBoundsBasedOnState(world, x, y, z)
    super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity)
  }
  override def createTileEntity(world: World, meta: Int): TileEntity = new TileEntityLog()
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.log.getIcon(0, 0)
  override def isWood(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = true
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (!player.isRiding && (Vec3.createVectorHelper(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ)) <= 2.5F)) {
      world.getTileEntity(x, y, z).asInstanceOf[TileEntityLog].mountable.interactFirst(player)
    }
    true
  }
  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
    val log = world.getTileEntity(x, y, z).asInstanceOf[TileEntityLog]
    val meta = (log.rotation + 1) % 2
    var xMin = 0.3125F
    var yMin = 0
    var zMin = 0.3125F
    var xMax = 0.6875F
    var yMax = 0.375F
    var zMax = 0.6875F
    if (meta == 0) {
      zMax = 0.875F
      zMin = 0.125F
      if (world.getBlock(x, y, z - 1) == Objs.log && (((world.getTileEntity(x, y, z - 1).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 0)) zMin = 0
      if (world.getBlock(x, y, z + 1) == Objs.log && (((world.getTileEntity(x, y, z + 1).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 0)) zMax = 1
    }
    if (meta == 1) {
      xMax = 0.875F
      xMin = 0.125F
      if (world.getBlock(x - 1, y, z) == Objs.log && (((world.getTileEntity(x - 1, y, z).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 1)) xMin = 0
      if (world.getBlock(x + 1, y, z) == Objs.log && (((world.getTileEntity(x + 1, y, z).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 1)) xMax = 1
    }
    setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax)
  }
}
