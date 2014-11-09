package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.ObjInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import com.rikmuld.camping.misc.Rotation
import com.rikmuld.camping.core.Objs

class Knife(infoClass: Class[_]) extends ItemMain(infoClass) with IKnife {
  setHasSubtypes(true)
  setMaxDamage(Objs.config.toolUse)
  setMaxStackSize(1)

  override def onItemUse(item: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, sideHit: Int, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    !world.isRemote && Rotation.rotateBlock(world, x, y, z)
  }
}

trait IKnife {}