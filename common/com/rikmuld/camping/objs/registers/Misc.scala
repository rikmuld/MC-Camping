package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.objs.misc.PlayerExitLog
import com.rikmuld.corerm.RMMod
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraft.item.ItemFood
import net.minecraft.item.crafting.FurnaceRecipes
import com.rikmuld.camping.objs.misc.ItemsData
import com.rikmuld.camping.objs.misc.KeyData
import com.rikmuld.camping.objs.misc.NBTPlayer
import net.minecraft.item.ItemStack
import com.rikmuld.camping.objs.misc.PlayerSleepInTent
import com.rikmuld.corerm.bounds.BoundsData
import net.minecraftforge.fml.relauncher.SideOnly
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.camping.objs.misc.MapData
import com.rikmuld.camping.objs.misc.OpenGui
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.client.settings.KeyBinding
import com.rikmuld.camping.objs.misc.PotionBleeding
import com.rikmuld.camping.objs.misc.DamageSourceBleeding
import com.rikmuld.camping.misc.Pan
import com.rikmuld.corerm.bounds.BoundsStructure
import com.rikmuld.camping.misc.Spit
import net.minecraftforge.common.util.EnumHelper
import com.rikmuld.camping.misc.Grill
import com.rikmuld.camping.Lib._
import net.minecraft.init.Items._
import net.minecraft.init.Blocks._
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
      
        tab = new com.rikmuld.camping.objs.misc.Tab(MOD_ID)
        fur = EnumHelper.addArmorMaterial("FUR", "", 20, Array(2, 5, 4, 2), 20)
        
        //packats
        RMMod.registerPacket(classOf[OpenGui])
        RMMod.registerPacket(classOf[NBTPlayer])
        RMMod.registerPacket(classOf[KeyData])
        RMMod.registerPacket(classOf[ItemsData])
        RMMod.registerPacket(classOf[PlayerExitLog])  
        RMMod.registerPacket(classOf[BoundsData])
        RMMod.registerPacket(classOf[PlayerSleepInTent])
        RMMod.registerPacket(classOf[MapData])
                        
        if(!config.coreOnly){
          bleedingSource = new DamageSourceBleeding(DamageInfo.BLEEDING)
          bleeding = new PotionBleeding(PotionInfo.BLEEDING)
        }
      } else if(phase==ModRegister.POST){
        
        grill = new Grill(nwsk(kit, Kit.GRILL))
        spit = new Spit(nwsk(kit, Kit.SPIT))
        pan = new Pan(nwsk(kit, Kit.PAN))
        
        val ironStick = nwsk(parts, Parts.STICK_IRON)
        
        CookingEquipment.addEquipmentRecipe(spit, nwsk(stick), nwsk(stick), ironStick)
        CookingEquipment.addEquipmentRecipe(grill, nwsk(stick), nwsk(stick), nwsk(stick), nwsk(stick), ironStick, ironStick, nwsk(iron_bars))
        CookingEquipment.addEquipmentRecipe(pan, nwsk(stick), nwsk(stick), ironStick, nwsk(string), nwsk(parts, 1, Parts.PAN))
        
        grill.addFood(nwsk(fish), nwsk(cooked_fish))
        grill.addFood(nwsk(fish, 1), nwsk(cooked_fish, 1))
        grill.addFood(nwsk(beef), nwsk(cooked_beef))
        grill.addFood(nwsk(porkchop), nwsk(cooked_porkchop))
        grill.addFood(nwsk(venisonRaw), nwsk(venisonCooked))
        grill.addFood(nwsk(mutton), nwsk(cooked_mutton))
        pan.addFood(nwsk(potato), nwsk(baked_potato))
        pan.addFood(nwsk(rotten_flesh), nwsk(leather))
        spit.addFood(nwsk(chicken), nwsk(cooked_chicken))
        spit.addFood(nwsk(rabbit), nwsk(cooked_rabbit))
        spit.addFood(nwsk(fish), nwsk(cooked_fish))
        spit.addFood(nwsk(fish, 1), nwsk(cooked_fish, 1))
        
        FurnaceRecipes.instance.getSmeltingList.asInstanceOf[java.util.Map[ItemStack, ItemStack]].foreach(stacks => {
          if (stacks._1.getItem.isInstanceOf[ItemFood] && stacks._1.getItem.getUnlocalizedName != null) {
            if (stacks._1.getItem.asInstanceOf[ItemFood].isWolfsFavoriteMeat()) grill.addFood(stacks._1, stacks._2)
            else pan.addFood(stacks._1, stacks._2)
          }
        })
      }
    }
  }
  