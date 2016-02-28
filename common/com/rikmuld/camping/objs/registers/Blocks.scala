package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.objs.block.Campfire
import com.rikmuld.camping.objs.block.CampfireCook
import com.rikmuld.camping.objs.block.CampfireWood
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.camping.objs.block.Lantern
import com.rikmuld.camping.objs.block.Logseat
import com.rikmuld.camping.objs.block.SleepingBag
import com.rikmuld.camping.objs.block.Tent
import com.rikmuld.camping.objs.block.TentBounds
import com.rikmuld.camping.objs.block.Trap
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.camping.objs.tile.TileLight
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.objs.tile.TileTrap
import com.rikmuld.camping.render.objs.CampfireCookRender
import com.rikmuld.camping.render.objs.CampfireRender
import com.rikmuld.camping.render.objs.TentRender
import com.rikmuld.camping.render.objs.TrapRender
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.block.state.IBlockState
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.objs.BlockDefinitions._

object ModBlocks extends ModRegister {
  override def register {
    campfireCook = new CampfireCook(MOD_ID, CAMPFIRE_COOK)
    campfireWood = new CampfireWood(MOD_ID, CAMPFIRE_WOOD)
    campfire = new Campfire(MOD_ID, CAMPFIRE)
    sleepingBag = new SleepingBag(MOD_ID, SLEEPING_BAG)
    tent = new Tent(MOD_ID, TENT)
    logseat = new Logseat(MOD_ID, LOGSEAT)
    lantern = new Lantern(MOD_ID, LANTERN)
    if(!config.coreOnly) trap = new Trap(MOD_ID, TRAP) 
    tentBounds = new TentBounds(MOD_ID, BOUNDS_TENT)
    hemp = new Hemp(MOD_ID, HEMP)
    light = new RMBlockContainer(MOD_ID, LIGHT) with WithModel {
      setBlockBounds(0, 0, 0, 0, 0, 0)
      
      override def createNewTileEntity(world: World, meta: Int): RMTile = new TileLight()
      override def getCollisionBoundingBox(world: World, pos:BlockPos, state:IBlockState): AxisAlignedBB = null
      override def getRenderType = -1
      override def isReplaceable(world: World, pos:BlockPos) = true
      override def canCollideCheck(state:IBlockState, hitIfLiquid:Boolean) = false
    }
  }
     
  @SideOnly(Side.CLIENT)
  override def registerClient {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfire], new CampfireRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
  }
}