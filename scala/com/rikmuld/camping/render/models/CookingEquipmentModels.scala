package com.rikmuld.camping.render.models

import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.corerm.client.ModularModal

object CookingEquipmentModels {
  final val SPIT =
    new ModularModal()

  final val GRILL =
    new ModularModal()

  final val PAN =
    new ModularModal()

  GRILL.setTextureSize(512, 128)
  GRILL.setScale(0.0078125f)

  GRILL.addBox("pillar1", 0, 8, -56, -64, -2, 4, 64, 4)
  GRILL.addBox("pillar2", 0, 8, 52, -64, -2, 4, 64, 4)
  GRILL.addBox("pillar3", 0, 8, -2, -64, -56, 4, 64, 4)
  GRILL.addBox("pillar4", 0, 8, -2, -64, 52, 4, 64, 4)

  GRILL.addBox("line1", 0, 0, -60, -56, -1, 120, 2, 2)
  GRILL.addBox("line2", 0, 0, -1, -56, -60, 2, 2, 120)

  GRILL.setTextureSize(256, 256)

  for(i <- 0 until 15) {
    GRILL.addBox(s"sLine1_$i", 0, 8, -30, -57, -30 + i * 4, 58, 2, 2)
    GRILL.addBox(s"sLine2_$i", 0, 8, -30 + i * 4, -57, -30, 2, 2, 58)
  }

  SPIT.setTextureSize(512, 128)
  SPIT.setScale(0.0078125f)
  SPIT.bindTexture(CookingEquipment.TEXTURE_SPIT)

  SPIT.addBox("pillar1", 0, 8, -56, -64, -2, 4, 64, 4)
  SPIT.addBox("pillar2", 0, 8, 52, -64, -2, 4, 64, 4)

  SPIT.addBox("line", 0, 0, -60, -62, -1, 120, 2, 2)

  PAN.setTextureSize(256, 128)
  PAN.setScale(0.0078125f)

  PAN.addBox("pillar1", 0, 8, -56, -112, -2, 4, 112, 4)
  PAN.addBox("pillar2", 0, 8, 52, -112, -2, 4, 112, 4)

  PAN.addBox("line", 0, 0, -60, -110, -1, 120, 2, 2)
  PAN.addBox("cable", 0, 0, 0, -108, 0, 1, 45, 1) // 0 is -0.5

  PAN.setTextureSize(256, 128)

  PAN.addBox("panBody", 16, 8, -20, -68, -20, 40, 24, 40)
  PAN.addBox("panCover", 16, 8, -12, -72, -12, 24, 8, 24)
  PAN.addBox("panHandle", 16, 52, -2, -74, -2, 4, 4, 4)
}