package com.rikmuld.camping.features.general.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Library.AdvancementInfo.DYE_BURNED
import com.rikmuld.camping.Library.NBTInfo
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation

object DyeBurned {
  class Trigger extends TriggerSimple.Trigger[Int, Instance] {
    protected val id: ResourceLocation =
      DYE_BURNED

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance ={
      val damage = Option(json.get("damage")).map(_.getAsInt)
      val total = Option(json.get("total")).map(_.getAsInt)

      new Instance(damage, total)
    }
  }

  protected class Instance(damage: Option[Int], total: Option[Int]) extends TriggerSimple.Instance[Int](DYE_BURNED) {
    def test(player: EntityPlayerMP, burnedDamage: Int): Boolean = {
      val dataTag = player.getEntityData.getCompoundTag(NBTInfo.ACHIEVEMENTS)
      var burnedArray = dataTag.getIntArray("dye")

      if(!burnedArray.contains(damage))
        burnedArray = burnedArray :+ burnedDamage

      dataTag.setIntArray("dye", burnedArray)
      player.getEntityData.setTag(NBTInfo.ACHIEVEMENTS, dataTag)

      damage.fold(true)(_ == burnedDamage) && total.fold(true)(_ == burnedArray.length)
    }
  }
}
