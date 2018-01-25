package com.rikmuld.camping.objs.misc

import com.rikmuld.camping.objs.Objs._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.{ItemMonsterPlacer, ItemStack}
import net.minecraft.util.{NonNullList, ResourceLocation}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable.ListBuffer

object TabData {
  val eggIds = new ListBuffer[ResourceLocation]
}

class Tab(name: String) extends CreativeTabs(name) {
  override def getIconItemStack: ItemStack = new ItemStack(knife)
  override def getTabIconItem = getIconItemStack
  
  @SideOnly(Side.CLIENT)
  override def displayAllRelevantItems(list: NonNullList[ItemStack]) {
    super.displayAllRelevantItems(list)
    TabData.eggIds.foreach { id =>
      val stack = new ItemStack(Items.SPAWN_EGG, 1)
      ItemMonsterPlacer.applyEntityIdToItemStack(stack, id)
      list.asInstanceOf[java.util.List[ItemStack]].add(stack)
    }
  }
}