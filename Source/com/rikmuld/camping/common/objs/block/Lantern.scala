package com.rikmuld.camping.common.objs.block

import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.block.Block
import com.rikmuld.corerm.core.CoreUtils._
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.init.Items
import net.minecraft.init.Blocks
import com.rikmuld.corerm.common.objs.item.ItemBlockMain
import net.minecraft.item.ItemBlock
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.LanternInfo
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.item.Item
import cpw.mods.fml.relauncher.Side
import com.rikmuld.corerm.core.ObjInfo
import com.rikmuld.corerm.common.objs.block.BlockWithInstability
import com.rikmuld.corerm.common.objs.block.BlockMain
import com.rikmuld.corerm.common.objs.block.BlockWithModel
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.ModInfo

class Lantern(infoClass: Class[_]) extends BlockMain(infoClass, Objs.tab, ModInfo.MOD_ID, Material.glass, classOf[LanternItem].asInstanceOf[Class[ItemBlock]], false, true) with BlockWithModel with BlockWithInstability {
  var burnTime: Int = _
  setBlockBounds(0.3F, 0, 0.3F, 0.7F, 0.9F, 0.7F)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, par6: Int) {
    burnTime = if (world.getTileEntity(x, y, z) != null) world.getTileEntity(x, y, z).asInstanceOf[TileEntityLantern].burnTime else 0
    super.breakBlock(world, x, y, z, block, par6)
  }
  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean = {
    val block = world.getBlock(x, y, z)
    ((block == null) || block.isReplaceable(world, x, y, z)) && world.isTouchableBlockPartitionalSolidForSideOrHasCorrectBounds(x, y, z, 0, 1)
  }
  override def createNewTileEntity(world: World, meta: Int): TileEntity = new TileEntityLantern()
  protected override def dropBlockAsItem(world: World, x: Int, y: Int, z: Int, stack: ItemStack) {
    if (!world.isRemote && world.getGameRules.getGameRuleBooleanValue("doTileDrops")) {
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound.setInteger("time", burnTime)
      val f = 0.7F
      val d0 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D)
      val d1 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D)
      val d2 = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D)
      val entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack)
      entityitem.delayBeforeCanPickup = 10
      world.spawnEntityInWorld(entityitem)
    }
  }
  override def damageDropped(meta: Int) = meta
  override def dropIfCantStay(world: World, x: Int, y: Int, z: Int) {
    if (!world.isTouchableBlockPartitionalSolidForSideOrHasCorrectBounds(x, y, z, 0, 1)) {
      breakBlock(world, x, y, z, this, 0)
      for (item <- this.getDrops(world, x, y, z, damageDropped(world.getBlockMetadata(x, y, z)), 1)) {
        dropBlockAsItem(world, x, y, z, item)
      }
      world.setBlock(x, y, z, Blocks.air)
    }
  }
  override def getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int = if (world.getBlockMetadata(x, y, z) == 0) 15 else 0
  override def getRenderType(): Int = 1
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (!world.isRemote) {
      if ((world.getBlockMetadata(x, y, z) == 1) && (player.getCurrentEquippedItem != null && player.getCurrentEquippedItem.getItem == Items.glowstone_dust)) {
        world.setBlockMetadataWithNotify(x, y, z, 0, 2)
        world.markBlockForUpdate(x, y, z)
        world.getTileEntity(x, y, z).asInstanceOf[TileEntityLantern].burnTime = 1500
        return true
      }
    }
    false
  }
}

class LanternItem(block: Block) extends ItemBlockMain(block, classOf[LanternInfo].asInstanceOf[Class[ObjInfo]]) {
  setMaxStackSize(1);

  override def addInformation(stack: ItemStack, player: EntityPlayer, list: java.util.List[_], par4: Boolean) {
    if (stack.hasTagCompound()) list.asInstanceOf[java.util.List[String]].add("Burning time left: " + (stack.getTagCompound.getInteger("time") / 2) + " seconds")
  }
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, creativetabs: CreativeTabs, stackList: java.util.List[_]) {
    val lantern = new ItemStack(item, 1, LanternInfo.LANTERN_ON)
    lantern.setTagCompound(new NBTTagCompound())
    lantern.getTagCompound.setInteger("time", 1500)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(lantern)
    stackList.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(item, 1, LanternInfo.LANTERN_OFF))
  }
  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean = {
    if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
      world.getTileEntity(x, y, z).asInstanceOf[TileEntityLantern].burnTime = if (stack.hasTagCompound() && (stack.getItemDamage == LanternInfo.LANTERN_ON)) stack.getTagCompound.getInteger("time") else 0
      return true
    }
    false
  }
}