package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.SlotItemsNot
import com.rikmuld.camping.common.inventory.SlotItemsNot
import com.rikmuld.camping.common.inventory.SlotNoPickup
import com.rikmuld.camping.common.inventory.SlotNoPickup
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.core.ObjInfo
import com.rikmuld.camping.core.ObjInfo

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class Backpack(infoClass: Class[ObjInfo]) extends ItemMain(infoClass) {
  maxStackSize = 1
  setHasSubtypes(true)

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (!world.isRemote) player.openGui(CampingMod, GuiInfo.GUI_BACKPACK, world, 0, 0, 0)
    stack;
  }
}