package com.rikmuld.camping.misc

import net.minecraft.potion.Potion
import net.minecraft.entity.EntityLivingBase
import net.minecraft.client.Minecraft
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.util.ResourceLocation
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.core.Objs

object Potions {
  def getNextFreePotionID(): Int = (0 until Potion.potionTypes.length).find(Potion.potionTypes(_) == null).getOrElse(-1)
}

class PotionBleeding(name: String) extends Potion(Potions.getNextFreePotionID(), false, 9643043) {
  setPotionName(name)
  setIconIndex(0, 0)

  @SideOnly(Side.CLIENT)
  override def hasStatusIcon(): Boolean = {
    Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.SPRITE_POTION))
    true
  }
  override def isReady(par1: Int, par2: Int): Boolean = {
    val k = 60 >> par2
    if (k > 0) (par1 % k) == 0 else true
  }
  override def performEffect(entity: EntityLivingBase, amplifier: Int) = entity.attackEntityFrom(Objs.bleedingSource, (entity.getMaxHealth() / 20) * (amplifier + 1));
}