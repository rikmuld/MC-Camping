package com.rikmuld.camping.features.inventory_camping

import com.rikmuld.camping.Definitions.Lantern
import com.rikmuld.camping.Library.{AdvancementInfo, NBTInfo}
import com.rikmuld.camping.features.inventory_camping.InventoryCamping._
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.inventory.{InventoryItem, InventoryPlayer}
import com.rikmuld.corerm.utils.NBTUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraft.world.storage.MapData

object InventoryCamping {
  final val POUCH = 12 until 15
  final val BACKPACK = Vector(3, 4, 5, 12, 13, 14, 21, 22, 23)
  final val RUCKSACK = 0 until 27

  final val SLOT_BACKPACK = 0
  final val SLOT_KNIFE = 1
  final val SLOT_LANTERN = 2
  final val SLOT_MAP = 3

  var inventory: InventoryCamping =
    _

  def refreshInventory(player: EntityPlayer): Unit =
    inventory = getInventory(player)

  def getMap: ItemStack =
    inventory.getStackInSlot(SLOT_MAP)

  def getLantern: ItemStack =
    inventory.getStackInSlot(SLOT_LANTERN)

  def getMapData(map: ItemStack, world: World): MapData =
    Items.FILLED_MAP.getMapData(map, world)

  def lanternTick(player: EntityPlayer): Unit = {
    val lantern = getLantern

    if (lantern.getItemDamage == Lantern.ON) {
      val time =
        if(lantern.hasTagCompound)
          lantern.getTagCompound.getInteger("time")
        else {
          lantern.setTagCompound(new NBTTagCompound)
          750
        }

      if(time > 1) {
        lantern.getTagCompound.setInteger("time", time - 1)
        inventory.setInventorySlotContents(SLOT_LANTERN, lantern)
      } else
        inventory.setInventorySlotContents(SLOT_LANTERN,
          new ItemStack(ObjRegistry.lantern, 1, Lantern.OFF)
        )

      inventory.saveTag(player)
    }
  }

  def getSlotsFor(stack: ItemStack): Seq[Int] =
    if (stack.isEmpty || stack.getItem != ObjRegistry.backpack) Vector()
    else if (stack.getItemDamage == 0) POUCH
    else if (stack.getItemDamage == 1) BACKPACK
    else RUCKSACK

  def dropItems(player: EntityPlayer): Unit = {
    if (player.getEntityData.hasKey(NBTInfo.INV_CAMPING))
      NBTUtils.readInventory(player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)).values foreach {
        item => player.dropItem(item, true, false)
      }

    EventsServer.mapChanged(None, player)
  }

  def getInventory(player: EntityPlayer): InventoryCamping = {
    val inv = new InventoryCamping(player, None)
    inv.openInventory(player)
    inv
  }
}

class InventoryCamping(player: EntityPlayer, container: Option[ContainerCamping]) extends InventoryPlayer(player, 4, 1, NBTInfo.INV_CAMPING) {
  private var backpackInv: Option[InventoryItem] =
    None

  override def openInventory(player:EntityPlayer) {
    super.openInventory(player)

    if(container.isDefined)
      onChange(SLOT_BACKPACK)
  }

  override def onChange(slotNum: Int) {
    if (slotNum == SLOT_BACKPACK)
      backpackChanged()

    if (slotNum == SLOT_MAP)
      EventsServer.mapChanged(Some(getStackInSlot(slotNum)), player)

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
      container.foreach(_.backpackChanged(pack, backpackInv))

      backpackInv.get.openInventory(player)
    } else {
      backpackInv = None
      container.foreach(_.backpackChanged(pack, backpackInv))
    }
  }

  override def closeInventory(player: EntityPlayer): Unit = {
    backpackInv.foreach(_.closeInventory(player))

    super.closeInventory(player)
  }

  def getBackpack: Option[InventoryItem] =
    backpackInv
}
