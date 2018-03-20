package com.rikmuld.camping.features.blocks.campfire.cook.equipment

import com.rikmuld.camping.Definitions.Parts
import com.rikmuld.camping.Library.TextureInfo
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.client.ModularModal
import com.rikmuld.corerm.utils.StackUtils
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.RenderItem
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.ResourceLocation

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