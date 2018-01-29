package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Lib.AdvancementInfo._
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

object FoodRoasted {
  class Trigger extends TriggerSimple.Trigger[(ItemStack, ItemStack), Instance] {
    protected val id: ResourceLocation =
      FOOD_ROASTED

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance = {
      val in = Option(json.get("roasting_item")).map(data => ItemPredicate.deserialize(data))
      val out = Option(json.get("result")).map(data => ItemPredicate.deserialize(data))

      new Instance(in, out)
    }
  }

  protected class Instance(in: Option[ItemPredicate],
                           out: Option[ItemPredicate]) extends TriggerSimple.Instance[(ItemStack, ItemStack)](FOOD_ROASTED) {

    def test(player: EntityPlayerMP, inAndOut: (ItemStack, ItemStack)): Boolean =
      in.fold(true)(_.test(inAndOut._1)) && out.fold(true)(_.test(inAndOut._2))
  }
}