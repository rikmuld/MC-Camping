package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.Objs
import net.minecraft.item.ItemStack
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.common.objs.item.ItemArmorMain
import com.rikmuld.camping.core.ModInfo

class ArmorFur(infoClass: Class[_], armorType: Int) extends ItemArmorMain(infoClass, Objs.tab, ModInfo.MOD_ID, Objs.fur, armorType) {
  override def getArmorTexture(stack: ItemStack, entity: Entity, slot: Int, layer: String): String = if (this == Objs.furLeg) TextureInfo.ARMOR_FUR_LEG else TextureInfo.ARMOR_FUR_MAIN
}