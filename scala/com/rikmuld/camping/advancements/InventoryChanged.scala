package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Lib.AdvancementInfo._
import com.rikmuld.camping.inventory.camping.InventoryCamping
import com.rikmuld.corerm.advancements.{AdvancementTrigger, TriggerInstance}
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation

object InventoryChanged {
  class Trigger extends AdvancementTrigger[InventoryCamping, Instance] {
    protected val id: ResourceLocation =
      INVENTORY_CHANGED

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance = {
      val items = Option(json.get("items")) map ItemPredicate.deserializeArray map(_.toSeq)
      val isFull = Option(json.get("full")).map(_.getAsBoolean)
      val isEmpty = Option(json.get("empty")).map(_.getAsBoolean)

      new Instance(items, isFull, isEmpty)
    }
  }

  protected class Instance(items: Option[Seq[ItemPredicate]],
                           full: Option[Boolean],
                           empty: Option[Boolean]) extends TriggerInstance[InventoryCamping](INVENTORY_CHANGED) {

    def test(player: EntityPlayerMP, inventory: InventoryCamping): Boolean =
      items.fold(true)(_.forall(testItem => inventory.getInventory.exists(item => testItem.test(item)))) &&
        full.fold(true)(_ == inventory.isFull) &&
        empty.fold(true)(_ == inventory.isEmpty)
  }
}