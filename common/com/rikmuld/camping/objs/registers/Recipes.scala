package com.rikmuld.camping.objs.registers

import com.rikmuld.corerm.objs.PropType
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraft.init.Items
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.Lib._
import net.minecraft.init.Items._
import net.minecraft.init.Blocks._
import scala.collection.JavaConversions._
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.BlockDefinitions
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.init.Items._
import net.minecraft.init.Blocks._
import com.rikmuld.camping.objs.ItemDefinitions

object ModRecipes extends ModRegister {
  override def register {
    val dye = Items.DYE.getMetaCycle(16)
    val prts = parts.getMetaCycle(parts.getItemInfo.getValue(PropType.METADATA).asInstanceOf[Array[_]].length)
    val lanterns = lantern.getMetaCycle(2)

    GameRegistry.addRecipe(nwsk(knife), "010", "010", "010", '0': Character, dye(1), '1': Character, IRON_INGOT)
    GameRegistry.addRecipe(nwsk(campfireCook), " 0 ", "0 0", " 0 ", '0': Character, COBBLESTONE)
    //GameRegistry.addRecipe(nwsk(campfire), " 0 ", "010", "020", '0': Character, stick, '1': Character, flint, '2': Character, campfireCook)
    GameRegistry.addRecipe(nwsk(campfireWood), " 0 ", "0 0", "0 0", '0': Character, STICK)
    GameRegistry.addRecipe(nwsk(kit), "000", "111", "000", '0': Character, IRON_INGOT, '1': Character, dye(11))
    GameRegistry.addRecipe(lanterns(BlockDefinitions.Lantern.ON), "000", "010", "222", '1': Character, GLOWSTONE_DUST, '0': Character, GLASS_PANE, '2': Character, GOLD_INGOT)
    GameRegistry.addRecipe(lanterns(BlockDefinitions.Lantern.OFF), "000", "0 0", "111", '1': Character, GOLD_INGOT, '0': Character, GLASS_PANE)
    GameRegistry.addRecipe(prts(Parts.STICK_IRON), "0", "0", '0': Character, IRON_INGOT)
    GameRegistry.addRecipe(prts(Parts.PAN), " 0 ", "121", " 1 ", '0': Character, dye(1), '1': Character, IRON_INGOT, '2': Character, BOWL)
    GameRegistry.addRecipe(prts(Parts.MARSHMALLOW).toStack(3), "010", "020", "030", '0': Character, SUGAR, '1': Character, POTIONITEM, '2': Character, EGG, '3': Character, BOWL)
    GameRegistry.addShapelessRecipe(prts(Parts.TENT_PEG), prts(Parts.STICK_IRON), nwsk(knife).getWildValue)
    GameRegistry.addShapelessRecipe(nwsk(logseat), nwsk(LOG).getWildValue, nwsk(knife).getWildValue)
    GameRegistry.addShapelessRecipe(nwsk(LOG), nwsk(LOG2).getWildValue, nwsk(knife).getWildValue)
    GameRegistry.addShapelessRecipe(lanterns(BlockDefinitions.Lantern.ON), lanterns(BlockDefinitions.Lantern.OFF), GLOWSTONE_DUST)
    GameRegistry.addShapelessRecipe(lanterns(BlockDefinitions.Lantern.ON), lanterns(BlockDefinitions.Lantern.ON), GLOWSTONE_DUST)
    GameRegistry.addShapelessRecipe(prts(Parts.MARSHMALLOWSTICK).toStack(3), prts(Parts.MARSHMALLOW), prts(Parts.STICK_IRON), prts(Parts.STICK_IRON), prts(Parts.STICK_IRON))
    GameRegistry.addRecipe(nwsk(tent, 1, BlockDefinitions.Tent.WHITE), "000", "0 0", "1 1", '0': Character, prts(Parts.CANVAS), '1': Character, prts(Parts.TENT_PEG));
    GameRegistry.addRecipe(nwsk(sleepingBag), "1  ", "000", '0': Character, nwsk(WOOL).getWildValue, '1': Character, nwsk(knife).getWildValue)
    GameRegistry.addRecipe(nwsk(sleepingBag), " 1 ", "000", '0': Character, nwsk(WOOL).getWildValue, '1': Character, nwsk(knife).getWildValue)
    GameRegistry.addRecipe(nwsk(sleepingBag), "  1", "000", '0': Character, nwsk(WOOL).getWildValue, '1': Character, nwsk(knife).getWildValue)
    GameRegistry.addRecipe(nwsk(backpack, 1, ItemDefinitions.Backpack.POUCH), " 0 ", "1 1", "111", '0': Character, STRING, '1': Character, LEATHER)
    GameRegistry.addRecipe(nwsk(backpack, 1, ItemDefinitions.Backpack.BACKPACK), " 0 ", "121", "111", '0': Character, STRING, '1': Character, prts(ItemDefinitions.Parts.CANVAS), '2':Character, nwsk(backpack, 1, ItemDefinitions.Backpack.POUCH))
    GameRegistry.addRecipe(nwsk(backpack, 1, ItemDefinitions.Backpack.RUCKSACK), " 0 ", "121", "111", '0': Character, STRING, '1': Character, nwsk(animalParts, 1, ItemDefinitions.PartsAnimal.FUR_BROWN), '2':Character, nwsk(backpack, 1, ItemDefinitions.Backpack.BACKPACK))
    GameRegistry.addShapelessRecipe(prts(Parts.CANVAS), hemp, nwsk(knife).getWildValue)
    for(i <- 0 until 16) GameRegistry.addShapelessRecipe(nwsk(tent, 1, i), nwsk(tent).getWildValue, nwsk(Items.DYE, 1, i))

    val partsAnimal = animalParts.getMetaCycle(animalParts.getItemInfo.getValue(PropType.METADATA).asInstanceOf[Array[_]].length)

    GameRegistry.addSmelting(nwsk(venisonRaw), nwsk(venisonCooked), 3)
    GameRegistry.addRecipe(nwsk(trap), " 1 ", "101", " 1 ", '0': Character, IRON_INGOT, '1': Character, prts(Parts.STICK_IRON))
    GameRegistry.addRecipe(nwsk(furHead), "000", "010", '0': Character, partsAnimal(PartsAnimal.FUR_WHITE), '1': Character, LEATHER_HELMET)
    GameRegistry.addRecipe(nwsk(furChest), "0 0", "010", "222", '0': Character, partsAnimal(PartsAnimal.FUR_WHITE), '1': Character, LEATHER_CHESTPLATE, '2': Character, partsAnimal(PartsAnimal.FUR_BROWN))
    GameRegistry.addRecipe(nwsk(furLeg), "000", "010", "0 0", '0': Character, partsAnimal(PartsAnimal.FUR_BROWN), '1': Character, LEATHER_LEGGINGS)
    GameRegistry.addRecipe(nwsk(furBoot), "0 0", "010", '0': Character, partsAnimal(PartsAnimal.FUR_BROWN), '1': Character, LEATHER_BOOTS)
  }
}
