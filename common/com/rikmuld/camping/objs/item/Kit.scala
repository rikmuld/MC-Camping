package com.rikmuld.camping.objs.item

import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.camping.objs.Objs
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.objs.RMItem
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraftforge.fml.relauncher.SideOnly
import scala.collection.mutable.HashMap
import net.minecraft.item.Item
import net.minecraftforge.fml.relauncher.Side
import org.lwjgl.input.Keyboard
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.objs.Properties
import com.rikmuld.corerm.objs.PropType._
import com.rikmuld.corerm.Lib._

class Kit(modId:String, info:ObjInfo) extends RMItem(modId, info) {
  override def addInformation(stack: ItemStack, player: EntityPlayer, list: java.util.List[String], par4: Boolean) {
    if ((stack.getItemDamage > 0) && stack.hasTagCompound() && Keyboard.isKeyDown(42)) {
      list.clear()
      list.asInstanceOf[java.util.List[String]].add("This kit contains:")
      val itemMap = new HashMap[String, Integer]()
      val containingItems = stack.getTagCompound.getTag("Items").asInstanceOf[NBTTagList]
      for (itemCound <- 0 until containingItems.tagCount()) {
        val item = ItemStack.loadItemStackFromNBT(containingItems.getCompoundTagAt(itemCound).asInstanceOf[NBTTagCompound])
        if (!itemMap.contains(item.getDisplayName)) itemMap(item.getDisplayName) = 1
        else itemMap(item.getDisplayName) = itemMap(item.getDisplayName) + 1
      }
      itemMap.foreach(mapData => list.asInstanceOf[java.util.List[String]].add(java.lang.Integer.toString(mapData._2) + " " + mapData._1))
    } else if (stack.getItemDamage > 0) {
      list.asInstanceOf[java.util.List[String]].add(TextInfo.FORMAT_ITALIC + "Hold shift for more information")
    }
  }
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, creativetabs: CreativeTabs, list: java.util.List[ItemStack]) {
    val stick = new ItemStack(Items.stick)
    val stickIron = new ItemStack(Objs.parts, 1, Parts.STICK_IRON)
    val string = new ItemStack(Objs.parts, 1, Parts.PAN)
    val pan = new ItemStack(Items.string)
    val fenceIron = new ItemStack(Blocks.iron_bars)
    val inventoryContents = Array(Array(stick, stick, stickIron), Array(stick, stick, stick, stick, stickIron, stickIron, fenceIron), Array(stick, stick, stickIron, string, pan))
    list.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(item, 1, 0))
    for (meta <- 1 until (info.getProp[Properties.Metadata](METADATA).names.size - 1)) {
      val stack = new ItemStack(item, 1, meta)
      val inventory = new NBTTagList()
      for (slot <- 0 until inventoryContents(meta - 1).length if inventoryContents(meta - 1)(slot) != null) {
        val Slots = new NBTTagCompound()
        Slots.setByte("Slot", slot.toByte)
        inventoryContents(meta - 1)(slot).writeToNBT(Slots)
        inventory.appendTag(Slots)
      }
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound.setTag("Items", inventory)
      list.asInstanceOf[java.util.List[ItemStack]].add(stack)
    }
  } 
}