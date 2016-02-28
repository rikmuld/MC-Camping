package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.camping.objs.tile.TileTrap
import com.rikmuld.corerm.bounds.TileBounds
import net.minecraftforge.fml.common.registry.GameRegistry
import com.rikmuld.camping.objs.tile.TileCampfireWood
import com.rikmuld.camping.objs.tile.TileLantern
import com.rikmuld.camping.objs.tile.TileLight
import com.rikmuld.camping.objs.tile.TileLogseat
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.camping.CampingMod._
import com.rikmuld.corerm.misc.ModRegister

object ModTiles extends ModRegister {
    override def register {
      GameRegistry.registerTileEntity(classOf[TileLantern], MOD_ID + "_tileLantern")
      GameRegistry.registerTileEntity(classOf[TileLogseat], MOD_ID + "_tileLogseat")
      GameRegistry.registerTileEntity(classOf[TileLight], MOD_ID + "_tileLight")
      GameRegistry.registerTileEntity(classOf[TileTrap], MOD_ID + "_tileTrap")
      GameRegistry.registerTileEntity(classOf[TileCampfire], MOD_ID + "_tileCampfire")
      GameRegistry.registerTileEntity(classOf[TileCampfireCook], MOD_ID + "_tileCampfireCook")
      GameRegistry.registerTileEntity(classOf[TileCampfireWood], MOD_ID + "_tileCampfireWood")
      GameRegistry.registerTileEntity(classOf[TileBounds], MOD_ID + "_tileBounds")
      GameRegistry.registerTileEntity(classOf[TileTent], MOD_ID + "_tileTent")
    }
  }