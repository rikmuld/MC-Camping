package com.rikmuld.camping.common.objs.block

import com.rikmuld.camping.common.objs.item.ItemBlockMain
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation
import com.rikmuld.camping.core.SleepingBagInfo
import com.rikmuld.camping.core.Utils._
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

class SleepingBag(infoClass: Class[_]) extends BlockMain(infoClass, Material.cloth, classOf[SleepigBagItem].asInstanceOf[Class[ItemBlock]], false, true) with BlockWithModel with BlockWithInstability with BlockWithRotation{  
  setBlockBounds(0, 0, 0, 1, 0.0625f, 1)
  
  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null
  override def damageDropped(par1: Int): Int = 0
  override def createNewTileEntity(world: World, meta: Int): TileEntity = new TileEntityWithRotation
}

class SleepigBagItem(block: Block) extends ItemBlockMain(block, classOf[SleepingBagInfo]) {
  setMaxStackSize(4)
  
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, tab: CreativeTabs, list: java.util.List[_]) = list.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(item, 1, 0))
}