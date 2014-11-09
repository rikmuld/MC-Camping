package com.rikmuld.camping.misc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import com.rikmuld.camping.core.Objs
import scala.collection.mutable.ListBuffer
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityList.EntityEggInfo
import net.minecraft.init.Items

class Tab(name: String) extends CreativeTabs(name) {
  val eggIds = new ListBuffer[Int]

  override def getIconItemStack: ItemStack = new ItemStack(Objs.knife)
  override def getTabIconItem: Item = getIconItemStack.getItem
  override def displayAllReleventItems(list: java.util.List[_]) {
    super.displayAllReleventItems(list)
    val iterator = EntityList.entityEggs.values.iterator()
    while (iterator.hasNext) {
      val entityegginfo = iterator.next().asInstanceOf[EntityEggInfo]
      if (eggIds.contains(entityegginfo.spawnedID)) list.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(Items.spawn_egg, 1, entityegginfo.spawnedID))
    }
  }
}