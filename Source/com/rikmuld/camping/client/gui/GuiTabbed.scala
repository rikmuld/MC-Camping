package com.rikmuld.camping.client.gui

import java.util.ArrayList
import java.util.List
import scala.collection.JavaConversions._
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.OpenGlHelper
import scala.collection.mutable.ListBuffer
import com.rikmuld.corerm.common.network.PacketSender
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.common.inventory.gui.ContainerTabbed
import com.rikmuld.camping.client.gui.button.ButtonWithTab
import net.minecraft.client.renderer.InventoryEffectRenderer

abstract class GuiTabbed(player:EntityPlayer, container: Container) extends GuiContainer(container) {
  var tabsLeft: ListBuffer[GuiTab] = new ListBuffer[GuiTab]()
  var tabsTop: ListBuffer[GuiTab] = new ListBuffer[GuiTab]()
  var active: Array[Int] = Array(0, 0)
  var itemRender: RenderItem = new RenderItem()
  var texture: ResourceLocation = _
  
  def this(player:EntityPlayer, container:Container, texture:ResourceLocation){
    this(player, container)
    this.texture = texture
  }
  override def initGui() {
    super.initGui()
    tabsLeft.clear()
    tabsTop.clear()
    initTabbed  
    updateButtons(0, 0)
    setTabLeftActive(0)
    setTabTopActive(0)
  }
  def initTabbed
  def getFontRenderer = fontRendererObj
  def getRenderEngine = mc.renderEngine
  override def drawScreen(x: Int, y: Int, par3: Float) {
    super.drawScreen(x, y, par3)
    for (tab <- tabsLeft if func_146978_c(tab.guiStartX - guiLeft + tab.guiWidth, tab.guiStartY - guiTop + 10 + (tabsLeft.indexOf(tab) * 28),23, 25, x, y)){
      if (Mouse.isButtonDown(0)&&tab.enabled) this.setTabLeftActive(tab.id)
      this.drawHoveringText(tab.getHoveringText, x, y, fontRendererObj) 
    }
    for (tab <- tabsTop if func_146978_c(tab.guiStartX - guiLeft + 4 + (tabsTop.indexOf(tab) * 26),tab.guiStartY - guiTop - 20, 24, 20, x, y)){
      if (Mouse.isButtonDown(0)&&tab.enabled) this.setTabTopActive(tab.id)
      this.drawHoveringText(tab.getHoveringText, x, y, fontRendererObj) 
    }
  }
  def updateButtons(left:Int, top:Int) = buttonList.foreach(button => {if(button.isInstanceOf[ButtonWithTab]) button.asInstanceOf[ButtonWithTab].tabChanged(left, top)})
  def updateContainer(left:Int, top:Int) = player.openContainer.asInstanceOf[ContainerTabbed].updateTab(player, left, top)
  def setTabTopActive(id: Int) {
    updateContainer(active(0), id)
    tabsTop.foreach(tab => {
      tab.active = tab.id==id
      if(tab.active)active(1)=tab.id 
    })
    updateButtons(active(0), id)
  }
  def setTabLeftActive(id: Int) {
    updateContainer(id, active(1))
    tabsLeft.foreach(tab => {
      tab.active = tab.id==id
      if(tab.active)active(0)=tab.id 
    })
    updateButtons(id, active(1))
  }
  def getCleanGL(){
    GL11.glColor4f(1, 1, 1, 1)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glEnable(GL11.GL_BLEND)
    OpenGlHelper.glBlendFunc(770, 771, 1, 0)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
  }
  def drawTab(left:Boolean, id:Int)
  private def getActiveLeftPage(): Int = tabsLeft.find(_.active).map(_.id).getOrElse(-1)
  private def getActiveTopPage(): Int = tabsTop.find(_.active).map(_.id).getOrElse(-1)
  protected override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
    for (tab <- tabsLeft)if(active(0)==tab.id)drawTab(true, tab.id)
    for (tab <- tabsTop)if(active(1)==tab.id)drawTab(false, tab.id)
    getCleanGL
    for (tab <- tabsLeft) tab.drawScreen(this)
    for (tab <- tabsTop) tab.drawScreen(this)
    if(texture!=null){
      mc.renderEngine.bindTexture(texture)
      for (tab <- tabsLeft) tab.drawBackgroundTexture(this)
      for (tab <- tabsTop) tab.drawBackgroundTexture(this) 
    }
    for (tab <- tabsLeft) tab.drawBackgroundItem(this)
    for (tab <- tabsTop) tab.drawBackgroundItem(this)
  }
  def addLeftTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int, texture: ItemStack) = tabsLeft.append(new GuiTab(name, xSize, ySize, startX, startY, id, texture, 0))
  def addLeftTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int) = tabsLeft.append(new GuiTab(name, xSize, ySize, startX, startY, id, 0))
  def addLeftTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int, t1: Int, t2: Int, t3: Int, t4: Int) = tabsLeft.append(new GuiTab(name, xSize, ySize, startX, startY, id, t1, t2, t3, t4, 0))
  def addTopTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int, texture: ItemStack) = tabsTop.append(new GuiTab(name, xSize, ySize, startX, startY, id, texture, 1))
  def addTopTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int) = tabsTop.append(new GuiTab(name, xSize, ySize, startX, startY, id, 1))
  def addTopTab(name: String, xSize: Int, ySize: Int, startX: Int, startY: Int, id: Int, t1: Int, t2: Int, t3: Int, t4: Int) = tabsTop.append(new GuiTab(name, xSize, ySize, startX, startY, id, t1, t2, t3, t4, 1))
}

class GuiTab(var name: String, var guiWidth: Int, var guiHeigth: Int, xStart: Int, yStart: Int, tabID: Int, var side: Int) {
  final val TEXT_UTILS_TAB = new ResourceLocation(TextureInfo.GUI_TAB_UTILS)  
 
  private var zLevel: Float = 0
  var guiStartX: Int = xStart
  var guiStartY: Int = yStart
  var id: Int = tabID
  var active: Boolean = (id == 0)
  var stack: ItemStack = _
  var mc: Minecraft = Minecraft.getMinecraft
  var textureInfo: Array[Int] = _
  var enabled:Boolean = true
  
  def this(name: String, guiWidth: Int, guiHeigth: Int, xStart: Int, yStart: Int, tabID: Int, item: ItemStack, side: Int) {
    this(name, guiWidth, guiHeigth, xStart, yStart, tabID, side)
    this.stack = item
  }
  def this(name: String, guiWidth: Int, guiHeigth: Int, xStart: Int, yStart: Int, tabID: Int, t1: Int, t2: Int, t3: Int, t4: Int, side: Int) {
    this(name, guiWidth, guiHeigth, xStart, yStart, tabID, side)
    this.textureInfo = Array(t1, t2, t3, t4)
  }
  def getHoveringText(): List[String] = scala.List(name)
  def drawScreen(guiTab:GuiTabbed) {
    mc.renderEngine.bindTexture(TEXT_UTILS_TAB)
    val xStart = if(side==0)guiStartX + guiWidth - 3 else guiStartX + 4
    val yStart = if(side==0)guiStartY + 10 else guiStartY + 3 - 23
    
    if(!enabled&&(!active))GL11.glColor3f(.5f, .5f, .5f)
    if (side==0){
      if(active)guiTab.drawTexturedModalRect(xStart, yStart + (id * 28), 0, 46, 26, 26) 
      else guiTab.drawTexturedModalRect(xStart, yStart + (id * 28), 0, 20, 26, 26)
    } else {
      if (active) guiTab.drawTexturedModalRect(xStart + (id * 26), yStart, 26, 43, 24, 23) 
      else guiTab.drawTexturedModalRect(xStart + (id * 26), yStart, 26, 20, 24, 23)
    }
    if(!enabled)GL11.glColor3f(1, 1, 1)
  }
  def drawBackgroundItem(guiTab:GuiTabbed){
    GL11.glDisable(GL11.GL_LIGHTING)
    val xStart = if(side==0)guiStartX + guiWidth + 2 else guiStartX + 8
    val yStart = if(side==0)guiStartY + 15 else guiStartY - 17
    if(stack!=null)guiTab.itemRender.renderItemAndEffectIntoGUI(guiTab.getFontRenderer, guiTab.getRenderEngine, stack, xStart + (if(side==0) 0 else (id * 26)), yStart + (if(side==0)(id * 28)else 0))
  }
  def drawBackgroundTexture(guiTab:GuiTabbed){
    val xStart = if(side==0)guiStartX + guiWidth + 2 else guiStartX + 8
    val yStart = if(side==0)guiStartY + 15 else guiStartY - 17
        
    if(textureInfo!=null) guiTab.drawTexturedModalRect(xStart + (if(side==0) 0 else (id * 26)), yStart + (if(side==0)(id * 28)else 0), textureInfo(0), textureInfo(1), textureInfo(2), textureInfo(3))
  }
}