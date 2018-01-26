package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.{ItemDefinitions, Objs}
import com.rikmuld.corerm.features.tabbed.GuiTabbed
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object GuiCamping {
  final val TAB_ARMOR = 0
  final val TAB_BACKPACK = 1
  final val TAB_CRAFTING = 2
  final val TAB_CONFIG = 3
}

class GuiCamping(var player: EntityPlayer) extends GuiTabbed(player, new ContainerCamping(player), new ResourceLocation(TextureInfo.GUI_CAMPINV)) with GuiWithEffect {
  this.xSize = 220
  this.ySize = 166
  
  var hasBackpack = false
  var hasKnife = false
  var lastMouseX = 0
  var lastMouseY = 0

  var pack = 0

  val container = inventorySlots.asInstanceOf[ContainerCamping]
           
  override def initGui {
    super.initGui 
    init(guiLeft, guiTop, fontRenderer)
  }
  override def initTabbed {
    addTopTab("Armor", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Items.SKULL, 1, 3))
    addTopTab("Backpack", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Objs.backpack))
    addTopTab("Crafting", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Objs.knife))
    addTopTab("Configuration", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, 220, 0, 16, 16)
  }
  def setContents(){
    val backpack = container.getIInventory.getStackInSlot(0)
    val knife = container.getIInventory.getStackInSlot(1)

    if(!backpack.isEmpty){
      hasBackpack = true
      pack = backpack.getItemDamage
    } else hasBackpack = false

    hasKnife = !knife.isEmpty
  }
  override def setTabTopActive(id: Int) {
    if(id == GuiCamping.TAB_CONFIG){
      val gui =  new ConfigGUI(this)
      gui.setWorldAndResolution(mc, width, height)
      gui.initGui()
      mc.currentScreen = gui
    } else super.setTabTopActive(id)
  }
  protected override def drawGuiContainerBackgroundLayer(partTick: Float, mouseX: Int, mouseY: Int) {
    draw
    setContents
    tabsTop(1).enabled = hasBackpack
    tabsTop(2).enabled = hasKnife
    lastMouseX = mouseX
    lastMouseY = mouseY
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(texture)
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    if (isPointInRegion(8, 35, 16, 16, mouseX, mouseY)) itemRender.renderItemIntoGUI(new ItemStack(Objs.backpack), guiLeft + 8, guiTop + 35)
    if (isPointInRegion(8, 53, 16, 16, mouseX, mouseY)) itemRender.renderItemIntoGUI(new ItemStack(Objs.knife), guiLeft + 8, guiTop + 53)
    if (isPointInRegion(196, 35, 16, 16, mouseX, mouseY)) itemRender.renderItemIntoGUI(new ItemStack(Objs.lantern), guiLeft + 196, guiTop + 35)
    if (isPointInRegion(196, 53, 16, 16, mouseX, mouseY)) itemRender.renderItemIntoGUI(new ItemStack(Items.FILLED_MAP), guiLeft + 196, guiTop + 53)
    super.drawGuiContainerBackgroundLayer(partTick, mouseX, mouseY)
  }
  override def drawGuiContainerForegroundLayer(mouseX:Int, mouseY:Int) {
    if(active(1)==0)this.fontRenderer.drawString(I18n.format("container.crafting", new java.lang.Object), 119, 8, 4210752)
  }
  def drawTab(left:Boolean, id:Int){
    if(id==1||id==2)mc.renderEngine.bindTexture(texture)
    getCleanGL
    if(id==0){ 
      this.mc.getTextureManager().bindTexture(inventoryTexture)
      this.drawTexturedModalRect(guiLeft + 29, guiTop+7, 7, 7, 164, 72)
      GuiInventory.drawEntityOnScreen(guiLeft + 51 + 22, guiTop + 75, 30, (guiLeft + 51 + 22 - lastMouseX).asInstanceOf[Float], (guiTop + 75 - 50 - lastMouseY).asInstanceOf[Float], this.mc.player)
    } else if(id==1){
      if(hasBackpack){
        if(pack==ItemDefinitions.Backpack.RUCKSACK)drawTexturedModalRect(guiLeft+29, guiTop+16, 29, 83, 162, 54)
        else if(pack==ItemDefinitions.Backpack.BACKPACK)drawTexturedModalRect(guiLeft + 83, guiTop + 16, 29, 83, 54, 54)
        else if(pack==ItemDefinitions.Backpack.POUCH)drawTexturedModalRect(guiLeft + 83, guiTop + 34, 0, 166, 54, 18)  
      }
    }
    else if(id==2)drawTexturedModalRect(guiLeft+52, guiTop+16, 0, 166, 115, 54)
    else {
      
    }
  }
}