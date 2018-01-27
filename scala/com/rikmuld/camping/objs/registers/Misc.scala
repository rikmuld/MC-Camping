package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.advancements._
import com.rikmuld.camping.misc.{CookingEquipment, Grill, Pan, Spit}
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.misc._
import com.rikmuld.corerm.RMMod
import com.rikmuld.corerm.advancements.AdvancementTriggers
import com.rikmuld.corerm.features.bounds.{BoundsData, BoundsStructure}
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.corerm.utils.CoreUtils._
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks._
import net.minecraft.init.Items._
import net.minecraft.item.{ItemFood, ItemStack}
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object ModMisc extends ModRegister {
    @SideOnly(Side.CLIENT)
    override def registerClient {
      if(phase==ModRegister.PERI){
        keyOpenCamping = new KeyBinding(KeyInfo.desc(KeyInfo.INVENTORY_KEY), KeyInfo.default(KeyInfo.INVENTORY_KEY), KeyInfo.CATAGORY_MOD)
        ClientRegistry.registerKeyBinding(keyOpenCamping)
      }
    }
    
    override def register {
      if(phase==ModRegister.PERI){
        val xLine = Array(1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0)
        val yLine = Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        val zLine = Array(0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2)
        tentStructure = BoundsStructure.regsisterStructure(xLine, yLine, zLine, true)

        RMMod.registerPacket(classOf[OpenGui])
        RMMod.registerPacket(classOf[NBTPlayer])
        RMMod.registerPacket(classOf[KeyData])
        RMMod.registerPacket(classOf[ItemsData])
        RMMod.registerPacket(classOf[PlayerExitLog])  
        RMMod.registerPacket(classOf[BoundsData])
        RMMod.registerPacket(classOf[PlayerSleepInTent])
        RMMod.registerPacket(classOf[MapData])
                        
        bleedingSource = new DamageSourceBleeding(DamageInfo.BLEEDING)
        bleeding = new PotionBleeding(PotionInfo.BLEEDING)

        slept = AdvancementTriggers.registerAdvancementTrigger(new Slept.Trigger)
        campfireMade = AdvancementTriggers.registerAdvancementTrigger(new CampfireMade.Trigger)
        dyeBurned = AdvancementTriggers.registerAdvancementTrigger(new DyeBurned.Trigger)
        entityTrapped = AdvancementTriggers.registerAdvancementTrigger(new EntityTrapped.Trigger)
        foodRoasted = AdvancementTriggers.registerAdvancementTrigger(new FoodRoasted.Trigger)
        inventoryChanged = AdvancementTriggers.registerAdvancementTrigger(new InventoryChanged.Trigger)
        tentChanged = AdvancementTriggers.registerAdvancementTrigger(new TentChanged.Trigger)
        camperInteract = AdvancementTriggers.registerAdvancementTrigger(new CamperInteract.Trigger)
      } else if(phase==ModRegister.POST){
        
        grill = new Grill(nwsk(kit, Kit.GRILL))
        spit = new Spit(nwsk(kit, Kit.SPIT))
        pan = new Pan(nwsk(kit, Kit.PAN))
        
        val ironStick = nwsk(parts, Parts.STICK_IRON)
        
        CookingEquipment.addEquipmentRecipe(spit, nwsk(STICK), nwsk(STICK), ironStick)
        CookingEquipment.addEquipmentRecipe(grill, nwsk(STICK), nwsk(STICK), nwsk(STICK), nwsk(STICK), ironStick, ironStick, nwsk(IRON_BARS))
        CookingEquipment.addEquipmentRecipe(pan, nwsk(STICK), nwsk(STICK), ironStick, nwsk(STICK), nwsk(parts, 1, Parts.PAN))
        
        grill.addFood(nwsk(FISH), nwsk(COOKED_FISH))
        grill.addFood(nwsk(FISH, 1), nwsk(COOKED_FISH, 1))
        grill.addFood(nwsk(BEEF), nwsk(COOKED_BEEF))
        grill.addFood(nwsk(PORKCHOP), nwsk(COOKED_PORKCHOP))
        grill.addFood(nwsk(venisonRaw), nwsk(venisonCooked))
        grill.addFood(nwsk(MUTTON), nwsk(COOKED_MUTTON))
        pan.addFood(nwsk(POTATO), nwsk(BAKED_POTATO))
        pan.addFood(nwsk(ROTTEN_FLESH), nwsk(LEATHER))
        spit.addFood(nwsk(CHICKEN), nwsk(COOKED_CHICKEN))
        spit.addFood(nwsk(RABBIT), nwsk(COOKED_RABBIT))
        spit.addFood(nwsk(FISH), nwsk(COOKED_FISH))
        spit.addFood(nwsk(FISH, 1), nwsk(COOKED_FISH, 1))
        
        FurnaceRecipes.instance.getSmeltingList.asInstanceOf[java.util.Map[ItemStack, ItemStack]].foreach(stacks => {
          if (stacks._1.getItem.isInstanceOf[ItemFood] && stacks._1.getItem.getUnlocalizedName != null) {
            if (stacks._1.getItem.asInstanceOf[ItemFood].isWolfsFavoriteMeat()) grill.addFood(stacks._1, stacks._2)
            else pan.addFood(stacks._1, stacks._2)
          }
        })
      }
    }
  }
  