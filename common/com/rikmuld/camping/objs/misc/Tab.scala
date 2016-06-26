package com.rikmuld.camping.objs.misc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import com.rikmuld.camping.objs.Objs._
import scala.collection.mutable.ListBuffer
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityList.EntityEggInfo
import net.minecraft.init.Items
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.item.ItemMonsterPlacer

class Tab(name: String) extends CreativeTabs(name) {
  val eggIds = new ListBuffer[String]

  override def getIconItemStack: ItemStack = new ItemStack(knife)
  override def getTabIconItem: Item = getIconItemStack.getItem
  
  @SideOnly(Side.CLIENT)
  override def displayAllRelevantItems(list: java.util.List[ItemStack]) {
    super.displayAllRelevantItems(list)
    val iterator = EntityList.ENTITY_EGGS.values.iterator()
    while (iterator.hasNext) {
      val entityegginfo = iterator.next().asInstanceOf[EntityEggInfo]
      
      val stack = new ItemStack(Items.SPAWN_EGG, 1)
      ItemMonsterPlacer.applyEntityIdToItemStack(stack, entityegginfo.spawnedID);

      if (eggIds.contains(entityegginfo.spawnedID)) list.asInstanceOf[java.util.List[ItemStack]].add(stack)
    }
  }
}