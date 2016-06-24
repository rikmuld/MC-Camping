package com.rikmuld.camping.inventory.gui

import net.minecraft.client.gui.inventory.GuiContainer
import org.lwjgl.opengl.GL11
import scala.collection.JavaConversions._
import java.util.Collection
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.client.gui.FontRenderer

trait GuiWithEffect extends GuiContainer {
  final val inventoryTexture = new ResourceLocation("textures/gui/container/inventory.png")
  var activeEffects = false
  var leftGui:Int = _
  var topGui:Int = _
  var fontRenderer:FontRenderer = _
  
  def init(left:Int, top:Int, fontRender:FontRenderer) {
    if (!this.mc.thePlayer.getActivePotionEffects().isEmpty())activeEffects = true
    leftGui = left
    topGui = top
    fontRenderer = fontRender
  }
  def draw {
    if(activeEffects){
      val i = leftGui - 124
      var j = topGui
      val flag = true
      val collection = this.mc.thePlayer.getActivePotionEffects
      if (!collection.isEmpty()){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
        GL11.glDisable(GL11.GL_LIGHTING)
        var k = 33
        if (collection.size() > 5) k = 132 / (collection.size() - 1);
        this.mc.thePlayer.getActivePotionEffects.asInstanceOf[Collection[PotionEffect]].foreach { potioneffect =>
          val potion = potioneffect.getPotion
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
          this.mc.getTextureManager().bindTexture(inventoryTexture)
          this.drawTexturedModalRect(i, j, 0, 166, 140, 32)

          if (potion.hasStatusIcon){
              val l = potion.getStatusIconIndex
              this.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18)
          }

          potion.renderInventoryEffect(i, j, potioneffect, mc)
          if (potion.shouldRenderInvText(potioneffect)){
            var s1 = I18n.format(potion.getName(), new Object)

            if (potioneffect.getAmplifier() == 1)s1 = s1 + " " + I18n.format("enchantment.level.2", new Object)
            else if (potioneffect.getAmplifier() == 2)s1 = s1 + " " + I18n.format("enchantment.level.3", new Object)
            else if (potioneffect.getAmplifier() == 3)s1 = s1 + " " + I18n.format("enchantment.level.4", new Object)
            
            fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215)
            val s = potioneffect.getDuration
            fontRenderer.drawStringWithShadow(s.toString(), i + 10 + 18, j + 6 + 10, 8355711)
          }
          j += k
        }
      }
    }
  }
}