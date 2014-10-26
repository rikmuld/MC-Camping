package com.rikmuld.camping.misc.damagesources

import java.util.Random

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChatComponentText
import net.minecraft.util.DamageSource
import net.minecraft.util.IChatComponent

class DamageSourceBleeding(name: String) extends DamageSource(name) {
  override def func_151519_b(entity: EntityLivingBase): IChatComponent = {
    val random = new Random()
    val num = random.nextInt(5)
    if (entity.isInstanceOf[EntityPlayer]) {
      if (num == 0) new ChatComponentText(entity.asInstanceOf[EntityPlayer].getCommandSenderName + " bled away")
      else if (num == 1) new ChatComponentText(entity.asInstanceOf[EntityPlayer].getCommandSenderName + " has run out of blood")
      else if (num == 2) new ChatComponentText(entity.asInstanceOf[EntityPlayer].getCommandSenderName + " bled out")
      else if (num == 3) new ChatComponentText(entity.asInstanceOf[EntityPlayer].getCommandSenderName + " bled to death")
      else if (num == 4) new ChatComponentText(entity.asInstanceOf[EntityPlayer].getCommandSenderName + " fizzled")
      else null
    } else null
  }
}