package com.rikmuld.camping.misc

import java.util.HashMap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.PartInfo
import scala.collection.JavaConversions._
import java.util.ArrayList
import java.util.Arrays
import com.rikmuld.camping.core.Objs
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import org.lwjgl.opengl.GL11
import com.rikmuld.camping.core.PartInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.PartInfo
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.PartInfo

abstract class CookingEquipment(var cookTime: Int, var cookableFoood: HashMap[ItemStack, ItemStack], var maxFood: Int, var itemInfo: ItemStack) {
  var slots: Array[Array[Int]] = Array.ofDim[Int](2, maxFood)
  protected var renderer: ItemRenderer = _

  setSlots()
  CookingEquipment.addCooking(itemInfo, this)

  def canCook(stack: ItemStack): Boolean = cookableFoood.keySet.find(_.isItemEqual(stack)).map(_ => true).getOrElse(false)
  protected def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase): Unit
  //def drawGuiTexture(container: GuiCampfireCook)
  def getBaseCookTime(): Int = cookTime
  def getCookedFood(stack: ItemStack): ItemStack = cookableFoood.keySet.find(_.isItemEqual(stack)).map(cookableFoood.get(_)).getOrElse(null)
  def renderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    if (renderer == null) {
      renderer = new ItemRenderer(Minecraft.getMinecraft)
    }
    if (foodIndex < maxFood && 
      (!(stack.getItem == Objs.parts) || (stack.getItemDamage != PartInfo.ASH))) {
      doRenderFood(foodIndex, stack, entity)
    }
  }
  def renderModel()
  def setSlots()
}

object CookingEquipment {
  var grillFood: HashMap[ItemStack, ItemStack] = new HashMap[ItemStack, ItemStack]()
  var spitFood: HashMap[ItemStack, ItemStack] = new HashMap[ItemStack, ItemStack]()
  var panFood: HashMap[ItemStack, ItemStack] = new HashMap[ItemStack, ItemStack]()
  var equipment: HashMap[ItemStack, CookingEquipment] = new HashMap[ItemStack, CookingEquipment]()
  var equipmentRecipes: HashMap[ArrayList[ItemStack], CookingEquipment] = new HashMap[ArrayList[ItemStack], CookingEquipment]()
  
  def addCooking(item: ItemStack, cookEquipment: CookingEquipment) = equipment.put(item, cookEquipment)
  def addEquipmentRecipe(equipment: CookingEquipment, items: ItemStack*) {
    val key = new ArrayList[ItemStack]()
    for (item <- items) key.add(item)
    equipmentRecipes.put(key, equipment)
  }
  def addGrillFood(stack: ItemStack, item: ItemStack) = grillFood.put(stack, item)
  def addPanFood(stack: ItemStack, item: ItemStack) = panFood.put(stack, item)
  def addSpitFood(stack: ItemStack, item: ItemStack) = spitFood.put(stack, item)
  def getCooking(item: ItemStack): CookingEquipment = equipment.keySet.find(_.isItemEqual(item)).map(equipment.get(_)).getOrElse(null)
  def getCookingForRecipe(items: ArrayList[ItemStack]): CookingEquipment = {
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
      if(!flag)i += 1
    }
    println(i)
    if(i<3&&flag)equipmentRecipes.values().toArray()(i).asInstanceOf[CookingEquipment] else null
  }
}

class Grill(item: ItemStack) extends CookingEquipment(600, CookingEquipment.grillFood, 4, item) {
  var pilar: AbstractBox = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F,  0.0F)
  var line: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var line2: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 1, 1, 60, 0.015625F, 0.0F, 0.0F, 0.0F)
  var sLine: AbstractBox = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 29, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var sLine2: AbstractBox = new AbstractBox(64, 64, false, 0, 0, 0, 0, 0, 1, 1, 29, 0.015625F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    val item = new ItemStack(stack.getItem, 1, stack.getItemDamage)
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.09F, -0.4375F, 0.01525F)
    foodIndex match {
      case 0 => GL11.glTranslatef(-0.109375F, 0F, 0.046875F)
      case 1 => GL11.glTranslatef(0.109375F, 0F, 0.046875F)
      case 2 => GL11.glTranslatef(-0.109375F, 0F, -0.171875F)
      case 3 => GL11.glTranslatef(0.109375F, 0F, -0.171875F)
    }
    GL11.glScalef(0.15F, 0.25F, 0.15F)
    GL11.glRotatef(-90, 1, 0, 0)
    GL11.glRotatef(41, 0, -1, 0)
    GL11.glRotatef(-155, 1, 0, 1)
    renderer.renderItem(entity, item, 0)
    GL11.glPopMatrix()
  }
//  override def drawGuiTexture(container: GuiCampfireCook) {
//    container.drawTexturedModalRect(container.getGuiLeft + 67, container.getGuiTop + 7, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 89, container.getGuiTop + 7, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 67, container.getGuiTop + 27, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 89, container.getGuiTop + 27, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 47, container.getGuiTop + 39, 176, 115, 80, 63)
//  }
  override def renderModel() {
    GL11.glPushMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0234375F, -0.5F, -0.4375F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.0234375F, -0.5F, 0.40625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.4375F, -0.0078125F)
    line.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.015625F, -0.4375F, -0.46875F)
    line2.render(Tessellator.instance)
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
    for (i <- 0 until 15) {
      GL11.glPushMatrix()
      GL11.glTranslatef(-0.2343675F, -0.4453125F, -0.2265625F + (i * 0.03125F))
      sLine.render(Tessellator.instance)
      GL11.glPopMatrix()
    }
    for (i <- 0 until 15) {
      GL11.glPushMatrix()
      GL11.glTranslatef(-0.2343675F + (i * 0.03125F), -0.4453125F, -0.2265625F)
      sLine2.render(Tessellator.instance)
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

class Pan(item: ItemStack) extends CookingEquipment(1000, CookingEquipment.panFood, 8, item) {
  var pilar: AbstractBox = new AbstractBox(64, 32, false, 0, 2, 0, 0, 0, 1, 28, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
  var line: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
  var cable: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 1, 45, 1, 0.0078625F, 0.0F, 0.0F, 0.0F)
  var pan: AbstractBox = new AbstractBox(32, 16, false, 2, 1, 0, 0, 0, 5, 3, 5, 0.0625F, 0.0F, 0.0F, 0.0F)
  var panCover: AbstractBox = new AbstractBox(64, 32, false, 4, 2, 0, 0, 0, 3, 1, 3, 0.0625F, 0.0F, 0.0F, 0.0F)
  var panHandle: AbstractBox = new AbstractBox(64, 32, false, 4, 13, 0, 0, 0, 1, 1, 1, 0.03125F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {}
//  override def drawGuiTexture(container: GuiCampfireCook) {
//    container.drawTexturedModalRect(container.getGuiLeft + 24, container.getGuiTop + 77, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 131, container.getGuiTop + 77, 7, 105, 18, 18)
//    container.drawTexturedModalRect(container.getGuiLeft + 32, container.getGuiTop + 54, 7, 105, 1818)
//    container.drawTexturedModalRect(container.getGuiLeft + 123, container.getGuiTop + 54, 7, 105, 1818)
//    container.drawTexturedModalRect(container.getGuiLeft + 40, container.getGuiTop + 30, 7, 105, 1818)
//    container.drawTexturedModalRect(container.getGuiLeft + 113, container.getGuiTop + 30, 7, 105, 1818)
//    container.drawTexturedModalRect(container.getGuiLeft + 65, container.getGuiTop + 21, 7, 105, 1818)
//    container.drawTexturedModalRect(container.getGuiLeft + 90, container.getGuiTop + 21, 7, 105, 1818)
//  }
  override def renderModel() {
    GL11.glPushMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_PAN))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.875F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.875F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.859375F, -0.0078125F)
    line.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.15625F, -0.53125F, -0.15625F)
    pan.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.09375F, -0.5625F, -0.09375F)
    panCover.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.015625F, -0.578125F, -0.015625F)
    panHandle.render(Tessellator.instance)
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.00393125F, -0.8515125F, -0.00393125F)
    cable.render(Tessellator.instance)
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
class Spit(item: ItemStack) extends CookingEquipment(300, CookingEquipment.spitFood, 2, item) {
  var pilar: AbstractBox = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
  var line: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)

  override def doRenderFood(foodIndex: Int, stack: ItemStack, entity: EntityLivingBase) {
    val item = new ItemStack(stack.getItem, 1, stack.getItemDamage)
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.09F, -0.425F, 0.01525F)
    foodIndex match {
      case 0 => GL11.glTranslatef(-0.125F, 0F, 0F)
      case 1 => GL11.glTranslatef(0.125F, 0F, 0)
    }
    GL11.glScalef(0.15F, 0.15F, 0.25F)
    GL11.glRotatef(41, 0, -1, 0)
    GL11.glRotatef(-150, 1, 0, 1)
    renderer.renderItem(entity, item, 0)
    GL11.glPopMatrix()
  }
//  override def drawGuiTexture(container: GuiCampfireCook) {
//    container.drawTexturedModalRect(container.getGuiLeft + 48, container.getGuiTop + 26, 176, 44, 80, 68)
//  }
  override def renderModel() {
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
    pilar.render(Tessellator.instance)
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glTranslatef(-0.46875F, -0.484375F, -0.0078125F)
    line.render(Tessellator.instance)
    GL11.glPopMatrix()
  }
  override def setSlots() {
    slots(0)(0) = 66
    slots(1)(0) = 27
    slots(0)(1) = 94
    slots(1)(1) = 27
  }
}