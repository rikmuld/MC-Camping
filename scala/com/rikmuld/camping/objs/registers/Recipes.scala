package com.rikmuld.camping.objs.registers

import java.io.{File, PrintWriter}

import com.rikmuld.camping.objs.{BlockDefinitions, ItemDefinitions}
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.corerm.objs.PropType
import com.rikmuld.corerm.utils.CoreUtils._
import net.minecraft.block.Block
import net.minecraft.init.Blocks._
import net.minecraft.init.Items
import net.minecraft.init.Items._
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fml.common.registry.GameRegistry

object ModRecipes {
  def register {
    GameRegistry.addSmelting(nwsk(venisonRaw), nwsk(venisonCooked), 3)

    val dye = Items.DYE.getMetaCycle(16)
    val prts = parts.getMetaCycle(parts.getItemInfo.getValue(PropType.METADATA).asInstanceOf[Array[_]].length)
    val lanterns = lantern.getMetaCycle(2)

    createRecipeFile("tent", nwsk(tent, 1, BlockDefinitions.Tent.WHITE), List("000", "0 0", "1 1"), Map('0' -> prts(Parts.CANVAS), '1' -> prts(Parts.TENT_PEG)));
    createShapelessRecipe("lantern_lantern_on_1", lanterns(BlockDefinitions.Lantern.ON), List(lanterns(BlockDefinitions.Lantern.OFF), GLOWSTONE_DUST))
    createShapelessRecipe("lantern_lantern_on_2", lanterns(BlockDefinitions.Lantern.ON), List(lanterns(BlockDefinitions.Lantern.ON), GLOWSTONE_DUST))
    createRecipeFile("sleeping_bag", nwsk(sleepingBag), List("1", "000"), Map('0' -> nwsk(WOOL).getWildValue, '1' -> nwsk(knife).getWildValue))
    createRecipeFile("lantern_lantern_on", lanterns(BlockDefinitions.Lantern.ON), List("000", "010", "222"), Map('1' -> GLOWSTONE_DUST, '0' -> GLASS_PANE, '2' -> GOLD_INGOT))
  }

  def createRecipeFile(name: String, result: ItemStack, pattern: Seq[String], charMap: Map[Char, Object]): Unit = {
    val json = s"""
       |{
       |  "type": "minecraft:crafting_shaped",
       |  "pattern": [
       |    ${pattern.foldLeft(""){case (acc, next) => acc + s""""$next","""}.dropRight(1)}
       |  ],
       |  "key": {
       |    ${charMap.foldLeft(""){case (acc, (char, obj)) => acc + createKeyElement(char, obj)}.dropRight(1)}
       |  },
       |  "result": ${createItemInfo(result)}
       |}
     """.stripMargin

    val pw = new PrintWriter(new File(name + ".json"))
    pw.write(json)
    pw.close
  }

  def createItemInfo(o: Object): String = o match {
    case stack: ItemStack =>
      s"""{
         |  "item": "${stack.getItem.getRegistryName.toString}",
         |  "data": ${stack.getItemDamage},
         |  "count": ${stack.getCount}
         |}
       """.stripMargin
    case item: Item => createItemInfo(new ItemStack(item, 1, 0))
    case block: Block => createItemInfo(new ItemStack(block, 1, 0))
  }

  def createKeyElement(c: Char, value: Object): String =
    s"""
      | "$c": ${createItemInfo(value)},
    """.stripMargin

  def createShapelessRecipe(name: String, result: ItemStack, ingredients: Seq[Object]): Unit = {
    val json = s"""
       |{
       |  "type": "minecraft:crafting_shapeless",
       |  "ingredients": [
       |    ${ingredients.foldLeft("") { case (acc, obj) => acc + createItemInfo(obj) + "," }.dropRight(1)}
       |  ],
       |  "result": ${createItemInfo(result)}
       |}
     """.stripMargin

    val pw = new PrintWriter(new File(name + ".json"))
    pw.write(json)
    pw.close
  }
}
