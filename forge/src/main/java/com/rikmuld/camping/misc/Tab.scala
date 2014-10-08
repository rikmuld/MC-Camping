package com.rikmuld.camping.misc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import com.rikmuld.camping.core.Objs

class Tab(name: String) extends CreativeTabs(name) {
  override def getIconItemStack: ItemStack = {
	new ItemStack(Objs.knife)
  }
  override def getTabIconItem: Item = {
    getIconItemStack.getItem
  }
}