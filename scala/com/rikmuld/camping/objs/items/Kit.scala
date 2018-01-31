//package com.rikmuld.camping.objs.item
//
//import com.rikmuld.camping.objs.ItemDefinitions._
//import com.rikmuld.camping.objs.Objs
//import com.rikmuld.corerm.Library.TextInfo
//import com.rikmuld.corerm.objs.PropType._
//import com.rikmuld.corerm.objs.items.ItemRM
//import com.rikmuld.corerm.objs.{ObjDefinition, ObjInfo, Properties}
//import net.minecraft.client.util.ITooltipFlag
//import net.minecraft.creativetab.CreativeTabs
//import net.minecraft.init.{Blocks, Items}
//import net.minecraft.item.ItemStack
//import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
//import net.minecraft.util.NonNullList
//import net.minecraft.world.World
//import org.lwjgl.input.Keyboard
//
//import scala.collection.mutable.HashMap
//
//class Kit(modId:String, info:ObjDefinition) extends ItemRM(modId, info) {
//  override def addInformation(stack: ItemStack, player: World, list: java.util.List[String], par4: ITooltipFlag) {
//    if ((stack.getItemDamage > 0) && stack.hasTagCompound() && Keyboard.isKeyDown(42)) {
//      list.clear()
//      list.asInstanceOf[java.util.List[String]].add("This kit contains:")
//      val itemMap = new HashMap[String, Integer]()
//      val containingItems = stack.getTagCompound.getTag("items").asInstanceOf[NBTTagList]
//      for (itemCound <- 0 until containingItems.tagCount()) {
//        val item = new ItemStack(containingItems.getCompoundTagAt(itemCound))
//        if (!itemMap.contains(item.getDisplayName)) itemMap(item.getDisplayName) = 1
//        else itemMap(item.getDisplayName) = itemMap(item.getDisplayName) + 1
//      }
//      itemMap.foreach(mapData => list.asInstanceOf[java.util.List[String]].add(java.lang.Integer.toString(mapData._2) + " " + mapData._1))
//    } else if (stack.getItemDamage > 0) {
//      list.asInstanceOf[java.util.List[String]].add(TextInfo.FORMAT_ITALIC + "Hold shift for more information")
//    }
//  }
//
//  override def getSubItems(tab: CreativeTabs, list: NonNullList[ItemStack]) {
//    if(!isInCreativeTab(tab)) return
//
//    val stick = new ItemStack(Items.STICK)
//    val stickIron = new ItemStack(Objs.parts, 1, Parts.STICK_IRON)
//    val string = new ItemStack(Objs.parts, 1, Parts.PAN)
//    val pan = new ItemStack(Items.STRING)
//    val fenceIron = new ItemStack(Blocks.IRON_BARS)
//    val inventoryContents = Array(Array(stick, stick, stickIron), Array(stick, stick, stick, stick, stickIron, stickIron, fenceIron), Array(stick, stick, stickIron, string, pan))
//    list.asInstanceOf[java.util.List[ItemStack]].add(new ItemStack(this, 1, 0))
//    for (meta <- 1 until (info.getProp[Properties.ItemMetaData](METADATA).names.size - 1)) {
//      val stack = new ItemStack(this, 1, meta)
//      val inventory = new NBTTagList()
//      for (slot <- 0 until inventoryContents(meta - 1).length if inventoryContents(meta - 1)(slot) != null) {
//        val Slots = new NBTTagCompound()
//        Slots.setByte("slotIndex", slot.toByte)
//        inventoryContents(meta - 1)(slot).writeToNBT(Slots)
//        inventory.appendTag(Slots)
//      }
//      stack.setTagCompound(new NBTTagCompound())
//      stack.getTagCompound.setTag("items", inventory)
//      list.asInstanceOf[java.util.List[ItemStack]].add(stack)
//    }
//  }
//}