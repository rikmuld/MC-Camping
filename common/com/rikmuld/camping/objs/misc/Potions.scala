package com.rikmuld.camping.objs.misc

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object Potions {
  final val TEXTURE = new ResourceLocation(TextureInfo.SPRITE_POTION)
}

class PotionBleeding(name: String) extends Potion(false, 9643043) {
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