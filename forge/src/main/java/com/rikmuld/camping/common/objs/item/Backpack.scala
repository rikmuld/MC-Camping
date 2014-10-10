package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.ObjInfo
import com.rikmuld.camping.core.Objs
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.common.inventory.SlotItemsNot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.common.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.SlotNoPickup
import org.lwjgl.opengl.GL11
import com.rikmuld.camping.CampingMod
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import com.rikmuld.camping.core.ObjInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.common.inventory.SlotItemsNot
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.common.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.SlotNoPickup
import com.rikmuld.camping.CampingMod
import net.minecraft.client.gui.Gui
import com.rikmuld.camping.core.Utils._

class Backpack(infoClass: Class[ObjInfo]) extends ItemMain(infoClass) {
  maxStackSize = 1
  setHasSubtypes(true)
  CampingMod.proxy.registerGui(GuiInfo.GUI_BACKPACK, classOf[ContainerBackpack].asInstanceOf[Class[Container]], classOf[GuiBackpack].asInstanceOf[Class[Gui]])

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (!world.isRemote) player.openGui(CampingMod, GuiInfo.GUI_BACKPACK, world, 0, 0, 0)
    stack;
  }
}

class ContainerBackpack(player: EntityPlayer) extends Container {
  val invPlayer: InventoryPlayer = player.inventory;
  val inv = new InventoryItemMain(player.getCurrentEquippedItem, player, 27, 64);

  for (row <- 0 until 3; collom <- 0 until 9) {
    this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack));
  }
  this.addSlots(invPlayer, 9, 3, 9, 8, 84);
  for (row <- 0 until 9) {
    if (row == invPlayer.currentItem) addSlotToContainer(new SlotNoPickup(invPlayer, row, 8 + (row * 18), 142));
    else addSlotToContainer(new Slot(invPlayer, row, 8 + (row * 18), 142))
  }

  inv.openInventory();

  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead && inv.isUseableByPlayer(player)
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    inv.closeInventory()
    if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().equals(Objs.backpack)) ((inv).setNBT(player.getCurrentEquippedItem()))
  }
  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null;
    var slot = inventorySlots.get(i).asInstanceOf[Slot];
    if ((slot != null) && slot.getHasStack()) {
      var itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (i < inv.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), true)) return null;
      } else if (!mergeItemStack(itemstack1, 0, inv.getSizeInventory(), false)) return null;
      if (itemstack1.stackSize == 0) slot.putStack(null);
      else slot.onSlotChanged();
    }
    itemstack;
  }
}

class GuiBackpack(player:EntityPlayer) extends GuiContainer(new ContainerBackpack(player)) {
  protected override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_BAG))
    val var5 = (width - xSize) / 2
    val var6 = (height - ySize) / 2
    drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize)
  }
  override def drawGuiContainerForegroundLayer(i: Int, j: Int) {
    fontRendererObj.drawString("Hiking Bag", 64, 10, 4210752)
  }
}
