package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.features.tabbed.ButtonWithTab
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class ButtonItem(id:Int, xx:Int, yy:Int, stack:ItemStack) extends GuiButton(id, xx, yy, 16, 16, "") {
  val render = Minecraft.getMinecraft.getRenderItem
  
  override def drawButton(mc:Minecraft, mouseX:Int, mouseY:Int, partial: Float) {
    if (this.visible){
      GL11.glColor3f(1, 1, 1)
      GL11.glEnable(GL11.GL_BLEND)
      OpenGlHelper.glBlendFunc(770, 771, 1, 0)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
      hovered = mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height
      val hoverState = this.getHoverState(hovered)
      if(hoverState==1)GL11.glColor3f(0.75f, 0.75f, 0.75f)
      if(!enabled)GL11.glColor3f(0.4f, 0.4f, 0.4f)
      this.render.renderItemIntoGUI(stack, x, y)
      this.mouseDragged(mc, mouseX, mouseY)
    }
  }
}

class ButtonTabbed(i:Int, xPos:Int, yPos:Int, w:Int, h:Int, val left: Int, val top: Int) extends GuiButton(i, xPos, yPos, w, h, "") with ButtonWithTab