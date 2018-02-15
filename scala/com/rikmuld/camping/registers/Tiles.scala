package com.rikmuld.camping.registers

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.tileentity.{TileLantern, TileTrap}
import net.minecraftforge.fml.common.registry.GameRegistry

object ModTiles {
    def register {
      GameRegistry.registerTileEntity(classOf[TileLantern], MOD_ID + "_tileLantern")
//      GameRegistry.registerTileEntity(classOf[TileLogseat], MOD_ID + "_tileLogseat")
//      GameRegistry.registerTileEntity(classOf[TileLight], MOD_ID + "_tileLight")
      GameRegistry.registerTileEntity(classOf[TileTrap], MOD_ID + "_tileTrap")
//      GameRegistry.registerTileEntity(classOf[TileCampfire], MOD_ID + "_tileCampfire")
//      GameRegistry.registerTileEntity(classOf[TileCampfireCook], MOD_ID + "_tileCampfireCook")
//      GameRegistry.registerTileEntity(classOf[TileCampfireWood], MOD_ID + "_tileCampfireWood")
//      GameRegistry.registerTileEntity(classOf[TileTent], MOD_ID + "_tileTent")
    }
  }