package com.rikmuld.camping.registers

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Library.EntityInfo._
import com.rikmuld.camping.Library.PotionInfo
import com.rikmuld.camping.Library.SoundInfo._
import com.rikmuld.camping.features.blocks.trap.PotionBleeding
import com.rikmuld.camping.features.entities.bear.EntityBear
import com.rikmuld.camping.features.entities.camper.EntityCamper
import com.rikmuld.camping.features.entities.fox.EntityFox
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityList.EntityEggInfo
import net.minecraft.init.Items
import net.minecraft.item.{ItemMonsterPlacer, ItemStack}
import net.minecraft.potion.Potion
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.EntityEntry

@Mod.EventBusSubscriber
object Registry {

  var foxAmb,
      foxDeath,
      bearAmb,
      bearDeath: SoundEvent =
    _

  var bleeding: Potion =
    _


  var bear,
      fox,
      camper: EntityEntry =
    _

  @SubscribeEvent
  def registerSounds(event: RegistryEvent.Register[SoundEvent]): Unit = {
    foxAmb = registerSound(FOX_SAY)
    foxDeath = registerSound(FOX_HURT)
    bearAmb = registerSound(BEAR_SAY)
    bearDeath = registerSound(BEAR_HURT)

    event.getRegistry.registerAll(
      foxAmb,
      foxDeath,
      bearAmb,
      bearDeath
    )
  }

  @SubscribeEvent
  def registerPotions(event: RegistryEvent.Register[Potion]): Unit = {
    bleeding = new PotionBleeding(PotionInfo.BLEEDING)

    event.getRegistry.registerAll(
      bleeding
    )
  }

  @SubscribeEvent
  def register(event: RegistryEvent.Register[EntityEntry]) {
    bear = new EntityEntry(classOf[EntityBear], BEAR).setRegistryName(BEAR)
    fox = new EntityEntry(classOf[EntityFox], FOX).setRegistryName(FOX)
    camper = new EntityEntry(classOf[EntityCamper], CAMPER).setRegistryName(CAMPER)

    bear.setEgg(new EntityEggInfo(bear.getRegistryName, 0x583B2D, 0xE2B572))
    fox.setEgg(new EntityEggInfo(fox.getRegistryName, 0xE0EEEE, 0x362819))
    camper.setEgg(new EntityEggInfo(camper.getRegistryName, 0x747B51, 0x70471B))

    event.getRegistry.registerAll(
      bear,
      fox,
      camper
    )
  }

  def registerSound(location: ResourceLocation):SoundEvent =
    new SoundEvent(location).setRegistryName(location)

  def mkEntityEntry[A <: Entity](entity: Class[A], name: String, color1: Int, color2: Int): EntityEntry = {
    val entry = mkEntityEntry(entity, name)
    val stack = new ItemStack(Items.SPAWN_EGG, 1)

    ItemMonsterPlacer.applyEntityIdToItemStack(stack, entry.getRegistryName)
    CampingMod.OBJ.tab.addToTab(stack)

    entry.setEgg(new EntityEggInfo(entry.getRegistryName, color1, color2))
    entry
  }

  def mkEntityEntry[A <: Entity](entity: Class[A], name: String): EntityEntry =
    new EntityEntry(entity, name).setRegistryName(name)
}