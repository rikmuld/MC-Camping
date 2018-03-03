package com.rikmuld.camping.render.models

import com.rikmuld.corerm.client.ModularModal

import scala.collection.mutable.ListBuffer

object TentModel {
  final val BED_MIDDLE = "bedMiddle"
  final val BED_LEFT = "bedLeft"
  final val CHEST_RIGHT_1 = "chestRight1"
  final val CHEST_RIGHT_2 = "chestRight2"
  final val CHEST_MIDDLE_BIG = "chestMiddleBig"
  final val CHEST_MIDDLE_SMALL_1 = "chestMiddleSmall1"
  final val CHEST_MIDDLE_SMALL_2 = "chestMiddleSmall2"
  final val CHEST_MIDDLE_SMALL_3 = "chestMiddleSmall3"
  final val CHEST_MIDDLE_SMALL_4 = "chestMiddleSmall4"
  final val CANVAS = Array("canvasRightBottom", "canvasLeftBottom", "canvasRightMiddle", "canvasLeftMiddle", "canvasRightTop", "canvasLeftTop")
  final val PEGS = Array("peg1", "peg2", "peg3", "peg4")
  final val CHESTS_LESS = Array(CHEST_RIGHT_1, CHEST_RIGHT_2)
  final val CHESTS_MORE = Array(CHEST_MIDDLE_BIG, CHEST_MIDDLE_SMALL_1, CHEST_MIDDLE_SMALL_2, CHEST_MIDDLE_SMALL_3, CHEST_MIDDLE_SMALL_4)
  
  def getPartsFor(chests:Int, bed:Boolean):Array[String] = {
    val strings = new ListBuffer[String]
    if(bed) strings.append(if(chests>0) BED_LEFT else BED_MIDDLE)
    for(i <- 0 until chests) strings.append(if(bed) CHESTS_LESS(i) else CHESTS_MORE(i))
    strings.toArray
  }

  final val TENT_MODEL = new ModularModal()

  TENT_MODEL.setTextureSize(128, 64)
  TENT_MODEL.setScale(1)

  TENT_MODEL.addBox(CANVAS(0), 0, 0, 48, 1, 12, -8F, 23.8F, -15.2F, 0.835486F, 0F, 0F)
  TENT_MODEL.addBox(CANVAS(1), 0, 0, 48, 1, 12, -8F, 23.8F, 15.2F, 2.402461F, 0F, 0F)
  TENT_MODEL.addBox(CANVAS(2), 0, 0, 48, 1, 11, -8F, 15.2F, -7.3F, 1.081485F, 0F, 0F)
  TENT_MODEL.addBox(CANVAS(3), 0, 0, 48, 1, 11, -8F, 15.6F, 6.4F, 2.048128F, 0F, 0F)
  TENT_MODEL.addBox(CANVAS(4), 0, 0, 48, 1, 6, -8F, 6F, -2.2F, 1.267377F, 0F, 0F)
  TENT_MODEL.addBox(CANVAS(5), 0, 0, 48, 1, 6, -8F, 6.2F, 1.6F, 1.936593F, 0F, 0F)
  TENT_MODEL.addBox(PEGS(0), 0, 0, 1, 4, 1, -7.8F, 20F, 15F, 0F, 0F, 0F)
  TENT_MODEL.addBox(PEGS(1), 0, 0, 1, 4, 1, 38.8F, 20F, 15F, 0F, 0F, 0F)
  TENT_MODEL.addBox(PEGS(2), 0, 0, 1, 4, 1, 38.8F, 20F, -15.4F, 0F, 0F, 0F)
  TENT_MODEL.addBox(PEGS(3), 0, 0, 1, 4, 1, -7.8F, 20F, -15.4F, 0F, 0F, 0F)
  TENT_MODEL.addBox(BED_LEFT, 0, 13, 32, 1, 16, 0F, 23F, -12F, 0F, 0F, 0F)
  TENT_MODEL.addBox(BED_MIDDLE, 0, 13, 32, 1, 16, 0F, 23F, -8F, 0F, 0F, 0F)
  TENT_MODEL.addBox(CHEST_RIGHT_1, 0, 30, 5, 4, 7, 25F, 20F, 5F, 0F, -1.570796F, 0F)
  TENT_MODEL.addBox(CHEST_RIGHT_2, 0, 30, 5, 4, 7, 14F, 20F, 5F, 0F, -1.570796F, 0F)
  TENT_MODEL.addBox(CHEST_MIDDLE_BIG, 33, 30, 9, 8, 9, 12F, 16F, -4F, 0F, 0F, 0F)
  TENT_MODEL.addBox(CHEST_MIDDLE_SMALL_1, 0, 30, 5, 4, 7, 13F, 20F, 10F, 0F, 1.570796F, 0F)
  TENT_MODEL.addBox(CHEST_MIDDLE_SMALL_2, 0, 30, 5, 4, 7, 21F, 20F, -3F, 0F, 0F, 0F)
  TENT_MODEL.addBox(CHEST_MIDDLE_SMALL_3, 0, 30, 5, 4, 7, 7F, 20F, -3F, 0F, 0F, 0F)
  TENT_MODEL.addBox(CHEST_MIDDLE_SMALL_4, 0, 30, 5, 4, 7, 13F, 20F, -4F, 0F, 1.570796F, 0F)
}