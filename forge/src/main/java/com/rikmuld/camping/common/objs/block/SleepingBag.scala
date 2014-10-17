package com.rikmuld.camping.common.objs.block

import java.util.Random
import com.rikmuld.camping.common.objs.item.ItemBlockMain
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation
import com.rikmuld.camping.common.objs.tile.TileEntitySleepingBag
import com.rikmuld.camping.core.SleepingBagInfo
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.Utils._
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.EnumStatus
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import tv.twitch.chat.ChatTextMessageToken
import net.minecraft.util.ChatComponentText
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.common.objs.tile.TileEntitySleepingBag
import net.minecraft.init.Blocks
import net.minecraft.util.ChatComponentTranslation

object SleepingBag {
  def isBlockHeadOfBed(meta: Int): Boolean = meta == 0
}

class SleepingBag(infoClass: Class[_]) extends BlockMain(infoClass, Material.cloth, classOf[SleepigBagItem].asInstanceOf[Class[ItemBlock]], false, true) with BlockWithModel with BlockWithInstability with BlockWithRotation{  
  setBlockBounds(0, 0, 0, 1, 0.0625f, 1)
  setStepSound(Block.soundTypeCloth)
  setHardness(0.5F)
  
  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB = null
  override def damageDropped(par1: Int): Int = 0
  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    if (meta == 1) {
      if(world.getTileEntity(x, y, z)!=null)world.getTileEntity(x, y, z).asInstanceOf[TileEntitySleepingBag].breakHead()
    } else {
      world.dropItemInWorld(new ItemStack(this, 1, 0), x, y, z, new Random())
    }
    super.breakBlock(world, x, y, z, block, meta)
  }
  override def createNewTileEntity(world: World, meta: Int): TileEntity = new TileEntitySleepingBag()
  override def getBedDirection(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    val rotation = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].rotation
    if ((rotation + 2) < 4) rotation + 2 else (if ((rotation + 2) < 5) 0 else 1)
  }
  override def isBed(world: IBlockAccess, x: Int, y: Int, z: Int, player: EntityLivingBase): Boolean = true
  override def isBedFoot(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = !SleepingBag.isBlockHeadOfBed(world.getBlockMetadata(x, y, z))
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (!world.isRemote) {
      val rotation = world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].rotation
      val xPos = if (rotation == 1) x + 1 else if (rotation == 3) x - 1 else x
      val zPos = if (rotation == 0) z - 1 else if (rotation == 2) z + 1 else z
      if (world.getBlockMetadata(x, y, z) == 1) return onBlockActivated(world, xPos, y, zPos, player, 
        side, par7, par8, par9) else if (world.getTileEntity(x, y, z).asInstanceOf[TileEntitySleepingBag].sleepingPlayer == null) {
        val state = player.sleepInBedAt(x, y, z)
        if (state == EnumStatus.OK) {
          world.getTileEntity(x, y, z).asInstanceOf[TileEntitySleepingBag].sleepingPlayer = player
          return true
        } else {
          if (state == EnumStatus.NOT_POSSIBLE_NOW) {
            player.addChatMessage(new ChatComponentTranslation("tile.bed.noSleep", new java.lang.Object))
          } else if (state == EnumStatus.NOT_SAFE) {
            player.addChatMessage(new ChatComponentTranslation("tile.bed.noSafe", new java.lang.Object))
          }
          return true
        }
      } else {
        player.addChatMessage(new ChatComponentText("This sleeping bag is occupied!"))
      }
    }
    true
  }
  override def quantityDropped(random: Random): Int = 0
}

class SleepigBagItem(block: Block) extends ItemBlockMain(block, classOf[SleepingBagInfo]) {
  setMaxStackSize(4)
  
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, tab: CreativeTabs, list: java.util.List[_]) = list.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(item, 1, 0))
}