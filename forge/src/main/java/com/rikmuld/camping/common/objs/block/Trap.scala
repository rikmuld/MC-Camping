package com.rikmuld.camping.common.objs.block

import com.rikmuld.camping.core.ObjInfo
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
import com.rikmuld.camping.common.objs.item.ItemBlockMain
import net.minecraft.item.ItemBlock
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.LanternInfo
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.item.Item
import cpw.mods.fml.relauncher.Side
import net.minecraft.util.AxisAlignedBB
import com.rikmuld.camping.common.objs.tile.TileEntityLight
import javax.swing.Icon
import net.minecraft.util.IIcon
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.common.objs.tile.TileEntityTrap

class Trap(infoClass: Class[_]) extends BlockMain(infoClass, Material.iron, false, false) with BlockWithModel with BlockWithInstability{
  setBlockBounds(0.21875F, 0, 0.21875F, 0.78125F, 0.1875F, 0.78125F)
  
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.iron_block.getIcon(0, 0)
  override def createTileEntity(world: World, meta: Int): TileEntity = new TileEntityTrap()
  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTrap]
    if (!tile.open) {
      tile.open = true
      tile.cooldown = 10
      tile.trappedEntity = null
      true
    } else {
      player.openGui(CampingMod, GuiInfo.GUI_TRAP, world, x, y, z)
      true
    }
  }
  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTrap]
    if (tile.open) setBlockBounds(0.21875F, 0, 0.21875F, 0.78125F, 0.1875F, 0.78125F)
    else setBlockBounds(0.21875F, 0, 0.34375F, 0.78125F, 0.25F, 0.65F)
  }
}