package com.rikmuld.camping.objs.misc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import com.rikmuld.camping.objs.Objs._

import scala.collection.mutable.ListBuffer
import net.minecraft.init.Items
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.item.ItemMonsterPlacer
import net.minecraft.util.{NonNullList, ResourceLocation}

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