package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Library.AdvancementInfo.ENTITY_TRAPPED
import com.rikmuld.camping.entity.Bear
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation

object EntityTrapped {
  class Trigger extends TriggerSimple.Trigger[EntityLivingBase, Instance] {
    protected val id: ResourceLocation =
      ENTITY_TRAPPED

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance = {
      val entity = Option(json.get("entity")).map(data => EntityPredicate.deserialize(data))
      val isAnimal = Option(json.get("isAnimal")).map(_.getAsBoolean)

      new Instance(entity, isAnimal)
    }
  }

  protected class Instance(entity: Option[EntityPredicate], isAnimal:Option[Boolean]) extends TriggerSimple.Instance[EntityLivingBase](ENTITY_TRAPPED) {
    def test(player: EntityPlayerMP, entityTrapped: EntityLivingBase): Boolean =
      entity.fold(true)(_.test(player, entityTrapped)) && isAnimal.fold(true)(isAnimal =>
        if(isAnimal) entityTrapped.isInstanceOf[EntityAnimal] && !entityTrapped.isInstanceOf[Bear]
        else entityTrapped.isInstanceOf[EntityMob] || entityTrapped.isInstanceOf[Bear]
      )
  }
}