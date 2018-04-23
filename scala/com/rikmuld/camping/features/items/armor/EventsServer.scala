package com.rikmuld.camping.features.items.armor

import java.util.{Random, UUID}

import com.rikmuld.camping.CampingMod
import net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.item.ItemArmor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent

import scala.collection.JavaConversions._

@Mod.EventBusSubscriber
object EventsServer {

  val UUIDSpeedCamping =
    new UUID(new Random(83746763).nextLong, new Random(28647556).nextLong)

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    val increase = event.player.getArmorInventoryList.count(_.getItem match {
      case item: ItemArmor =>
        item.getArmorMaterial == CampingMod.OBJ.fur
      case _ =>
        false
    })

    val speed = event.player.getEntityAttribute(MOVEMENT_SPEED)
    val modifier = Option(speed.getModifier(UUIDSpeedCamping))

    if(!modifier.exists(_.getAmount == 0.01 * increase)){
      modifier.foreach(mod => speed.removeModifier(mod))
      speed.applyModifier(new AttributeModifier(UUIDSpeedCamping, "camping.speedBoost", 0.01 * increase, 0))
    }
  }
}