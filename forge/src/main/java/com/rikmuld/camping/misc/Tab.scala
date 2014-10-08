package com.rikmuld.camping.misc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class Tab(name: String) extends CreativeTabs(name) {
  override def getIconItemStack: ItemStack = {
	null;
  }
  override def getTabIconItem: Item = {
    null;
  }
}