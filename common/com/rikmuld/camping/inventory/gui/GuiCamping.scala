package com.rikmuld.camping.inventory.gui

import org.lwjgl.opengl.GL11
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import scala.collection.JavaConversions._
import com.rikmuld.camping.Lib._
import scala.collection.JavaConversions._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import com.rikmuld.camping.inventory.container.ContainerCampinv
import com.rikmuld.camping.objs.Objs
import net.minecraft.client.gui.inventory.GuiInventory
import com.rikmuld.camping.CampingMod._
import com.rikmuld.corerm.tabbed.ButtonTabbed
import com.rikmuld.corerm.tabbed.GuiTabbed
import net.minecraft.client.Minecraft
import com.rikmuld.corerm.RMMod
import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.objs.ItemDefinitions

object GuiCampinginv {
  final val TAB_ARMOR = 0
  final val TAB_BACKPACK = 1
  final val TAB_CRAFTING = 2
  final val TAB_CONFIG = 3
}

class GuiCampinginv(var player: EntityPlayer) extends GuiTabbed(player, new ContainerCampinv(player), new ResourceLocation(TextureInfo.GUI_CAMPINV)) with GuiWithEffect {
  this.xSize = 220
  this.ySize = 166
  
  var hasBackpack = false
  var hasKnife = false
  var lastMouseX = 0
  var lastMouseY = 0
  
  var pack = 0
           
  override def initGui {
    super.initGui 
    init(guiLeft, guiTop, fontRendererObj)
  }
  override def initTabbed {
    addTopTab("Armor", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Items.SKULL, 1, 3))
    addTopTab("Backpack", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Objs.backpack))
    addTopTab("Crafting", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, new ItemStack(Objs.knife))
    addTopTab("Configuration", xSize, ySize, guiLeft+24, guiTop, tabsTop.size, 220, 0, 16, 16)
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonCampinv(this, 2, guiLeft+(xSize-40)/2-45, guiTop+10, 40, 20, 0))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonCampinv(this, 1, guiLeft+(xSize-40)/2, guiTop+10, 40, 20, 2))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonCampinv(this, 0, guiLeft+(xSize-40)/2+45, guiTop+10, 40, 20, 1))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonTabbed(3, guiLeft+(xSize)/2-64, guiTop+35, 128, 12, "Middle", 3, 0){
      enabled = config.prmInv==1&&config.secInv!=id-3})
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonTabbed(4, guiLeft+(xSize)/2-64, guiTop+47, 66, 15, "L-Top", 3, 0){
      enabled = config.prmInv==1&&config.secInv!=id-3})
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonTabbed(5, guiLeft+(xSize)/2, guiTop+47, 65, 15, "R-Top", 3, 0){
      enabled = config.prmInv==1&&config.secInv!=id-3})
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonTabbed(6, guiLeft+(xSize)/2-64, guiTop+62, 66, 15, "L-Bottom", 3, 0){
      enabled = config.prmInv==1&&config.secInv!=id-3})
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new ButtonTabbed(7, guiLeft+(xSize)/2, guiTop+62, 65, 15, "R-Bottom", 3, 0){
      enabled = config.prmInv==1&&config.secInv!=id-3})
  }
  def setContents(){
    val backpack = this.inventorySlots.asInstanceOf[ContainerCampinv].campinv.getStackInSlot(0)
    
    hasKnife = (this.inventorySlots.asInstanceOf[ContainerCampinv].campinv.getStackInSlot(1) != null)
    if(backpack!=null){
      hasBackpack = true
      pack = backpack.getItemDamage
    } else hasBackpack = false
  }
  override def setTabTopActive(id: Int) {
    if(id == GuiCampinginv.TAB_CONFIG){
      val gui =  new ConfigGUI(this)
      gui.setWorldAndResolution(mc, width, height)
      gui.initGui()
      mc.currentScreen = gui
    } else super.setTabTopActive(id)
  }
  def drawHoverdButton(id:Int, mouseX:Int, mouseY:Int){
    val list = new java.util.ArrayList[String]()
    if(id==2){
      list.add("Don't Edit MC Inventory")
      drawHoveringText(list, mouseX, mouseY, fontRendererObj)
    } else if(id==0){
      list.add("Replace MC Inventory")
      drawHoveringText(list, mouseX, mouseY, fontRendererObj)
    } else if(id==1){
      list.add("Use Button in MC Inventory")
      drawHoveringText(list, mouseX, mouseY, fontRendererObj)
    }
  }
  override def actionPerformed(button:GuiButton){
    if(button.id<3){
      config.setInv(button.id, config.secInv)
      buttonList.asInstanceOf[java.util.List[GuiButton]].foreach { 
        but2 => but2.enabled = if(but2.id<3) but2.id!=config.prmInv else config.prmInv==1&&config.secInv!=but2.id-3 
      }
    } else {
      config.setInv(config.prmInv, button.id-3)
      buttonList.asInstanceOf[java.util.List[GuiButton]].filter(_.id>=3).foreach { 
        but2 => but2.enabled = config.prmInv==1&&config.secInv!=but2.id-3 
      }
    }
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
    if(active(1)==0)this.fontRendererObj.drawString(I18n.format("container.crafting", new java.lang.Object), 108, 16, 4210752)
  }
  override def drawScreen(mouseX:Int, mouseY:Int, partialTick:Float){
    super.drawScreen(mouseX, mouseY, partialTick)
    buttonList.foreach(button => if(button.isInstanceOf[ButtonCampinv])button.asInstanceOf[ButtonCampinv].drawHoverd(mouseX, mouseY))
  }
  def drawTab(left:Boolean, id:Int){
    if(id==1||id==2)mc.renderEngine.bindTexture(texture)
    getCleanGL
    if(id==0){ 
      this.mc.getTextureManager().bindTexture(inventoryTexture)
      this.drawTexturedModalRect(guiLeft + 29, guiTop+7, 7, 7, 154, 72);
      GuiInventory.drawEntityOnScreen(guiLeft + 51 + 22, guiTop + 75, 30, (guiLeft + 51 + 22 - lastMouseX).asInstanceOf[Float], (guiTop + 75 - 50 - lastMouseY).asInstanceOf[Float], this.mc.thePlayer);
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