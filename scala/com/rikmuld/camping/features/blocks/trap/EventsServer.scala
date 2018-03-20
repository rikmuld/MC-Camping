package com.rikmuld.camping.features.blocks.trap

import net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

@Mod.EventBusSubscriber
object EventsServer {

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    val player = event.player

    if (event.phase.equals(Phase.START)) {
      val trapTime = player.getEntityData.getInteger("isInTrap")

      if (trapTime > 0) {
        player.getEntityData.setInteger("isInTrap", trapTime - 1)
      } else if (trapTime == 0) {
        player.getEntityData.setInteger("isInTrap", -1)

        val speed = player.getEntityAttribute(MOVEMENT_SPEED)

        Option(speed.getModifier(TileEntityTrap.UUIDSpeedTrap)).foreach(modifier =>
          speed.removeModifier(modifier)
        )
      }
    }
  }
}