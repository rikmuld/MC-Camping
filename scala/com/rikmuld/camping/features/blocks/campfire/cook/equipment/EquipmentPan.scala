package com.rikmuld.camping.features.blocks.campfire.cook.equipment

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Definitions.Kit

class EquipmentPan extends CookingEquipment(Kit.PAN,
  config.cookTimePan, 8, ModelCookingEquipment.PAN) {

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