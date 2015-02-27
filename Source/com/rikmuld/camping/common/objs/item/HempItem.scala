package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.Objs
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.rikmuld.corerm.common.objs.item.ItemMain
import com.rikmuld.camping.core.ModInfo

class HempItem(block: Block, infoClass: Class[_]) extends ItemMain(infoClass, Objs.tab, ModInfo.MOD_ID) {
  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, xx: Int, yy: Int, zz: Int, sideside: Int, partClickX: Float, partClickY: Float, partClickZ: Float): Boolean = {
    var x = xx
    var y = yy
    var z = zz
    var side = sideside

    val block = world.getBlock(x, y, z)
    if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) side = 1
    else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
      if (side == 0) y -= 1
      if (side == 1) y += 1
      if (side == 2) z -= 1
      if (side == 3) z += 1
      if (side == 4) x -= 1
      if (side == 5) x += 1
    }
    if (stack.stackSize == 0) false
    else if (!player.canPlayerEdit(x, y, z, side, stack)) false
    else if (y == 255 && this.block.getMaterial.isSolid) false
    else if (world.canPlaceEntityOnSide(this.block, x, y, z, false, side, player, stack)) {
      val i1 = this.getMetadata(stack.getItemDamage)
      val j1 = this.block.onBlockPlaced(world, x, y, z, side, partClickX, partClickY, partClickZ, i1)
      if (placeBlockAt(stack, player, world, x, y, z, side, partClickX, partClickY, partClickZ, j1)) {
        world.playSoundEffect((x.toFloat + 0.5F).toDouble, (y.toFloat + 0.5F).toDouble, (z.toFloat + 0.5F).toDouble, this.block.stepSound.func_150496_b(), (this.block.stepSound.getVolume + 1.0F) / 2.0F, this.block.stepSound.getPitch * 0.8F)
        stack.stackSize
      }
      true
    } else {
      false
    }
  }
  def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean = {
    if (!world.setBlock(x, y, z, block, metadata, 3)) return false
    if (world.getBlock(x, y, z) == block) {
      stack.stackSize -= 1;
      block.onBlockPlacedBy(world, x, y, z, player, stack)
      block.onPostBlockPlaced(world, x, y, z, metadata)
    }
    true
  }
}
