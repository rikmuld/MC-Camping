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

class Light(infoClass: Class[ObjInfo]) extends BlockMain(infoClass, Material.air) with BlockWithModel {
  setLightLevel(1.0F)
  setBlockBounds(0F, 0F, 0F, 0F, 0F, 0F)
  setCreativeTab(null)

  override def createTileEntity(world: World, meta: Int): TileEntity = new TileEntityLight()
  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null
}