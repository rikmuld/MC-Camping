package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Lib.AdvancementInfo._
import com.rikmuld.corerm.advancements.{AdvancementTrigger, TriggerInstance}
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

object TentChanged {
  class Trigger extends AdvancementTrigger[Seq[ItemStack], Instance] {
    protected val id: ResourceLocation =
      TENT_CHANGED

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance =
      new Instance(ItemPredicate.deserializeArray(json.get("items")))
  }

  protected class Instance(items: Seq[ItemPredicate]) extends TriggerInstance[Seq[ItemStack]](TENT_CHANGED) {
    def test(player: EntityPlayerMP, currentItems: Seq[ItemStack]): Boolean =
      items.forall(testItem => currentItems.exists(item => testItem.test(item)))
  }
}