package com.rikmuld.camping.misc

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Library.TextureInfo
import com.rikmuld.camping.objs.Definitions.{Kit, Parts}
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.render.models.CookingEquipmentModels
import com.rikmuld.corerm.client.ModularModal
import com.rikmuld.corerm.utils.StackUtils
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

import scala.collection.mutable

abstract class CookingEquipment(kit: Int, cookTime: Int, maxSlots: Int, model: ModularModal) {
  CookingEquipment.registerEquipment(kit, this)

  private val recipes: mutable.Map[(Item, Int), ItemStack] =
    mutable.Map()

  def getKitDamage: Int =
    kit

  def getCookTime: Int =
    cookTime

  def getMaxCookingSlot: Int =
    maxSlots

  def getModel: ModularModal =
    model

  def renderInWorld(): Unit =
    model.renderAll()

  def registerRecipe(item: Item, result: ItemStack): Unit =
    registerRecipe(item, 0, result)

  def registerRecipe(item: Item, damage: Int, result: ItemStack): Unit =
    recipes += (item, damage) -> result

  def canCook(food: ItemStack): Boolean =
    recipes.contains((food.getItem, food.getItemDamage))

  def getResult(food: ItemStack): Option[ItemStack] =
    recipes.get((food.getItem, food.getItemDamage)).map(_.copy())

  def renderFood(render: RenderItem, slot: Int, food: ItemStack): Unit =
    if(slot < maxSlots && !food.isEmpty && !food.isItemEqual(
      new ItemStack(ObjRegistry.parts, 1, Parts.ASH)
    ))
      doRenderFood(render, slot, food)

  def renderGUIBackground(gui: GuiContainer): Unit =
    for(i <- 0 until maxSlots)
      gui.drawTexturedModalRect(
        gui.getGuiLeft + getSlotPosition(i)._1 - 1,
        gui.getGuiTop + getSlotPosition(i)._2 - 1,
        7,
        105,
        18,
        18
      )

  protected def doRenderFood(render: RenderItem, slot: Int, food: ItemStack): Unit =
    Unit

  def getSlotPosition(slot: Int): (Int, Int)
}

object CookingEquipment {
  final val TEXTURE_SPIT =
    new ResourceLocation(TextureInfo.MODEL_SPIT)

  final val TEXTURE_GRILL =
    new ResourceLocation(TextureInfo.MODEL_GRILL)

  final val TEXTURE_PAN =
    new ResourceLocation(TextureInfo.MODEL_PAN)

  val kitRecipes: mutable.Map[Seq[ItemStack], CookingEquipment] =
    mutable.Map()

  val kits: mutable.Map[Int, CookingEquipment] =
    mutable.Map()

  def registerEquipment(kit: Int, equipment: CookingEquipment): Unit =
    kits(kit) = equipment

  def getEquipment(kit: Int): Option[CookingEquipment] =
    kits.get(kit)

  def getEquipment(kit: ItemStack): Option[CookingEquipment] =
    if(kit.isEmpty || kit.getItem != ObjRegistry.kit)
      None
    else
      getEquipment(kit.getItemDamage)

  def registerKitRecipe(cooking: CookingEquipment, contents: ItemStack*): Unit =
    kitRecipes.put(contents, cooking)

  def getFirstKitRecipe(equipment: CookingEquipment): Seq[ItemStack] =
    kitRecipes.find(_._2 == equipment).get._1

  def getEquipment(recipe: Seq[ItemStack]): Option[CookingEquipment] =
    _getEquipment(StackUtils.flatten(recipe))

  private def _getEquipment(items: Seq[ItemStack]): Option[CookingEquipment] = kitRecipes.find {
    case(contents, _) =>
      contents.lengthCompare(items.length) == 0 && items.forall(item =>
        contents.exists(other => other.isItemEqual(item) && other.getCount == item.getCount )
      )
  }.map(_._2)
}

class Spit extends CookingEquipment(Kit.SPIT,
  config.cookTimeSpit, 2, CookingEquipmentModels.SPIT) {

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

class Grill extends CookingEquipment(Kit.GRILL,
  config.cookTimeGrill, 4, CookingEquipmentModels.GRILL) {

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

class Pan extends CookingEquipment(Kit.PAN,
  config.cookTimePan, 8, CookingEquipmentModels.PAN) {

  override def getSlotPosition(slot: Int): (Int, Int) = slot match {
    case 0 => (25, 78)
    case 1 => (132, 78)
    case 2 => (33, 55)
    case 3 => (124, 55)
    case 4 => (41, 31)
    case 5 => (114, 31)
    case 6 => (66, 22)
    case 7 => (91, 22)
  }

  override def renderInWorld(): Unit = {
    getModel.bindTexture(CookingEquipment.TEXTURE_PAN)
    getModel.renderExcept("cable")

    getModel.bindTexture(CookingEquipment.TEXTURE_GRILL)
    getModel.renderOnly("cable")
  }
}