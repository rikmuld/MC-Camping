package com.rikmuld.camping.features.blocks.lantern

import com.rikmuld.camping.Definitions.Lantern
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.items.ItemBlockRM
import net.minecraft.block.Block
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemLantern(modId: String, definition: ObjDefinition, block: Block)
  extends ItemBlockRM(modId, definition, block) {

    override def addInformation(stack: ItemStack,
                                player: World,
                                list: java.util.List[String],
                                tooltip: ITooltipFlag): Unit =
      if (stack.hasTagCompound)
        list.add("Burning time left: " +
          stack.getTagCompound.getInteger("time") + " seconds")
      else if(stack.getItemDamage == Lantern.ON)
        list.add("Burning time left: 750 seconds")
  }