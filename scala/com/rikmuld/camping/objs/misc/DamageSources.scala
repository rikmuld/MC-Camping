package com.rikmuld.camping.objs.misc

import java.util.Random

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.DamageSource
import net.minecraft.util.text.{ITextComponent, TextComponentString}

class DamageSourceBleeding(name: String) extends DamageSource(name) {
  override def getDeathMessage(entity: EntityLivingBase): ITextComponent = {
    val random = new Random()
    val num = random.nextInt(5)
    if (entity.isInstanceOf[EntityPlayer]) {
      if (num == 0) new TextComponentString(entity.asInstanceOf[EntityPlayer].getName + " bled away")
      else if (num == 1) new TextComponentString(entity.asInstanceOf[EntityPlayer].getName + " has run out of blood")
      else if (num == 2) new TextComponentString(entity.asInstanceOf[EntityPlayer].getName + " bled out")
      else if (num == 3) new TextComponentString(entity.asInstanceOf[EntityPlayer].getName + " bled to death")
      else if (num == 4) new TextComponentString(entity.asInstanceOf[EntityPlayer].getName + " fizzled")
      else null
    } else null
  }
}