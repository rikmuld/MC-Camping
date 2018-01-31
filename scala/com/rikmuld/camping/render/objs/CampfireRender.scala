//package com.rikmuld.camping.render.objs
//
//import java.util.Random
//
//import com.rikmuld.camping.tileentity.{TileCampfire, TileCampfireCook}
//import net.minecraft.client.Minecraft
//import net.minecraft.client.renderer.ItemRenderer
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
//import net.minecraft.init.Items
//import net.minecraft.item.ItemStack
//import org.lwjgl.opengl.{GL11, GL12}
//
//class CampfireRender extends TileEntitySpecialRenderer[TileCampfire] {
//  var renderer: ItemRenderer = Minecraft.getMinecraft.getItemRenderer
//  var rand: Random = new Random()
//
//  override def render(tile: TileCampfire, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {
//    if(!tile.renderCoal)return
//    GL11.glPushMatrix()
//    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
//    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)
//    val coalItem = new ItemStack(Items.COAL, 1, 0)
//    for (i <- 0 until 20) {
//      GL11.glPushMatrix()
//      GL11.glTranslatef(tile.coals(0)(i), 0, tile.coals(1)(i))
//      GL11.glScalef(0.2F, 0.15F, 0.15F)
//      GL11.glRotatef(tile.coals(2)(i), 0, 1, 0)
//      GL11.glRotatef(45, 1F, 1F, 0.4F)
//      GL11.glRotatef(10, 0.0F, 1F, 0F)
//      GL11.glRotatef(5, 0.0F, 0F, -0.2F)
//      renderer.renderItem(tile.getWorld.getClosestPlayer(x, y, z, -1, false), coalItem, TransformType.FIRST_PERSON_RIGHT_HAND)
//      GL11.glPopMatrix()
//    }
//    GL11.glPopMatrix()
//  }
//}
//
//class CampfireCookRender extends TileEntitySpecialRenderer[TileCampfireCook] {
//  var renderer: ItemRenderer = Minecraft.getMinecraft.getItemRenderer
//  var rand: Random = new Random()
//
//  override def render(tile: TileCampfireCook, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {
//    GL11.glPushMatrix
//    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
//    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat, z.toFloat + 0.5F)
//    GL11.glScalef(1.0F, -1F, -1F)
//    if (Option(tile.equipment).isDefined) {
//      tile.equipment.renderModel
//      val entity = tile.getWorld.getClosestPlayer(x, y, z, -1, false)
//      for (i <- 0 until tile.equipment.maxFood if Option(tile.getStackInSlot(i + 2)).isDefined) {
//        tile.equipment.renderFood(i, tile.getStackInSlot(i + 2), entity)
//      }
//    }
//    GL11.glPopMatrix
//    GL11.glPushMatrix
//    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)
//    val coalItem = new ItemStack(Items.COAL, 1, 0)
//    for (i <- 0 until tile.getCoalPieces) {
//      GL11.glPushMatrix()
//      GL11.glTranslatef(tile.coals(0)(i), 0, tile.coals(1)(i))
//      GL11.glScalef(0.3F, 0.3F, 0.3F)
//      GL11.glRotatef(tile.coals(2)(i), 0, 1, 0)
//      GL11.glRotatef(45, 1F, 1F, 0.4F)
//      GL11.glRotatef(10, 0.0F, 1F, 0F)
//      GL11.glRotatef(5, 0.0F, 0F, -0.2F)
//      renderer.renderItem(tile.getWorld.getClosestPlayer(x, y, z, -1, false), coalItem, TransformType.FIRST_PERSON_RIGHT_HAND)
//      GL11.glPopMatrix()
//    }
//    GL11.glPopMatrix()
//  }
//}