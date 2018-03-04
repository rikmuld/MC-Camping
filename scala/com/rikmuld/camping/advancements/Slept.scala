package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Library.AdvancementInfo._
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{JsonUtils, ResourceLocation}

object Slept {
  class Trigger extends TriggerSimple.Trigger[BlockPos, Instance] {
    protected val id: ResourceLocation =
      SLEPT

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance =
      new Instance(JsonUtils.getString(json, "block"))
  }

  protected class Instance(block: String) extends TriggerSimple.Instance[BlockPos](SLEPT) {
    def test(player: EntityPlayerMP, sleptIn: BlockPos): Boolean =
      block == player.world.getBlockState(sleptIn).getBlock.getRegistryName.getResourcePath
  }
}