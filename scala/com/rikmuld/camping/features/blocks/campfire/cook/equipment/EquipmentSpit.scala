package com.rikmuld.camping.features.blocks.campfire.cook.equipment

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Definitions.Kit
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11


class EquipmentSpit extends CookingEquipment(Kit.SPIT,
  config.cookTimeSpit, 2, ModelCookingEquipment.SPIT) {

  override def renderGUIBackground(gui: GuiContainer): Unit =
    gui.drawTexturedModalRect(gui.getGuiLeft + 48, gui.getGuiTop + 26, 176, 44, 80, 68)

  override protected def doRenderFood(render: RenderItem, slot: Int, food: ItemStack): Unit = {
    food.setCount(1)

    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0525F, -0.435f, -0.025F)

    slot match {
      case 0 => GL11.glTranslatef(-0.125f, 0, 0)
      case 1 => GL11.glTranslatef(0.125f, 0, 0)
    }

    GL11.glScalef(0.35F, 0.35F, 0.35F)
    GL11.glRotatef(45, 0, -1, 0)
    GL11.glRotatef(-150, 1, 0, 1)

    render.renderItem(food, TransformType.FIRST_PERSON_RIGHT_HAND)

    GL11.glPopMatrix()
  }

  override def getSlotPosition(slot: Int): (Int, Int) = slot match {
    case 0 => (66, 27)
    case 1 => (94, 27)
  }
}