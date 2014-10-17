package com.rikmuld.camping.common.objs.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.init.Blocks
import net.minecraft.inventory.ISidedInventory
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import com.rikmuld.camping.common.network.PacketSender
import com.rikmuld.camping.common.network.TileData
import com.rikmuld.camping.misc.Bounds
import com.rikmuld.camping.common.network.BoundsData

class TileEntityMain extends TileEntity {
  override def readFromNBT(tag: NBTTagCompound) = super.readFromNBT(tag)
  override def writeToNBT(tag: NBTTagCompound) = super.writeToNBT(tag)
  override def getDescriptionPacket(): Packet = {
    val compound = new NBTTagCompound();
    writeToNBT(compound);
    new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, compound);
  }
  override def onDataPacket(net: NetworkManager, packet: S35PacketUpdateTileEntity) = readFromNBT(packet.func_148857_g());
  def setTileData(id: Int, data: Array[Int]) {}
  def sendTileData(id: Int, client: Boolean, data: Int*) {
    if (!client && worldObj.isRemote) PacketSender.toServer(new TileData(id, xCoord, yCoord, zCoord, data));
    else if (client && !worldObj.isRemote) PacketSender.toClient(new TileData(id, xCoord, yCoord, zCoord, data));
  }
}

class TileEntityWithRotation extends TileEntityMain {
  var rotation: Int = _

  def cycleRotation() = if (!getWorldObj.isRemote) setRotation(if (rotation < 3) rotation + 1 else 0)
  def setRotation(rotation: Int) {
    if (!getWorldObj().isRemote) {
      this.rotation = rotation
      sendTileData(0, true, this.rotation)
    }
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) {
      rotation = data(0)
      getWorldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, Blocks.air)
      getWorldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, Blocks.air)
      getWorldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("rotation", rotation)
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    rotation = tag.getInteger("rotation")
  }
}

abstract trait TileEntityWithInventory extends TileEntity with IInventory with ISidedInventory {
  var inventoryContents: Array[ItemStack] = new Array[ItemStack](getSizeInventory)

  override def canExtractItem(slot: Int, ItemStack: ItemStack, side: Int): Boolean = false
  override def canInsertItem(slot: Int, itemStack: ItemStack, side: Int): Boolean = isItemValidForSlot(slot, itemStack)
  override def decrStackSize(slot: Int, amount: Int): ItemStack = {
    if (inventoryContents(slot) != null) {
      var itemstack: ItemStack = null
      if (inventoryContents(slot).stackSize <= amount) {
        itemstack = inventoryContents(slot)
        inventoryContents(slot) = null
        itemstack
      } else {
        itemstack = inventoryContents(slot).splitStack(amount)
        if (inventoryContents(slot).stackSize == 0) {
          inventoryContents(slot) = null
        }
        itemstack
      }
    } else null
  }
  override def getAccessibleSlotsFromSide(side: Int): Array[Int] = Array.ofDim[Int](0)
  override def getInventoryStackLimit(): Int = 64
  override def getSizeInventory(): Int
  override def getStackInSlot(slot: Int): ItemStack = inventoryContents(slot)
  override def getStackInSlotOnClosing(slot: Int): ItemStack = {
    if (inventoryContents(slot) != null) {
      val itemstack = inventoryContents(slot)
      inventoryContents(slot) = null
      itemstack
    } else null
  }
  def hasStackInSlot(slot: Int): Boolean = getStackInSlot(slot) != null
  override def isItemValidForSlot(slot: Int, itemstack: ItemStack): Boolean = false
  override def isUseableByPlayer(player: EntityPlayer): Boolean = if (getWorldObj.getTileEntity(xCoord, yCoord, zCoord) != this) false else player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D
  override def readFromNBT(tag: NBTTagCompound) {
    inventoryContents = Array.ofDim[ItemStack](getSizeInventory)
    val inventory = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
    for (i <- 0 until inventory.tagCount()) {
      val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
      val slot = Slots.getByte("Slot")
      if ((slot >= 0) && (slot < inventoryContents.length)) inventoryContents(slot) = ItemStack.loadItemStackFromNBT(Slots)
    }
    super.readFromNBT(tag)
  }
  override def setInventorySlotContents(slot: Int, stack: ItemStack) {
    inventoryContents(slot) = stack
    if ((stack != null) && (stack.stackSize > getInventoryStackLimit)) stack.stackSize = getInventoryStackLimit
  }
  override def writeToNBT(tag: NBTTagCompound) {
    val inventory = new NBTTagList()
    for (slot <- 0 until inventoryContents.length if inventoryContents(slot) != null) {
      val Slots = new NBTTagCompound()
      Slots.setByte("Slot", slot.toByte)
      inventoryContents(slot).writeToNBT(Slots)
      inventory.appendTag(Slots)
    }
    tag.setTag("Items", inventory)
    super.writeToNBT(tag)
  }
  override def getInventoryName(): String = "container_" + getBlockType.getUnlocalizedName.substring(5)
  override def hasCustomInventoryName(): Boolean = false
  override def openInventory() {}
  override def closeInventory() {}
}

class TileEntityWithBounds extends TileEntityMain {
  var bounds: Bounds = _
  var baseX: Int = _
  var baseY: Int = _
  var baseZ: Int = _
  private var update: Boolean = _

  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    baseX = tag.getInteger("baseX")
    baseY = tag.getInteger("baseY")
    baseZ = tag.getInteger("baseZ")
    if (tag.hasKey("xMin")) setBounds(Bounds.readBoundsToNBT(tag))
  }
  def setBaseCoords(x: Int, y: Int, z: Int) {
    baseX = x
    baseY = y
    baseZ = z
    sendTileData(0, true, x, y, z)
  }
  def setBounds(bounds: Bounds) {
    this.bounds = bounds
    update = true
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) {
      baseX = data(0)
      baseY = data(1)
      baseZ = data(2)
    }
  }
  override def updateEntity() {
    if (!worldObj.isRemote && update) {
      PacketSender.toClient(new BoundsData(bounds, xCoord, yCoord, zCoord))
      update = false
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("baseX", baseX)
    tag.setInteger("baseY", baseY)
    tag.setInteger("baseZ", baseZ)
    if (bounds != null) bounds.writeBoundsToNBT(tag)
  }
}