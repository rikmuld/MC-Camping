package com.rikmuld.camping.client.gui.button

import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.OpenGlHelper

class ButtonItem(id:Int, x:Int, y:Int, stack:ItemStack) extends GuiButton(id, x, y, 16, 16, "") {
  val render = new RenderItem();
  
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