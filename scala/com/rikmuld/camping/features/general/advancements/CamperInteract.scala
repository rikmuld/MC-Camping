package com.rikmuld.camping.features.general.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Library.AdvancementInfo._
import com.rikmuld.camping.features.entities.camper.EntityCamper
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation

object CamperInteract {
  class Trigger extends TriggerSimple.Trigger[EntityCamper, Instance] {
    protected val id: ResourceLocation =
      CAMPER_INTERACT

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance =
      new Instance(Option(json.get("camper")) map EntityPredicate.deserialize)
  }

  protected class Instance(item: Option[EntityPredicate]) extends TriggerSimple.Instance[EntityCamper](CAMPER_INTERACT) {
    def test(player: EntityPlayerMP, camper: EntityCamper): Boolean =
      item.fold(true)(_.test(player, camper))
  }
}