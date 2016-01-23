package com.rikmuld.camping.render.models

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import com.rikmuld.corerm.client.RMModel

class ModelTrapClosed extends RMModel(32, 16) {    
  addBox("sideHigh1", 0, 0, 7, 1, 1, -3.5F, 20F, 1.5F, 0, 0, 0)
  addBox("sideHigh2", 0, 0, 7, 1, 1, -3.5F, 20F, -2.5F, 0F, 0F, 0F)
  addBox("side1", 0, 0, 1, 1, 3, 3.5F, 23F, -1.5F, 0, 0, 0)
  addBox("side2", 0, 0, 1, 1, 3, -4.5F, 23F, -1.5F, 0F, 0F, 0F)
  addBox("sideUp1", 0, 0, 1, 4, 1, 3.5F, 20F, -2.5F, 0, 0, 0)
  addBox("sideUp2", 0, 0, 1, 4, 1, -4.5F, 20F, -2.5F, 0F, 0F, 0F)
  addBox("sideUp3", 0, 0, 1, 4, 1, -4.5F, 20F, 1.5F, 0F, 0F, 0F)
  addBox("sideUp4", 0, 0, 1, 4, 1, 3.5F, 20F, 1.5F, 0F, 0F, 0F)
  addBox("middlePlate", 0, 0, 3, 0, 3, -1.5F, 23.5F, -1.5F, 0, 0, 0)
  addBox("middleConnector", 0, 0, 7, 0, 1, -3.5F, 23.5F, -0.5F, 0F, 0F, 0F)
  addBox("tooth1", 0, 0, 1, 0, 2, 3F, 20.5F, -0.5F, 0F, 0F, 0F)
  addBox("tooth2", 0, 0, 1, 0, 2, 2F, 20.5F, -1.5F, 0F, 0F, 0F)
  addBox("tooth3", 0, 0, 1, 0, 2, 0F, 20.5F, -1.5F, 0F, 0F, 0F)
  addBox("tooth4", 0, 0, 1, 0, 2, -4F, 20.5F, -1.5F, 0F, 0F, 0F)
  addBox("tooth5", 0, 0, 1, 0, 2, -3F, 20.5F, -0.5F, 0F, 0F, 0F)
  addBox("tooth6", 0, 0, 1, 0, 2, -1F, 20.5F, -0.5F, 0F, 0F, 0F)
  addBox("tooth7", 0, 0, 1, 0, 2, 1F, 20.5F, -0.5F, 0F, 0F, 0F)
  addBox("tooth8", 0, 0, 1, 0, 2, -2F, 20.5F, -1.5F, 0F, 0F, 0F)
}

class ModelTrapOpen extends RMModel(32, 16) {
  addBox("side1", 7, 0, 1, 1, 9, 3.5F, 23F, -4.5F, 0F, 0F, 0F)
  addBox("side2", 0, 4, 7, 1, 1, -3.5F, 23F, 3.5F, 0F, 0F, 0F)
  addBox("side3", 7, 0, 1, 1, 9, -4.5F, 23F, -4.5F, 0F, 0F, 0F)
  addBox("side4", 0, 4, 7, 1, 1, -3.5F, 23F, -4.5F, 0F, 0F, 0F)
  addBox("middlePlate", -1, 1, 3, 0, 3, -1.5F, 23.5F, -1.5F, 0F, 0F, 0F)
  addBox("middleConnector", 1, 0, 7, 0, 1, -3.5F, 23.6F, -0.5F, 0F, 0F, 0F)
  addBox("tooth1", 0, 0, 1, 2, 0, 0F, 21F, -4F, 0F, 0F, 0F)
  addBox("tooth2", 0, 0, 1, 2, 0, -2F, 21F, -4F, 0F, 0F, 0F)
  addBox("tooth3", 0, 0, 1, 2, 0, -4F, 21F, -4F, 0F, 0F, 0F)
  addBox("tooth4", 0, 0, 1, 2, 0, 2F, 21F, -4F, 0F, 0F, 0F)
  addBox("tooth5", 0, 0, 1, 2, 0, 3F, 21F, 4F, 0F, 0F, 0F)
  addBox("tooth6", 0, 0, 1, 2, 0, 1F, 21F, 4F, 0F, 0F, 0F)
  addBox("tooth7", 0, 0, 1, 2, 0, -1F, 21F, 4F, 0F, 0F, 0F)
  addBox("tooth8", 0, 0, 1, 2, 0, -3F, 21F, 4F, 0F, 0F, 0F)
}