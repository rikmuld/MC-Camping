package com.rikmuld.camping.render.models

import com.rikmuld.corerm.client.RMModel

object CampfireModel {
  final val BASE = Array("side1", "side2", "side3", "side4", "corner1", "corner2", "corner3", "corner4")
  final val TOP = Array("top", "balk1", "balk2", "balk3", "balk4")
}

class CampfireModel extends RMModel(64, 32) {
  addBox("side1", 0, 0, 2, 2, 6, 4F, 22F, -3F, 0F, 0F, 0F)
  addBox("side2", 0, 0, 2, 2, 6, -6F, 22F, -3F, 0F, 0F, 0F)
  addBox("side3", 0, 0, 6, 2, 2, -3F, 22F, 4F, 0F, 0F, 0F)
  addBox("side4", 0, 0, 6, 2, 2, -3F, 22F, -6F, 0F, 0F, 0F)
  addBox("corner1", 0, 8, 4, 2, 1, 2F, 22.005F, 5F, 0F, 0.837758F, 0F)
  addBox("corner2", 0, 8, 4, 2, 1, -2F, 22.005F, -5F, 0F, -2.303835F, 0F)
  addBox("corner4", 0, 8, 4, 2, 1, 5F, 22.005F, -2F, 0F, 2.378192F, 0F)
  addBox("corner3", 0, 8, 4, 2, 1, -5F, 22.005F, 2F, 0F, -0.7243116F, 0F)
  addBox("top", 10, 8, 2, 1, 2, -1F, 13.75F, -1F, 0F, 0F, 0F)
  addBox("balk1", 0, 11, 1, 10, 2, -4F, 23.55F, -1F, 0F, 0F, -2.73144F)
  addBox("balk2", 18, 11, 1, 10, 2, 5F, 23.12F, -1F, 0F, 0F, 2.722714F)
  addBox("balk3", 12, 11, 2, 10, 1, -1F, 23.1F, 5F, -2.722714F, 0F, 0F)
  addBox("balk4", 6, 11, 2, 10, 1, -1F, 23.55F, -4F, 2.733186F, 0F, 0F)
}