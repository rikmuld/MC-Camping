package com.rikmuld.camping.render.models

import com.rikmuld.corerm.client.ModularModal

//TODO remove and add closed to json
object TrapModel {
  final val MODEL_TRAP_CLOSED = new ModularModal()
  final val MODEL_TRAP_OPEN = new ModularModal()

  MODEL_TRAP_CLOSED.setTextureSize(32, 16)
  MODEL_TRAP_CLOSED.addBox("sideHigh1", 0, 0, 7, 1, 1, -3.5F, 20F, 1.5F, 0, 0, 0)
  MODEL_TRAP_CLOSED.addBox("sideHigh2", 0, 0, 7, 1, 1, -3.5F, 20F, -2.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("side1", 0, 0, 1, 1, 3, 3.5F, 23F, -1.5F, 0, 0, 0)
  MODEL_TRAP_CLOSED.addBox("side2", 0, 0, 1, 1, 3, -4.5F, 23F, -1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("sideUp1", 0, 0, 1, 4, 1, 3.5F, 20F, -2.5F, 0, 0, 0)
  MODEL_TRAP_CLOSED.addBox("sideUp2", 0, 0, 1, 4, 1, -4.5F, 20F, -2.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("sideUp3", 0, 0, 1, 4, 1, -4.5F, 20F, 1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("sideUp4", 0, 0, 1, 4, 1, 3.5F, 20F, 1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("middlePlate", 0, 0, 3, 0, 3, -1.5F, 23.5F, -1.5F, 0, 0, 0)
  MODEL_TRAP_CLOSED.addBox("middleConnector", 0, 0, 7, 0, 1, -3.5F, 23.5F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth1", 0, 0, 1, 0, 2, 3F, 20.5F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth2", 0, 0, 1, 0, 2, 2F, 20.5F, -1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth3", 0, 0, 1, 0, 2, 0F, 20.5F, -1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth4", 0, 0, 1, 0, 2, -4F, 20.5F, -1.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth5", 0, 0, 1, 0, 2, -3F, 20.5F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth6", 0, 0, 1, 0, 2, -1F, 20.5F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth7", 0, 0, 1, 0, 2, 1F, 20.5F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_CLOSED.addBox("tooth8", 0, 0, 1, 0, 2, -2F, 20.5F, -1.5F, 0F, 0F, 0F)

  MODEL_TRAP_OPEN.setTextureSize(32, 16)
  MODEL_TRAP_OPEN.addBox("side1", 7, 0, 1, 1, 9, 3.5F, 23F, -4.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("side2", 0, 4, 7, 1, 1, -3.5F, 23F, 3.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("side3", 7, 0, 1, 1, 9, -4.5F, 23F, -4.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("side4", 0, 4, 7, 1, 1, -3.5F, 23F, -4.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("middlePlate", -1, 1, 3, 0, 3, -1.5F, 23.5F, -1.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("middleConnector", 1, 0, 7, 0, 1, -3.5F, 23.6F, -0.5F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth1", 0, 0, 1, 2, 0, 0F, 21F, -4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth2", 0, 0, 1, 2, 0, -2F, 21F, -4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth3", 0, 0, 1, 2, 0, -4F, 21F, -4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth4", 0, 0, 1, 2, 0, 2F, 21F, -4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth5", 0, 0, 1, 2, 0, 3F, 21F, 4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth6", 0, 0, 1, 2, 0, 1F, 21F, 4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth7", 0, 0, 1, 2, 0, -1F, 21F, 4F, 0F, 0F, 0F)
  MODEL_TRAP_OPEN.addBox("tooth8", 0, 0, 1, 2, 0, -3F, 21F, 4F, 0F, 0F, 0F)
}