package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.Lib.{AdvancementInfo, NBTInfo}
import com.rikmuld.camping.inventory.camping.InventoryCamping._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.inventory.{InventoryItem, InventoryPlayer}
import com.rikmuld.corerm.utils.NBTUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.inventory.{InventoryCraftResult, InventoryCrafting}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object InventoryCamping {
  final val POUCH = 12 until 15
  final val BACKPACK = Vector(3, 4, 5, 12, 13, 14, 21, 22, 23)
  final val RUCKSACK = 0 until 27

  final val SLOT_BACKPACK = 0
  final val SLOT_KNIFE = 1
  final val SLOT_LANTERN = 2
  final val SLOT_MAP = 3

  def getSlotsFor(stack: ItemStack): Seq[Int] =
    if(stack.isEmpty || stack.getItem != Registry.backpack) Vector()
    else if(stack.getItemDamage == 0) POUCH
    else if(stack.getItemDamage == 1) BACKPACK
    else RUCKSACK

  def dropItems(player: EntityPlayer): Unit =
    if(player.getEntityData.hasKey(NBTInfo.INV_CAMPING))
      NBTUtils.readInventory(player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)).values foreach {
        item => player.dropItem(item, true, false)
      }
}

class InventoryCamping(player: EntityPlayer, container: ContainerCamping) extends InventoryPlayer(player, 4, 1, NBTInfo.INV_CAMPING) {
  val craftMatrix: InventoryCrafting =
    new InventoryCrafting(container, 3, 3)

  val craftMatrixSmall: InventoryCrafting =
    new InventoryCrafting(container, 2, 2)

  val craftResult: InventoryCraftResult =
    new InventoryCraftResult()

  val craftResultSmall: InventoryCraftResult =
    new InventoryCraftResult()

  private var backpackInv: Option[InventoryItem] =
    None

  override def openInventory(player:EntityPlayer) {
    super.openInventory(player)

    onChange(SLOT_BACKPACK)
  }

  override def onChange(slotNum: Int) {
    if (slotNum == SLOT_BACKPACK)
      backpackChanged()

    if(!player.world.isRemote)
      TriggerHelper.trigger(AdvancementInfo.INVENTORY_CHANGED, player.asInstanceOf[EntityPlayerMP], this)
  }

  def backpackChanged(): Unit = {
    val pack = getStackInSlot(SLOT_BACKPACK)

    //TODO solves the problems of a bug, but not the root of the problem, fix that and remove this! (probably fix in core?)
    if(!pack.isEmpty && !pack.hasTagCompound)
      pack.setTagCompound(new NBTTagCompound())

    if (!pack.isEmpty) {
      backpackInv = Some(new InventoryItem(pack, 27, 64))
      container.backpackChanged(pack, backpackInv)

      backpackInv.get.openInventory(player)
    } else {
      backpackInv = None
      container.backpackChanged(pack, backpackInv)
    }
  }

  override def closeInventory(player: EntityPlayer): Unit = {
    backpackInv.foreach(_.closeInventory(player))

    for (i <- 0 until 9)
      player.dropItem(craftMatrix.removeStackFromSlot(i), false)

    for (i <- 0 until 4)
      player.dropItem(craftMatrixSmall.removeStackFromSlot(i), false)

    super.closeInventory(player)
  }

  def getBackpack: Option[InventoryItem] =
    backpackInv
}
