package com.rikmuld.camping

import java.util.ArrayList

import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.utils.NBTUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.storage.MapData
import net.minecraftforge.common.util.Constants

import scala.collection.JavaConversions._

object Utils {
  implicit class CampingUtils(player: EntityPlayer) {
    //use nbt utils
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
    //use nbt utils
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
//      for (stack <- stacks if stack.getItem == Item.getItemFromBlock(Objs.lantern) if stack.getItemDamage == Lantern.ON) {
//        if (!stack.hasTagCompound()) {
//          stack.setTagCompound(new NBTTagCompound())
//          stack.getTagCompound.setInteger("time", 1500)
//        }
//        return stack.getTagCompound.getInteger("time")
//      }
      -1
    }
    def getCurrentMapData(): MapData = {
      val stacks = loadCampInvItemsFromNBT()
      for (stack <- stacks if stack.getItem == Items.FILLED_MAP) {
        return Items.FILLED_MAP.getMapData(stack, player.world)
      }
      null
    }
    def hasLantarn(): Boolean = loadCampInvItemsFromNBT().exists(stack =>
      false//stack.isItemEqual(new ItemStack(Item.getItemFromBlock(Objs.lantern), 1, BlockDefinitions.Lantern.ON))
    )
    def hasMap(): Boolean = loadCampInvItemsFromNBT().exists(stack => stack.getItem == Items.FILLED_MAP)
    def lanternTick() {
      val data = player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      val inv = NBTUtils.readInventory(data)

//      inv.get(InventoryCamping.SLOT_LANTERN.toByte).foreach { lantern =>
//        if (lantern.getItemDamage == Lantern.ON) {
//          val time =
//            if (lantern.hasTagCompound) lantern.getTagCompound.getInteger("time")
//            else 1500
//
//          NBTUtils.writeInventory(data, inv.updated(InventoryCamping.SLOT_LANTERN.toByte,
//            if (time - 1 <= 0) new ItemStack(Objs.lantern, 1, Lantern.OFF)
//            else {
//              lantern.getTagCompound.setInteger("time", time - 1)
//              lantern
//            }
//          ))
//
//          player.getEntityData.setTag(NBTInfo.INV_CAMPING, data)
//        }
//      }
    }
  }
}
