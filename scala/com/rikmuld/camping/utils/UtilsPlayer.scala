package com.rikmuld.camping.utils

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.SleepResult
import net.minecraft.init.Biomes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

import scala.collection.JavaConversions._

object UtilsPlayer {
  def trySleep(isOccupied: => Boolean, setOccupied: (Boolean) => Unit)(world: World, pos: BlockPos, player: EntityPlayer): Boolean = {
    if (!world.provider.canRespawnHere || world.getBiomeForCoordsBody(pos) == Biomes.HELL) {
      world.destroyBlock(pos, false)
      world.newExplosion(null, pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5, 5, true, true)

      return true
    }

    if (isOccupied && getPlayerInBed(world, pos).isDefined) {
      player.sendMessage(new TextComponentTranslation("tile.bed.occupied", new Object))

      return true
    }

    var occupied: Boolean =
      false

    player.trySleep(pos) match {
      case SleepResult.OK =>
        occupied = true
      case SleepResult.NOT_POSSIBLE_NOW =>
        player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
      case SleepResult.NOT_SAFE =>
        player.sendMessage(new TextComponentTranslation("tile.bed.notSafe", new Object))
      case SleepResult.TOO_FAR_AWAY =>
        player.sendMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object))
      case _ =>
    }

    setOccupied(occupied)

    true
  }

  def getPlayerInBed(world: World, pos: BlockPos): Option[EntityPlayer] =
    world.playerEntities.find(player =>
      player.isPlayerSleeping && player.getPosition.equals(pos)
    )
}
