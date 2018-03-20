package com.rikmuld.camping.features.blocks.trap

import com.rikmuld.camping.Library.{DamageInfo, TextureInfo}
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object PotionBleeding {
  final val TEXTURE =
    new ResourceLocation(TextureInfo.SPRITE_POTION)

  val bleedingSource =
    new DamageSourceBleeding(DamageInfo.BLEEDING)
}

class PotionBleeding(name: String) extends Potion(false, 9643043) {
  setPotionName(name)
  setRegistryName(name)
  setIconIndex(0, 0)

  @SideOnly(Side.CLIENT)
  override def hasStatusIcon: Boolean = {
    Minecraft.getMinecraft.renderEngine.bindTexture(PotionBleeding.TEXTURE)
    true
  }

  override def isReady(par1: Int, par2: Int): Boolean = {
    val k = 60 >> par2
    if (k > 0) (par1 % k) == 0 else true
  }

  override def performEffect(entity: EntityLivingBase, amplifier: Int): Unit =
    entity.attackEntityFrom(PotionBleeding.bleedingSource, (entity.getMaxHealth / 20) * (amplifier + 1))
}