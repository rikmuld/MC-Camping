package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.block._
import com.rikmuld.camping.objs.tile._
import com.rikmuld.camping.render.objs.{CampfireCookRender, CampfireRender, TentRender, TrapRender}
import com.rikmuld.corerm.objs.blocks.{RMBlockContainer, WithModel}
import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object ModBlocks {
  def preInit(): Unit = {
    tab = new com.rikmuld.camping.objs.misc.Tab(MOD_ID)
    fur = EnumHelper.addArmorMaterial("FUR", "", 20, Array(2, 5, 4, 2), 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0)
  }

  def createBlocks() {
    campfireCook = new CampfireCook(MOD_ID, CAMPFIRE_COOK)
    campfireWood = new CampfireWood(MOD_ID, CAMPFIRE_WOOD)
    //campfire = new Campfire(MOD_ID, CAMPFIRE)
    sleepingBag = new SleepingBag(MOD_ID, SLEEPING_BAG)
    tent = new Tent(MOD_ID, TENT)
    logseat = new Logseat(MOD_ID, LOGSEAT)
    lantern = new Lantern(MOD_ID, LANTERN)
    trap = new Trap(MOD_ID, TRAP)
    tentBounds = new TentBounds(MOD_ID, BOUNDS_TENT)
    hemp = new Hemp(MOD_ID, HEMP)
    light = new RMBlockContainer(MOD_ID, LIGHT) with WithModel {
      override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB = null
      override def createNewTileEntity(world: World, meta: Int): TileEntitySimple = new TileLight()
      override def getCollisionBoundingBox(state:IBlockState, world: IBlockAccess, pos:BlockPos): AxisAlignedBB = null
      override def getRenderType(state:IBlockState) = EnumBlockRenderType.INVISIBLE
      override def isReplaceable(world: IBlockAccess, pos:BlockPos) = true
      override def canCollideCheck(state:IBlockState, hitIfLiquid:Boolean) = false
    }
  }

  def registerBlocks(event: RegistryEvent.Register[Block]): Unit =
    event.getRegistry.registerAll(
      campfireCook,
      campfireWood,
      lantern,
      logseat,
      tent,
      trap,
      sleepingBag,
      hemp,
      light,
      tentBounds
    )
     
  @SideOnly(Side.CLIENT)
  def registerClient() {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfire], new CampfireRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
  }
}