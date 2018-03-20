package com.rikmuld.camping.features.blocks.trap

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType._
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.{GL11, GL12}

class RendererTrap extends TileEntitySpecialRenderer[TileEntityTrap] {
  override def render(tile: TileEntityTrap, x: Double, y: Double, z: Double,
                      partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {

    val stack = tile.getStackInSlot(0)

    if (!stack.isEmpty) {
      GL11.glPushMatrix()

      GL11.glEnable(GL12.GL_RESCALE_NORMAL)
      GL11.glTranslatef((x + 0.5 + 0.03125F).toFloat, y.toFloat + 0.03125F, ((z + 0.51) - 0.0625F/2).toFloat)
      GL11.glRotatef(90, 0, 0, 1)
      GL11.glRotatef(180, 1, 0, 0)
      GL11.glScalef(0.2F, -0.2F, -0.2F)

      Minecraft.getMinecraft.getRenderItem.renderItem(stack, FIRST_PERSON_LEFT_HAND)

      GL11.glPopMatrix()
    }
  }
}