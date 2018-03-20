package com.rikmuld.camping.features.blocks.campfire

import com.rikmuld.corerm.utils.PlayerUtils
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

@Mod.EventBusSubscriber
object EventsServer {

  var roasterTick: Float =
    0

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    val player = event.player
    val world = player.world
    val item = player.inventory.getCurrentItem

    if (!world.isRemote && event.phase.equals(Phase.END)) {
      val roasterTickOld = roasterTick

      if(!item.isEmpty){
        Option(PlayerUtils.getMOP(player)).foreach(mop =>
          world.getTileEntity(mop.getBlockPos) match {
            case roaster: Roaster if mop.getBlockPos.distanceSq(player.posX, player.posY, player.posZ) <= 7 =>

              if(roaster.canRoast(item)){
                if(roasterTick > roaster.roastTime(item)){
                  val cooked = roaster.roast(player, item).get

                  player.inventory.getCurrentItem.setCount(player.inventory.getCurrentItem.getCount - 1)

                  if (player.inventory.getCurrentItem.getCount <= 0) player.setHeldItem(player.getActiveHand, ItemStack.EMPTY)
                  if (!player.inventory.addItemStackToInventory(cooked)) player.dropItem(cooked, false)
                  roasterTick = 0
                } else roasterTick += roaster.roastSpeed(item)
              }

            case _ =>
          }
        )
      }

      if(roasterTick == roasterTickOld)
        roasterTick = 0
    }
  }
}