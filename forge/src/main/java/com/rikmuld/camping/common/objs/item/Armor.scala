package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.ObjInfo
import net.minecraft.item.ItemStack
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.TextInfo

class ArmorFur(infoClass: Class[_], armorType: Int) extends ItemArmorMain(infoClass, Objs.fur, armorType) {
  override def getArmorTexture(stack: ItemStack, entity: Entity, slot: Int, layer: String): String = if (this == Objs.furLeg) TextureInfo.ARMOR_FUR_LEG else TextureInfo.ARMOR_FUR_MAIN
}