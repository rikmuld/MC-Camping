package com.rikmuld.camping.features.inventory

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{FontRenderer, GuiScreen}
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._

//TODO not working anymore
object GuiEffect {
  final val inventoryTexture = new ResourceLocation("textures/gui/container/inventory.png")

  def draw(left: Int, top:Int, parent: GuiScreen, mc: Minecraft, fontRenderer: FontRenderer) {
    if(!mc.player.getActivePotionEffects.isEmpty){
      val i = left - 124
      var j = top
      val flag = true
      val collection = mc.player.getActivePotionEffects
      if (!collection.isEmpty){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
        GL11.glDisable(GL11.GL_LIGHTING)
        var k = 33
        if (collection.size() > 5) k = 132 / (collection.size() - 1)
        mc.player.getActivePotionEffects.foreach { potioneffect =>
          val potion = potioneffect.getPotion
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
          mc.getTextureManager.bindTexture(GuiEffect.inventoryTexture)
          parent.drawTexturedModalRect(i, j, 0, 166, 140, 32)

          if (potion.hasStatusIcon){
              val l = potion.getStatusIconIndex
              parent.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18)
          }

          potion.renderInventoryEffect(i, j, potioneffect, mc)
          if (potion.shouldRenderInvText(potioneffect)){
            var s1 = I18n.format(potion.getName, new Object)

            if (potioneffect.getAmplifier == 1)s1 = s1 + " " + I18n.format("enchantment.level.2", new Object)
            else if (potioneffect.getAmplifier == 2)s1 = s1 + " " + I18n.format("enchantment.level.3", new Object)
            else if (potioneffect.getAmplifier == 3)s1 = s1 + " " + I18n.format("enchantment.level.4", new Object)

            fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215)
            val s = potioneffect.getDuration
            fontRenderer.drawStringWithShadow(s.toString, i + 10 + 18, j + 6 + 10, 8355711)
          }
          j += k
        }
      }
    }
  }
}