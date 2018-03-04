package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.Library._
import com.rikmuld.camping.inventory.camping.GuiCamping._
import com.rikmuld.camping.inventory.camping.InventoryCamping._
import com.rikmuld.camping.inventory.gui.GuiEffect
import com.rikmuld.camping.objs.Definitions
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.gui.gui.{GuiTabbed, Tab}
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

object GuiCamping {
  final val TAB_ARMOR = 0
  final val TAB_BACKPACK = 1
  final val TAB_CRAFTING = 2
  final val TAB_CONFIG = 3
}

class GuiCamping(var player: EntityPlayer) extends GuiTabbed(player, new ContainerCamping(player)) {
  this.xSize = 220
  this.ySize = 166

  val container:ContainerCamping =
    inventorySlots.asInstanceOf[ContainerCamping]

  override val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_CAMPINV)

  override def getTabs: Seq[Tab] = Seq(
    new Tab("Armor", 20, this, TAB_ARMOR, new ItemStack(Items.SKULL, 1, 3)),
    new Tab("Backpack", 20, this, TAB_BACKPACK, new ItemStack(ObjRegistry.backpack)),
    new Tab("Crafting", 20, this, TAB_CRAFTING, new ItemStack(ObjRegistry.knife)),
    new Tab("Configuration", 20, this, TAB_CONFIG, 220, 0)
  )

  def hasBackpack:Boolean =
    !container.getIInventory.getStackInSlot(SLOT_BACKPACK).isEmpty

  def backpackType: Int =
    container.getIInventory.getStackInSlot(SLOT_BACKPACK).getItemDamage

  def hasKnife:Boolean =
    !container.getIInventory.getStackInSlot(SLOT_KNIFE).isEmpty

  override def setTab(index: Int): Unit =
    if(index == TAB_CONFIG){
      val gui =  new ConfigGUI(this)

      gui.setWorldAndResolution(mc, width, height)
      gui.initGui()

      mc.currentScreen = gui
    } else super.setTab(index)

  override def drawGUI(mouseX: Int, mouseY: Int) {
    setEnabled(TAB_BACKPACK, hasBackpack)
    setEnabled(TAB_CRAFTING, hasKnife)

    if (isPointInRegion(8, 35, 16, 16, mouseX, mouseY))
      itemRender.renderItemIntoGUI(new ItemStack(ObjRegistry.backpack), guiLeft + 8, guiTop + 35)

    if (isPointInRegion(8, 53, 16, 16, mouseX, mouseY))
      itemRender.renderItemIntoGUI(new ItemStack(ObjRegistry.knife), guiLeft + 8, guiTop + 53)

    if (isPointInRegion(196, 35, 16, 16, mouseX, mouseY))
      itemRender.renderItemIntoGUI(new ItemStack(ObjRegistry.lantern), guiLeft + 196, guiTop + 35)

    if (isPointInRegion(196, 53, 16, 16, mouseX, mouseY))
      itemRender.renderItemIntoGUI(new ItemStack(Items.FILLED_MAP), guiLeft + 196, guiTop + 53)

    GuiEffect.draw(guiLeft, guiTop, this, mc, fontRenderer)

    bindTexture()

    super.drawGUI(mouseX, mouseY)
  }

  override def drawGuiContainerForegroundLayer(mouseX:Int, mouseY:Int): Unit =
    if(getActive == GuiCamping.TAB_ARMOR)
      this.fontRenderer.drawString(I18n.format("container.crafting", new java.lang.Object), 119, 8, 4210752)

  def drawPage(tab: Int, mouseX: Int, mouseY: Int): Unit = tab match{
    case TAB_BACKPACK if hasBackpack =>
      if(backpackType == Definitions.Backpack.RUCKSACK)
        drawTexturedModalRect(guiLeft+29, guiTop+16, 29, 83, 162, 54)
      else if(backpackType == Definitions.Backpack.BACKPACK)
        drawTexturedModalRect(guiLeft + 83, guiTop + 16, 29, 83, 54, 54)
      else if(backpackType == Definitions.Backpack.POUCH)
        drawTexturedModalRect(guiLeft + 83, guiTop + 34, 0, 166, 54, 18)
    case TAB_CRAFTING =>
      drawTexturedModalRect(guiLeft+52, guiTop+16, 0, 166, 115, 54)
    case TAB_ARMOR =>
      this.mc.getTextureManager.bindTexture(GuiEffect.inventoryTexture)
      this.drawTexturedModalRect(guiLeft + 29, guiTop+7, 7, 7, 164, 72)
      GuiInventory.drawEntityOnScreen(guiLeft + 51 + 22, guiTop + 75, 30, guiLeft + 51 + 22 - mouseX, guiTop + 75 - 50 - mouseY, this.mc.player)
    case _ =>
  }
}