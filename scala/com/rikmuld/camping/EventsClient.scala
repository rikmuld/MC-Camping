package com.rikmuld.camping

import com.rikmuld.camping.CampingMod._
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
object EventsClient {

  @SubscribeEvent
  def onPlayerJoinWorld(event: PlayerLoggedInEvent){
    if(config.welcomeMess){
      event.player.sendMessage(new TextComponentString("The Camping Iventory overides the normal inventory. To change this, " +
                                                                 "just press the configuration tab in the Camping Invenory; search for: " +
                                                                 "'primary inventory option'."))

      config.disableMess()
    }
  }
}