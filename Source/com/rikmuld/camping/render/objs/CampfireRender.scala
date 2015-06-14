package com.rikmuld.camping.render.objs

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import com.rikmuld.camping.objs.Objs
import net.minecraft.util.ResourceLocation
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import java.util.Random
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.camping.Lib.TextureInfo
import com.rikmuld.camping.objs.tile.TileCampfireCook

class CampfireRender extends TileEntitySpecialRenderer {
  var renderer: ItemRenderer = Minecraft.getMinecraft.getItemRenderer
  var rand: Random = new Random()

  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float, i:Int) {
    val tile = tileentity.asInstanceOf[TileCampfire]
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)
    val coalItem = new ItemStack(Items.coal, 1, 0)
    for (i <- 0 until 20) {
      GL11.glPushMatrix()
      GL11.glTranslatef(tile.coals(0)(i), 0, tile.coals(1)(i))
      GL11.glScalef(0.2F, 0.15F, 0.15F)
      GL11.glRotatef(tile.coals(2)(i), 0, 1, 0)
      GL11.glRotatef(45, 1F, 1F, 0.4F)
      GL11.glRotatef(10, 0.0F, 1F, 0F)
      GL11.glRotatef(5, 0.0F, 0F, -0.2F)
      renderer.renderItem(tileentity.getWorld.getClosestPlayer(x, y, z, -1), coalItem, TransformType.FIRST_PERSON)
      GL11.glPopMatrix()
    }
    GL11.glPopMatrix()
  }
}

class CampfireCookRender extends TileEntitySpecialRenderer {
  var renderer: ItemRenderer = Minecraft.getMinecraft.getItemRenderer
  var rand: Random = new Random()

  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float, i:Int) {
    val tile = tileentity.asInstanceOf[TileCampfireCook]
    GL11.glPushMatrix
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat, z.toFloat + 0.5F)
    GL11.glScalef(1.0F, -1F, -1F)
    if (Option(tile.equipment).isDefined) {
      tile.equipment.renderModel
      val entity = tileentity.getWorld.getClosestPlayer(x, y, z, -1)
      for (i <- 0 until tile.equipment.maxFood if Option(tile.getStackInSlot(i + 2)).isDefined) {
        tile.equipment.renderFood(i, tile.getStackInSlot(i + 2), entity)
      }
    }
    GL11.glPopMatrix
    GL11.glPushMatrix
    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)
    val coalItem = new ItemStack(Items.coal, 1, 0)
    for (i <- 0 until tile.getCoalPieces) {
      GL11.glPushMatrix()
      GL11.glTranslatef(tile.coals(0)(i), 0, tile.coals(1)(i))
      GL11.glScalef(0.2F, 0.15F, 0.15F)
      GL11.glRotatef(tile.coals(2)(i), 0, 1, 0)
      GL11.glRotatef(45, 1F, 1F, 0.4F)
      GL11.glRotatef(10, 0.0F, 1F, 0F)
      GL11.glRotatef(5, 0.0F, 0F, -0.2F)
      renderer.renderItem(tileentity.getWorld.getClosestPlayer(x, y, z, -1), coalItem, TransformType.FIRST_PERSON)
      GL11.glPopMatrix()
    }
    GL11.glPopMatrix()
  }
}