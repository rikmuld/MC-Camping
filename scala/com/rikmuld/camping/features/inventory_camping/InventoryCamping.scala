package com.rikmuld.camping.features.inventory_camping

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Library.{AdvancementInfo, NBTInfo}
import com.rikmuld.camping.features.blocks.lantern.TileEntityLantern
import com.rikmuld.camping.features.inventory_camping.InventoryCamping._
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

  val EMPTY_INV: Map[Byte, ItemStack] = Map(
    0.toByte -> ItemStack.EMPTY,
    1.toByte -> ItemStack.EMPTY,
    2.toByte -> ItemStack.EMPTY,
    3.toByte -> ItemStack.EMPTY
  )

  def getInventory(player: EntityPlayer): Map[Byte, ItemStack] =
    if (player.getEntityData.hasKey(NBTInfo.INV_CAMPING))
      EMPTY_INV ++ NBTUtils.readInventory(player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING))
    else
      EMPTY_INV

  def getMapData(map: ItemStack, world: World): MapData =
    Items.FILLED_MAP.getMapData(map, world)

  def lanternTick(player: EntityPlayer): Unit = {
    val time = getLanternTime(player)

    if (time > 0)
      setLanternTime(player, time - 1)
  }

  def getSlotsFor(stack: ItemStack): Seq[Int] =
    if (stack.isEmpty || stack.getItem != CampingMod.OBJ.backpack) Vector()
    else if (stack.getItemDamage == 0) POUCH
    else if (stack.getItemDamage == 1) BACKPACK
    else RUCKSACK

  def dropItems(player: EntityPlayer): Unit = {
    getInventory(player).values foreach(item =>
      if (!item.isEmpty)
        player.dropItem(item, true, false))

    EventsServer.mapChanged(None, player)
  }

  def lanternTimeToInventory(player: EntityPlayer, inventory: InventoryCamping): Unit = {
    val time = getLanternTime(player)
    val lantern = inventory.getStackInSlot(InventoryCamping.SLOT_LANTERN)

    if (!lantern.isEmpty)
      inventory.setInventorySlotContents(
        InventoryCamping.SLOT_LANTERN,
        TileEntityLantern.stackFromTime(time, 1)
      )
  }

  def lanternTimeFromInventory(player: EntityPlayer, inventory: InventoryCamping): Unit = {
    val lantern = inventory.getStackInSlot(InventoryCamping.SLOT_LANTERN)
    val time = TileEntityLantern.timeFromStack(lantern)

    setLanternTime(player, time)
  }

  def getLanternTime(player: EntityPlayer): Int =
    withLanternKey(player, _.getInteger(NBTInfo.LANTERN_TIME))

  def setLanternTime(player: EntityPlayer, time: Int): Unit =
    withLanternKey(player, _.setInteger(NBTInfo.LANTERN_TIME, time))

  def withLanternKey[A](player: EntityPlayer, f: NBTTagCompound => A): A = {
    val data = player.getEntityData

    if (!data.hasKey(NBTInfo.LANTERN_TIME))
      data.setInteger(NBTInfo.LANTERN_TIME, 0)

    f(data)
  }
}

// TODO now container is none if inventory is used for reading the nbt, make something sepperate for this
class InventoryCamping(player: EntityPlayer, container: Option[ContainerCamping]) extends InventoryPlayer(player, 4, 1, NBTInfo.INV_CAMPING) {
  private var backpackInv: Option[InventoryItem] =
    None

  override def openInventory(player:EntityPlayer) {
    super.openInventory(player)

    if(container.isDefined) {
      onChange(SLOT_BACKPACK)
      InventoryCamping.lanternTimeToInventory(player, this)
    }
  }

  override def onChange(slotNum: Int) {
    if (slotNum == SLOT_BACKPACK)
      backpackChanged()

    if (slotNum == SLOT_MAP)
      EventsServer.mapChanged(Some(getStackInSlot(slotNum)), player)

    if (slotNum == SLOT_LANTERN)
      InventoryCamping.lanternTimeFromInventory(player, this)

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
