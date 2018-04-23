package com.rikmuld.camping.features.blocks.campfire.cook.equipment

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Definitions.Kit
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11

class EquipmentGrill extends CookingEquipment(Kit.GRILL,
  CONFIG.cookTimeGrill, 4, ModelCookingEquipment.GRILL) {

  private val spitParts = Seq(
    "pillar1", "pillar2", "pillar3", "pillar4", "line1", "line2"
  )

  override def renderGUIBackground(gui: GuiContainer): Unit = {
    gui.drawTexturedModalRect(gui.getGuiLeft + 47, gui.getGuiTop + 39, 176, 115, 80, 63)

    super.renderGUIBackground(gui)
  }

  override protected def doRenderFood(render: RenderItem, slot: Int, food: ItemStack): Unit = {
    food.setCount(1)

    GL11.glPushMatrix()
    GL11.glTranslatef(0.05f, -0.475f, 0.0225F)

    slot match {
      case 0 => GL11.glTranslatef(-0.109375f, 0, 0.046875f)
      case 1 => GL11.glTranslatef(0.109375f, 0, 0.046875f)
      case 2 => GL11.glTranslatef(-0.109375f, 0, -0.171875f)
      case 3 => GL11.glTranslatef(0.109375f, 0, -0.171875f)
    }

    GL11.glScalef(0.3F, 0.3F, 0.3F)
    GL11.glRotatef(90, 0, 0, 1)

    render.renderItem(food, TransformType.FIRST_PERSON_RIGHT_HAND)

    GL11.glPopMatrix()
  }

  override def getSlotPosition(slot: Int): (Int, Int) = slot match {
    case 0 => (68, 8)
    case 1 => (90, 8)
    case 2 => (68, 28)
    case 3 => (90, 28)
  }

  override def renderInWorld(): Unit = {
    getModel.bindTexture(CookingEquipment.TEXTURE_SPIT)
    getModel.renderOnly(spitParts:_*)

    getModel.bindTexture(CookingEquipment.TEXTURE_GRILL)
    getModel.renderExcept(spitParts:_*)
  }
}