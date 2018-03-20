package com.rikmuld.camping.features.blocks.trap

import java.util.Random

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.DamageSource
import net.minecraft.util.text.{ITextComponent, TextComponentString}

object DamageSourceBleeding {
  final val random =
    new Random()
}

class DamageSourceBleeding(name: String) extends DamageSource(name) {
  override def getDeathMessage(entity: EntityLivingBase): ITextComponent =
    entity match {
      case player: EntityPlayer =>
        getMessage(player, DamageSourceBleeding.random.nextInt(5))
      case _ =>
        null
    }

  def getMessage(player: EntityPlayer, i: Int): TextComponentString =
    new TextComponentString(player.getDisplayName + " " + {
      i match {
        case 0 => "bled out"
        case 1 => "bled away"
        case 2 => "has run out of blood"
        case 3 => "bled to death"
        case 4 => "fizzled"
      }
    })
}