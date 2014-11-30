package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.CampingMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.corerm.common.objs.item.ItemMain
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.ModInfo

class Backpack(infoClass: Class[_]) extends ItemMain(infoClass, Objs.tab, ModInfo.MOD_ID) {
  maxStackSize = 1
  setHasSubtypes(true)

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (!world.isRemote) player.openGui(CampingMod, GuiInfo.GUI_BACKPACK, world, 0, 0, 0)
    stack;
  }
}