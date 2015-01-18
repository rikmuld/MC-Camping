package com.rikmuld.camping.common.objs.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import com.rikmuld.camping.core.Objs
import com.rikmuld.corerm.common.objs.item.ItemMain
import com.rikmuld.camping.core.ModInfo
import com.rikmuld.corerm.misc.Rotation

class Knife(infoClass: Class[_]) extends ItemMain(infoClass, Objs.tab, ModInfo.MOD_ID) with IKnife {
  setHasSubtypes(true)
  setMaxDamage(Objs.config.toolUse)
  setMaxStackSize(1)

  override def onItemUse(item: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, sideHit: Int, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    !world.isRemote && Rotation.rotateBlock(world, x, y, z)
  }
}

trait IKnife {}