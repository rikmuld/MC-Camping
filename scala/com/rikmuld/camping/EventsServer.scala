package com.rikmuld.camping

import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent

@Mod.EventBusSubscriber
object EventsServer {

  private var facing: EnumFacing =
    _

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit =
    facing = event.player.getHorizontalFacing

  //TODO test if this works as intended (before placing we are the in current players tick, so the can place should be in player tick so when this called should always be the facing of the player placing the block)
  def getFacing: EnumFacing =
    facing
}