package com.rikmuld.camping.misc

import java.util.{ArrayList, HashMap}

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.inventory.objs.GuiCampfireCook
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.misc.AbstractBox
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.{ItemRenderer, Tessellator}
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

abstract class CookingEquipment(var cookTime: Int, var maxFood: Int, var itemInfo: ItemStack) {
  var slots: Array[Array[Int]] = Array.ofDim[Int](2, maxFood)
  var cookableFoood = new HashMap[ItemStack, ItemStack]()
  var registerd = new ListBuffer[ItemStack]
  protected var renderer: ItemRenderer = _

  setSlots()
  CookingEquipment.addCooking(itemInfo, this)

  def addFood(stack: ItemStack, item: ItemStack) = if (canAdd(stack)) cookableFoood.put(stack, item)
  def canCook(stack: ItemStack): Boolean = cookableFoood.keySet.find(_.isItemEqual(stack)).map(_ => true).getOrElse(false)
  def canAdd(item: ItemStack): Boolean = {
    if (registerd contains item) return false
    registerd += item
    true
  }
  protected def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase): Unit
  def drawGuiTexture(container: GuiCampfireCook)
  def getBaseCookTime(): Int = cookTime
  def getCookedFood(stack: ItemStack): ItemStack = cookableFoood.keySet.find(_.isItemEqual(stack)).map(cookableFoood.get(_)).getOrElse(null)
  def renderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    if (renderer == null) {
      renderer = new ItemRenderer(Minecraft.getMinecraft)
    }
    if (foodIndex < maxFood &&
      (!(stack.getItem == Objs.parts) || (stack.getItemDamage != Parts.ASH))) {
      doRenderFood(foodIndex, stack, entity)
    }
  }
  def renderModel()
  def setSlots()
}

object CookingEquipment {
  var equipment = new HashMap[ItemStack, CookingEquipment]
  var equipmentRecipes = new HashMap[ArrayList[ItemStack], CookingEquipment]

  def addCooking(item: ItemStack, cookEquipment: CookingEquipment) = equipment.put(item, cookEquipment)
  def addEquipmentRecipe(equipment: CookingEquipment, items: ItemStack*) {
    val key = new ArrayList[ItemStack]()
    for (item <- items) key.add(item)
    equipmentRecipes.put(key, equipment)
  }
  def getCooking(item: ItemStack): CookingEquipment = equipment.keySet.find(_.isItemEqual(item)).map(equipment.get(_)).getOrElse(null)
  def getCookingForRecipe(items: Seq[ItemStack]): Option[CookingEquipment] = {
    var i = 0
    var flag = false
    for (list <- equipmentRecipes.keySet) {
      var cound = 0
      val copyItems = new ArrayList[List[Any]]()
      val recipes = new ArrayList[List[Any]]()
      for (item <- items) copyItems.add(List(item.getItem, item.getItemDamage))
      for (item <- list) recipes.add(List(item.getItem, item.getItemDamage))
      for (itemInfo <- recipes) {
        if (copyItems.contains(itemInfo)) {
          copyItems.remove(itemInfo)
          cound += 1
        }
        flag = flag || (cound == list.size) && (items.size == list.size)
      }
      if (!flag) i += 1
    }
    if (i < 3 && flag) Some(equipmentRecipes.values().toArray()(i).asInstanceOf[CookingEquipment]) else None
  }
}

class Grill(item: ItemStack) extends CookingEquipment(CampingMod.config.cookTimeGrill, 4, item) {
  var pilar: AbstractBox = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
  var line: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var line2: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 1, 1, 60, 0.015625F, 0.0F, 0.0F, 0.0F)
  var sLine: AbstractBox = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 29, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var sLine2: AbstractBox = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 1, 1, 29, 0.015625F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    val item = new ItemStack(stack.getItem, 1, stack.getItemDamage)
    GL11.glPushMatrix()
    GL11.glTranslatef(0.05f, -0.475f, 0.0225F)
    foodIndex match {
      case 0 => GL11.glTranslatef(-0.109375F, 0F, 0.046875F)
      case 1 => GL11.glTranslatef(0.109375F, 0F, 0.046875F)
      case 2 => GL11.glTranslatef(-0.109375F, 0F, -0.171875F)
      case 3 => GL11.glTranslatef(0.109375F, 0F, -0.171875F)
    }
    GL11.glScalef(0.3F, 0.3F, 0.3F)
    GL11.glRotatef(90, 0, 0, 1)
    if(entity!=null)renderer.renderItem(entity, item, TransformType.FIRST_PERSON_RIGHT_HAND)
    GL11.glPopMatrix()
  }
  override def drawGuiTexture(container: GuiCampfireCook) {
    container.drawTexturedModalRect(container.getGuiLeft + 67, container.getGuiTop + 7, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 89, container.getGuiTop + 7, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 67, container.getGuiTop + 27, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 89, container.getGuiTop + 27, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 47, container.getGuiTop + 39, 176, 115, 80, 63)
  }
  override def renderModel() {
    GL11.glPushMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0234375F, -0.5F, -0.4375F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0234375F, -0.5F, 0.40625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.4375F, -0.0078125F)
    line.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.015625F, -0.4375F, -0.46875F)
    line2.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
    for (i <- 0 until 15) {
      GL11.glPushMatrix()
      GL11.glTranslatef(-0.2343675F, -0.4453125F, -0.2265625F + (i * 0.03125F))
      sLine.render(Tessellator.getInstance.getBuffer)
      GL11.glPopMatrix()
    }
    for (i <- 0 until 15) {
      GL11.glPushMatrix()
      GL11.glTranslatef(-0.2343675F + (i * 0.03125F), -0.4453125F, -0.2265625F)
      sLine2.render(Tessellator.getInstance.getBuffer)
      GL11.glPopMatrix()
    }
    GL11.glPopMatrix()
  }
  override def setSlots() {
    slots(0)(0) = 68
    slots(1)(0) = 8
    slots(0)(1) = 90
    slots(1)(1) = 8
    slots(0)(2) = 68
    slots(1)(2) = 28
    slots(0)(3) = 90
    slots(1)(3) = 28
  }
}

class Pan(item: ItemStack) extends CookingEquipment(CampingMod.config.cookTimePan, 8, item) {
  var pilar: AbstractBox = new AbstractBox(64, 32, false, 0, 2, 0, 0, 0, 1, 28, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
  var line: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var cable: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 1, 45, 1, 0.0078625F, 0.0F, 0.0F, 0.0F)
  var pan: AbstractBox = new AbstractBox(32, 16, false, 2, 1, 0, 0, 0, 5, 3, 5, 0.0625F, 0.0F, 0.0F, 0.0F)
  var panCover: AbstractBox = new AbstractBox(64, 32, false, 4, 2, 0, 0, 0, 3, 1, 3, 0.0625F, 0.0F, 0.0F, 0.0F)
  var panHandle: AbstractBox = new AbstractBox(64, 32, false, 4, 13, 0, 0, 0, 1, 1, 1, 0.03125F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {}
  override def drawGuiTexture(container: GuiCampfireCook) {
    container.drawTexturedModalRect(container.getGuiLeft + 24, container.getGuiTop + 77, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 131, container.getGuiTop + 77, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 32, container.getGuiTop + 54, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 123, container.getGuiTop + 54, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 40, container.getGuiTop + 30, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 113, container.getGuiTop + 30, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 65, container.getGuiTop + 21, 7, 105, 18, 18)
    container.drawTexturedModalRect(container.getGuiLeft + 90, container.getGuiTop + 21, 7, 105, 18, 18)
  }
  override def renderModel() {
    GL11.glPushMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_PAN))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.875F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.875F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.859375F, -0.0078125F)
    line.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.15625F, -0.53125F, -0.15625F)
    pan.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.09375F, -0.5625F, -0.09375F)
    panCover.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.015625F, -0.578125F, -0.015625F)
    panHandle.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.00393125F, -0.8515125F, -0.00393125F)
    cable.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPopMatrix()
  }
  override def setSlots() {
    slots(0)(0) = 25
    slots(1)(0) = 78
    slots(0)(1) = 132
    slots(1)(1) = 78
    slots(0)(2) = 33
    slots(1)(2) = 55
    slots(0)(3) = 124
    slots(1)(3) = 55
    slots(0)(4) = 41
    slots(1)(4) = 31
    slots(0)(5) = 114
    slots(1)(5) = 31
    slots(0)(6) = 66
    slots(1)(6) = 22
    slots(0)(7) = 91
    slots(1)(7) = 22
  }
}
class Spit(item: ItemStack) extends CookingEquipment(CampingMod.config.cookTimeSpit, 2, item) {
  var pilar: AbstractBox = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
  var line: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    val item = new ItemStack(stack.getItem, 1, stack.getItemDamage)
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0525F, -0.435f, -0.025F)
    foodIndex match {
      case 0 => GL11.glTranslatef(-0.125F, 0F, 0F)
      case 1 => GL11.glTranslatef(0.125F, 0F, 0)
    }
    GL11.glScalef(0.35F, 0.35F, 0.35F)
    GL11.glRotatef(45, 0, -1, 0)
    GL11.glRotatef(-150, 1, 0, 1)
    if(entity!=null)renderer.renderItem(entity, item, TransformType.FIRST_PERSON_RIGHT_HAND)
    GL11.glPopMatrix()
  }
  override def drawGuiTexture(container: GuiCampfireCook) {
    container.drawTexturedModalRect(container.getGuiLeft + 48, container.getGuiTop + 26, 176, 44, 80, 68)
  }
  override def renderModel() {
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
    pilar.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.484375F, -0.0078125F)
    line.render(Tessellator.getInstance.getBuffer)
    GL11.glPopMatrix()
  }
  override def setSlots() {
    slots(0)(0) = 66
    slots(1)(0) = 27
    slots(0)(1) = 94
    slots(1)(1) = 27
  }
}