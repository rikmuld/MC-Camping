package com.rikmuld.camping.common.objs.block

import java.util.Random
import scala.collection.JavaConversions.asScalaBuffer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.corerm.common.network.PacketSender
import com.rikmuld.corerm.common.objs.item.ItemBlockMain
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TentInfo
import com.rikmuld.corerm.core.CoreUtils._
import com.rikmuld.camping.misc.BoundsTracker
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.IIcon
import net.minecraft.util.MathHelper
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import java.util.ArrayList
import com.rikmuld.corerm.common.objs.tile.TileEntityMain
import com.rikmuld.corerm.common.objs.tile.TileEntityWithRotation
import com.rikmuld.corerm.core.ObjInfo
import com.rikmuld.corerm.common.objs.block.BlockWithInstability
import com.rikmuld.corerm.common.objs.block.BlockWithRotation
import com.rikmuld.corerm.common.objs.block.BlockMain
import com.rikmuld.corerm.common.objs.block.BlockWithModel
import com.rikmuld.camping.core.ModInfo

object Tent {
  def isBlockHeadOfBed(meta: Int): Boolean = true
}

class Tent(infoClass: Class[_]) extends BlockMain(infoClass, Objs.tab, ModInfo.MOD_ID, Material.cloth, null, false, false) with BlockWithModel with BlockWithRotation with BlockWithInstability {
  var color: Int = _
  var rotationYaw: Float = 0
  setHardness(0.2F)

  override def addCollisionBoxesToList(world: World, x: Int, y: Int, z: Int, alignedBB: AxisAlignedBB, list: java.util.List[_], entity: Entity) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent]
    TileEntityTent.bounds(tile.rotation).setBlockCollision(this)
    super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity)
  }
  override def breakBlock(world: World, x: Int, y: Int, z: Int, par5: Block, par6: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent]
    if (tile != null) {
      color = tile.color
      world.dropBlockItems(x, y, z, new Random())
      tile.structures(tile.rotation).destroyStructure(world, tile.tracker(tile.rotation))
      super.breakBlock(world, x, y, z, par5, par6)
      if (!tile.dropped) {
        val stacks = new ArrayList[ItemStack]
        dropBlockAsItem(world, x, y, z, new ItemStack(this))
        stacks.addAll(tile.getContends)
        world.dropItemsInWorld(stacks, x, y, z, new Random())
        tile.dropped = true
      }
    }
    world.setBlock(x, y, z, Blocks.air)
  }
  override def canPlaceBlockAt(world: World, x: Int, y: Int, z: Int): Boolean = {
    val block = world.getBlock(x, y, z)
    var direction = 0
    val facing = MathHelper.floor_double(((rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3
    if (facing == 0) direction = ForgeDirection.NORTH.ordinal() - 2
    else if (facing == 1) direction = ForgeDirection.SOUTH.ordinal() - 2
    else if (facing == 2) direction = ForgeDirection.WEST.ordinal() - 2
    else if (facing == 3) direction = ForgeDirection.EAST.ordinal() - 2
    ((block == null) || block.isReplaceable(world, x, y, z)) && Objs.tentStructure(direction).canBePlaced(world, new BoundsTracker(x, y, z, TileEntityTent.bounds(direction)))
  }
  override def createNewTileEntity(world: World, meta: Int): TileEntityMain = new TileEntityTent()
  protected override def dropBlockAsItem(world: World, x: Int, y: Int, z: Int, stack: ItemStack) {
    if (stack != null) {
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound.setInteger("color", color)
    }
    super.dropBlockAsItem(world, x, y, z, stack)
  }
  override def dropIfCantStay(world: World, x: Int, y: Int, z: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent]
    if ((tile.structures != null) &&
      !tile.structures(tile.rotation).hadSolidUnderGround(world, tile.tracker(tile.rotation))) {
      breakBlock(world, x, y, z, this, 0)
    }
  }
  override def getBedDirection(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].rotation
  }
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.wool.getIcon(0, 0)
  override def getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent]
    if ((tile.lanternDamage == 0) && (tile.lanterns > 0)) 15 else 0
  }
  override def isBed(world: IBlockAccess, x: Int, y: Int, z: Int, player: EntityLivingBase): Boolean = true
  override def isBedFoot(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = !Tent.isBlockHeadOfBed(world.getBlockMetadata(x, y, z))
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (!world.isRemote) {
      if ((player.getCurrentEquippedItem != null) && world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent].addContends(player.getCurrentEquippedItem)) {
        player.getCurrentEquippedItem.stackSize -= 1
        if (player.getCurrentEquippedItem.stackSize < 0) player.setCurrentItem(null)
        return true
      } else if ((player.getCurrentEquippedItem != null) && (player.getCurrentEquippedItem.getItem() == Items.dye) && (world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent].color != player.getCurrentEquippedItem.getItemDamage)) {
        world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent].setColor(player.getCurrentEquippedItem.getItemDamage)
        player.getCurrentEquippedItem.stackSize -= 1
        return true
      } else {
        PacketSender.to(new OpenGui(GuiInfo.GUI_TENT, x, y, z), player)
        return true
      }
    }
    true
  }
  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entityLiving: EntityLivingBase, itemStack: ItemStack) {
    super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack)
    world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent].setColor(if (itemStack.hasTagCompound()) itemStack.getTagCompound.getInteger("color") else 15)
  }
  override def quantityDropped(random: Random): Int = 0
  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityTent]
    TileEntityTent.bounds(tile.rotation).setBlockBounds(this)
  }
}