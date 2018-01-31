package com.rikmuld.camping.registers

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.advancements._
import com.rikmuld.camping.misc._
import com.rikmuld.camping.objs.ItemDefinitions._
import Objs._
import com.rikmuld.camping.objs
import com.rikmuld.corerm.network.PacketWrapper
import com.rikmuld.corerm.old.BoundsStructure
import com.rikmuld.camping.CampingMod._
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks._
import net.minecraft.init.Items._
import net.minecraft.init.SoundEvents
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.{ItemFood, ItemStack}
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object ModMisc {
    def preRegister(): Unit = {
      tab = new com.rikmuld.camping.misc.Tab(MOD_ID)
      fur = EnumHelper.addArmorMaterial("FUR", "", 20, Array(2, 5, 4, 2), 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0)
    }

    def preRegisterClient(): Unit = {
//      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
//      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfire], new CampfireRender)
//      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
//      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
    }

    @SideOnly(Side.CLIENT)
    def registerClient {
      keyOpenCamping = new KeyBinding(KeyInfo.desc(KeyInfo.INVENTORY_KEY), KeyInfo.default(KeyInfo.INVENTORY_KEY), KeyInfo.CATAGORY_MOD)
      ClientRegistry.registerKeyBinding(keyOpenCamping)
    }
    
    def register {
      GameRegistry.addSmelting(new ItemStack(venisonRaw), new ItemStack(venisonCooked), 3)

      val xLine = Array(1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0)
      val yLine = Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)
      val zLine = Array(0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2)
      tentStructure = BoundsStructure.regsisterStructure(xLine, yLine, zLine, true)

      bleedingSource = new DamageSourceBleeding(DamageInfo.BLEEDING)
      bleeding = new PotionBleeding(PotionInfo.BLEEDING)

//      grill = new Grill(new ItemStack(kit, 1, Kit.GRILL))
//      spit = new Spit(new ItemStack(kit, 1, Kit.SPIT))
//      pan = new Pan(new ItemStack(kit, 1, Kit.PAN))
//
//      val stick = new ItemStack(STICK)
//      val ironStick = new ItemStack(parts, 1, Parts.STICK_IRON)
//
//      CookingEquipment.addEquipmentRecipe(spit, stick, stick, ironStick)
//      CookingEquipment.addEquipmentRecipe(grill, stick, stick, stick, stick, ironStick, ironStick, new ItemStack(IRON_BARS))
//      CookingEquipment.addEquipmentRecipe(pan, stick, stick, ironStick, stick, new ItemStack(parts, 1, Parts.PAN))
//
//      grill.addFood(new ItemStack(FISH), new ItemStack(COOKED_FISH))
//      grill.addFood(new ItemStack(FISH, 1, 1), new ItemStack(COOKED_FISH, 1, 1))
//      grill.addFood(new ItemStack(BEEF), new ItemStack(COOKED_BEEF))
//      grill.addFood(new ItemStack(PORKCHOP), new ItemStack(COOKED_PORKCHOP))
//      grill.addFood(new ItemStack(venisonRaw), new ItemStack(venisonCooked))
//      grill.addFood(new ItemStack(MUTTON), new ItemStack(COOKED_MUTTON))
//      pan.addFood(new ItemStack(POTATO), new ItemStack(BAKED_POTATO))
//      pan.addFood(new ItemStack(ROTTEN_FLESH), new ItemStack(LEATHER))
//      spit.addFood(new ItemStack(CHICKEN), new ItemStack(COOKED_CHICKEN))
//      spit.addFood(new ItemStack(RABBIT), new ItemStack(COOKED_RABBIT))
//      spit.addFood(new ItemStack(FISH), new ItemStack(COOKED_FISH))
//      spit.addFood(new ItemStack(FISH, 1, 1), new ItemStack(COOKED_FISH, 1, 1))
//
//      FurnaceRecipes.instance.getSmeltingList.foreach(stacks => {
//        if (stacks._1.getItem.isInstanceOf[ItemFood] && stacks._1.getItem.getUnlocalizedName != null) {
//          if (stacks._1.getItem.asInstanceOf[ItemFood].isWolfsFavoriteMeat()) grill.addFood(stacks._1, stacks._2)
//          else pan.addFood(stacks._1, stacks._2)
//        }
//      })
    }
  }
  