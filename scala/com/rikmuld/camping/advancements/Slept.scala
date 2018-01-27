package com.rikmuld.camping.advancements

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.rikmuld.camping.Lib.AdvancementInfo._
import com.rikmuld.corerm.advancements.{AdvancementTrigger, TriggerInstance}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{JsonUtils, ResourceLocation}

object Slept {
  class Trigger extends AdvancementTrigger[BlockPos, Instance] {
    protected val id: ResourceLocation =
      SLEPT

    override def deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance =
      new Instance(JsonUtils.getString(json, "block"))
  }

  protected class Instance(block: String) extends TriggerInstance[BlockPos](SLEPT) {
    def test(player: EntityPlayerMP, sleptIn: BlockPos): Boolean =
      block == player.world.getBlockState(sleptIn).getBlock.getRegistryName.getResourcePath
  }
}