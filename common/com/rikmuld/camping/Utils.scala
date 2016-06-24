package com.rikmuld.camping

import java.util.ArrayList
import java.util.Random
import net.minecraft.block.Block
import net.minecraft.entity.EntityList
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraft.world.storage.MapData
import net.minecraftforge.oredict.OreDictionary
import scala.collection.JavaConversions._
import net.minecraftforge.common.util.Constants
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.BlockDefinitions
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.ItemDefinitions._
import net.minecraft.block.state.BlockState
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState

object Utils {
  implicit class CampingUtils(player: EntityPlayer) {
    def loadCampInvItemsFromNBT(): ArrayList[ItemStack] = {
      val tag = player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      if (tag == null) return null
      val stacks = new ArrayList[ItemStack]()
      val inventory = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
      for (i <- 0 until inventory.tagCount()) {
        val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
        Slots.getByte("Slot")
        stacks.add(ItemStack.loadItemStackFromNBT(Slots))
      }
      stacks
    }
    def loadCampInvSlotNumFromNBT(): ArrayList[Byte] = {
      val tag = player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      if (tag == null) return null
      val slots = new ArrayList[Byte]()
      val inventory = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
      for (i <- 0 until inventory.tagCount()) {
        val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
        slots.add(Slots.getByte("Slot"))
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
      for (stack <- stacks if stack.getItem == Items.filled_map) {
        return Items.filled_map.getMapData(stack, player.worldObj)
      }
      null
    }
    def hasLantarn(): Boolean = loadCampInvItemsFromNBT().containsStack(new ItemStack(Item.getItemFromBlock(Objs.lantern), 1, BlockDefinitions.Lantern.ON))
    def hasMap(): Boolean = loadCampInvItemsFromNBT().containsItem(Items.filled_map)
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
