package com.rikmuld.camping.objs.items

import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.registers.Objs._
import com.rikmuld.corerm.Library.TextInfo
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.items.ItemRM
import com.rikmuld.corerm.utils.{NBTUtils, StackUtils}
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import org.lwjgl.input.Keyboard

class Kit(modId:String, info:ObjDefinition) extends ItemRM(modId, info) {
  override def addInformation(stack: ItemStack, player: World, list: java.util.List[String], tooltip: ITooltipFlag): Unit =
    if(stack.hasTagCompound) {
      if(Keyboard.isKeyDown(42)) {
        list.clear()
        list.asInstanceOf[java.util.List[String]].add("This kit contains:")

        StackUtils.flatten(NBTUtils.readInventory(stack.getTagCompound).values.toSeq).foreach( stack => {
          list.add(stack.getCount + " " + stack.getDisplayName)
        })
      } else {
        list.add(TextInfo.FORMAT_ITALIC + "Hold shift for more information")
      }
    }

  override def getSubItems(tab: CreativeTabs, list: NonNullList[ItemStack]): Unit =
    if(isInCreativeTab(tab)) {
      val equipments = Seq(spit, grill, pan)

      for(i <- metadata.get.indices.dropRight(1)) {
        val stack = new ItemStack(this, 1, i)

        equipments.find(_.getKitDamage == i).foreach(equipment => {
          val stacks = CookingEquipment.getFirstKitRecipe(equipment).flatMap(stack =>
            for(i <- 0 until stack.getCount)
              yield new ItemStack(stack.getItem, 1, stack.getItemDamage)
          ).zipWithIndex.map(_.swap).toMap

          val tag = new NBTTagCompound()

          NBTUtils.writeInventory2(tag, stacks)
          stack.setTagCompound(tag)
        })

        list.add(stack)
      }
    }
}