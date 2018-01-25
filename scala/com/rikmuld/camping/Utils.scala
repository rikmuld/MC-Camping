package com.rikmuld.camping

import java.util.ArrayList

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.{BlockDefinitions, Objs}
import com.rikmuld.corerm.utils.CoreUtils._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.storage.MapData
import net.minecraftforge.common.util.Constants

import scala.collection.JavaConversions._

object Utils {
  implicit class CampingUtils(player: EntityPlayer) {
    //same as the readitems in inventorytag, generalize
    def loadCampInvItemsFromNBT(): ArrayList[ItemStack] = {
      val tag = player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      if (tag == null) return null
      val stacks = new ArrayList[ItemStack]()
      val inventory = tag.getTagList("items", Constants.NBT.TAG_COMPOUND)
      for (i <- 0 until inventory.tagCount()) {
        val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
        Slots.getByte("slotIndex")
        stacks.add(new ItemStack(Slots))
      }
      stacks
    }
    def loadCampInvSlotNumFromNBT(): ArrayList[Byte] = {
      val tag = player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      if (tag == null) return null
      val slots = new ArrayList[Byte]()
      val inventory = tag.getTagList("items", Constants.NBT.TAG_COMPOUND)
      for (i <- 0 until inventory.tagCount()) {
        val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
        slots.add(Slots.getByte("slotIndex"))
      }
      slots
    }
    def getLanternDamage(): Int = {
      val stacks = loadCampInvItemsFromNBT()
      for (stack <- stacks if stack.getItem == Item.getItemFromBlock(Objs.lantern) if stack.getItemDamage == Lantern.ON) {
        if (!stack.hasTagCompound()) {
          stack.setTagCompound(new NBTTagCompound())
          stack.getTagCompound.setInteger("time", 1500)
        }
        return stack.getTagCompound.getInteger("time")
      }
      -1
    }
    def getCurrentMapData(): MapData = {
      val stacks = loadCampInvItemsFromNBT()
      for (stack <- stacks if stack.getItem == Items.FILLED_MAP) {
        return Items.FILLED_MAP.getMapData(stack, player.world)
      }
      null
    }
    def hasLantarn(): Boolean = loadCampInvItemsFromNBT().containsStack(new ItemStack(Item.getItemFromBlock(Objs.lantern), 1, BlockDefinitions.Lantern.ON))
    def hasMap(): Boolean = loadCampInvItemsFromNBT().containsItem(Items.FILLED_MAP)
    def lanternTick() {
      val stacks = player.loadCampInvItemsFromNBT();
      val slots = player.loadCampInvSlotNumFromNBT();
      val stacks2 = new ArrayList[ItemStack];

      for (i <- 0 until 4) {
        if (slots.contains(i.toByte)) {
          var stack = stacks.get(slots.indexOf(i.toByte))
          if ((stack.getItem().equals(Item.getItemFromBlock(Objs.lantern))) && (stack.getItemDamage() == Lantern.ON)) {
            if (!stack.hasTagCompound()) {
              stack.setTagCompound(new NBTTagCompound())
              stack.getTagCompound().setInteger("time", 1500)
            }
            if ((stack.getTagCompound().getInteger("time") - 1) > 0) stack.getTagCompound().setInteger("time", stack.getTagCompound().getInteger("time") - 1);
            else stack = new ItemStack(Objs.lantern, 1, Lantern.OFF)
          }
          stacks2.add(stack)
        } else stacks2.add(null)
      }

      player.getEntityData().getCompoundTag(NBTInfo.INV_CAMPING).setTag("Items", stacks2.getNBT())
    }
  }
}
