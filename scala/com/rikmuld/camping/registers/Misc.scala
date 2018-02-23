package com.rikmuld.camping.registers

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.misc._
import com.rikmuld.camping.registers.Objs._
import com.rikmuld.camping.render.objs.{CampfireCookRender, TrapRender}
import com.rikmuld.camping.tileentity.{TileCampfireCook, TileTrap}
import com.rikmuld.corerm.old.BoundsStructure
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object ModMisc {
  @SideOnly(Side.CLIENT)
  def preRegisterClient(): Unit = {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
    //ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
  }

  @SideOnly(Side.CLIENT)
  def registerClient {
    keyOpenCamping = new KeyBinding(KeyInfo.desc(KeyInfo.INVENTORY_KEY), KeyInfo.default(KeyInfo.INVENTORY_KEY), KeyInfo.CATAGORY_MOD)
    ClientRegistry.registerKeyBinding(keyOpenCamping)
  }

  def register {
    val xLine = Array(1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0)
    val yLine = Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    val zLine = Array(0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2)
    tentStructure = BoundsStructure.regsisterStructure(xLine, yLine, zLine, true)

    //TODO put in potion registry, the potion bleeding
    bleedingSource = new DamageSourceBleeding(DamageInfo.BLEEDING)
    bleeding = new PotionBleeding(PotionInfo.BLEEDING)
  }
}
  