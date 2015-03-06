package com.rikmuld.camping.client.gui.button

import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.OpenGlHelper
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.client.gui.GuiCampinginv
import com.rikmuld.camping.core.Objs

class ButtonItem(id:Int, x:Int, y:Int, stack:ItemStack) extends GuiButton(id, x, y, 16, 16, "") {
  val render = new RenderItem
  
  override def drawButton(mc:Minecraft, mouseX:Int, mouseY:Int) {    
    if (this.visible){
      GL11.glColor3f(1, 1, 1)
      GL11.glEnable(GL11.GL_BLEND)
      OpenGlHelper.glBlendFunc(770, 771, 1, 0)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
      mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(stack.getItemSpriteNumber()))
      this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height
      val hoverState = this.getHoverState(this.field_146123_n)
      if(hoverState==1)GL11.glColor3f(0.75f, 0.75f, 0.75f)
      if(!enabled)GL11.glColor3f(0.4f, 0.4f, 0.4f)
      this.render.renderIcon(xPosition, yPosition, stack.getIconIndex, width, height)
      this.mouseDragged(mc, mouseX, mouseY)
    }
  }
}

class ButtonTabbed(id:Int, x:Int, y:Int, width:Int, height:Int, text:String, top:Int, left:Int) extends GuiButton(id, x, y, width, height, text) with ButtonWithTab {
  setTab(left, top)
}

class ButtonCampinv(gui:GuiCampinginv, id:Int, x:Int, y:Int, width:Int, height:Int, imgId:Int) extends GuiButton(id, x, y, width, height, "") with ButtonWithTab {
  setTab(0, 3)
  enabled = Objs.config.prmInv!=id
  
  val imgW = 27
  val imgH = 16
  final val tex = new ResourceLocation(TextureInfo.GUI_CAMPINV)

  override def drawButton(mc:Minecraft, mouseX:Int, mouseY:Int){
    super.drawButton(mc, mouseX, mouseY)
    mc.renderEngine.bindTexture(tex)
    if(visible)drawTexturedModalRect(xPosition+(width-27)/2, yPosition+(height-16)/2, imgW*imgId, 220, imgW, imgH)
  }
  def drawHoverd(mouseX:Int, mouseY:Int) = if(this.getHoverState(this.field_146123_n)==2)gui.drawHoverdButton(id, mouseX, mouseY)
}

trait ButtonWithTab extends GuiButton {
  visible = false
  
  var left = 0
  var top = 0
  
  def setTab(left:Int, top:Int){
    this.left = left
    this.top = top
  }
  def tabChanged(tabLeft:Int, tabTop:Int){
    if(left==tabLeft&&top==tabTop){
      if(!visible)visible = true
    } else if(visible)visible = false
  }
}