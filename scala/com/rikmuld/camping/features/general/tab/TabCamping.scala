package com.rikmuld.camping.features.general.tab

import com.rikmuld.camping.CampingMod
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable

class TabCamping(name: String) extends CreativeTabs(name) {
  private val additionalItems: mutable.ListBuffer[ItemStack] =
    mutable.ListBuffer()

  override def getTabIconItem =
    new ItemStack(CampingMod.OBJ.knife)

  def addToTab(stack: ItemStack): Unit =
    additionalItems.append(stack)

  @SideOnly(Side.CLIENT)
  override def displayAllRelevantItems(list: NonNullList[ItemStack]) {
    super.displayAllRelevantItems(list)

    additionalItems foreach list.add
  }
}
