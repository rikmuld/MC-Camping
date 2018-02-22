package com.rikmuld.camping.render.objs

import java.util.Random

import com.rikmuld.camping.render.objs.CampfireCookRender._
import com.rikmuld.camping.tileentity.TileCampfireCook
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType._
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.{GL11, GL12}

object CampfireCookRender {
  val random =
    new Random() //TODO get rid of all random everywhere and just have one for all

  lazy val renderer: RenderItem =
    Minecraft.getMinecraft.getRenderItem

  final val COAL =
    new ItemStack(Items.COAL, 1, 0)

  final val COALS =
    for (i <- 0 until 20)
      yield (
        random.nextFloat() / 5F,
        random.nextFloat() / 5F,
        random.nextFloat() * 360
      )
}

class CampfireCookRender extends TileEntitySpecialRenderer[TileCampfireCook] {
  override def render(tile: TileCampfireCook, x: Double, y: Double, z: Double,
                      partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {

    GL11.glPushMatrix()

    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat, z.toFloat + 0.5F)
    GL11.glScalef(1.0F, -1F, -1F)

    tile.getEquipment.foreach(equipment => {
      equipment.getModel.renderAll(1)

      for (i <- 0 until equipment.getMaxCookingSlot)
        equipment.renderFood(renderer, i, tile.getStackInSlot(i + 2))
    })

    GL11.glPopMatrix()

    GL11.glPushMatrix()
    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)

    for (i <- 0 until tile.getCoalPieces)
      renderCoalPiece(i, tile, x, y, z)

    GL11.glPopMatrix()
  }

  //TODO simplify
  def renderCoalPiece(piece: Int, tile: TileCampfireCook,
                      x: Double, y: Double, z: Double): Unit = {

    GL11.glPushMatrix()

    GL11.glTranslatef(COALS(piece)._1, 0, COALS(piece)._2)
    GL11.glScalef(0.3F, 0.3F, 0.3F)

    GL11.glRotatef(COALS(piece)._3, 0, 1, 0)
    GL11.glRotatef(45, 1F, 1F, 0.4F)
    GL11.glRotatef(10, 0.0F, 1F, 0F)
    GL11.glRotatef(5, 0.0F, 0F, -0.2F)

    renderer.renderItem(COAL, FIRST_PERSON_RIGHT_HAND)

    GL11.glPopMatrix()
  }
}