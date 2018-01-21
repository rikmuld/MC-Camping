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
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.RMMod
import net.minecraft.block.Block
import net.minecraft.init.{Blocks, SoundEvents}
import net.minecraft.item.{Item, ItemBlock, ItemStack}
import net.minecraft.world.IBlockAccess
import net.minecraft.util.EnumBlockRenderType
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.RegistryEvent

object ModBlocks {
  var
    campfireCookItem,
    campfireWoodItem,
    sleepingBagItem,
    tentItem,
    logSeatItem,
    lanternItem,
    trapItem,
    hempItem: ItemBlock = _

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
      override def createNewTileEntity(world: World, meta: Int): RMTile = new TileLight()
      override def getCollisionBoundingBox(state:IBlockState, world: IBlockAccess, pos:BlockPos): AxisAlignedBB = null
      override def getRenderType(state:IBlockState) = EnumBlockRenderType.INVISIBLE
      override def isReplaceable(world: IBlockAccess, pos:BlockPos) = true
      override def canCollideCheck(state:IBlockState, hitIfLiquid:Boolean) = false
    }
  }

  def registerBlocks(event: RegistryEvent.Register[Block]): Unit = {
    campfireCookItem = campfireCook.getInfo.register(event, campfireCook, MOD_ID)
    campfireWoodItem = campfireWood.getInfo.register(event, campfireWood, MOD_ID)
    lanternItem = lantern.getInfo.register(event, lantern, MOD_ID)
    logSeatItem = logseat.getInfo.register(event, logseat, MOD_ID)
    tentItem = tent.getInfo.register(event, tent, MOD_ID)
    trapItem = trap.getInfo.register(event, trap, MOD_ID)
    sleepingBagItem = sleepingBag.getInfo.register(event, sleepingBag, MOD_ID)
    hempItem = hemp.getInfo.register(event, hemp, MOD_ID)

    light.getInfo.register(event, light, MOD_ID)
    tentBounds.getInfo.register(event, tentBounds, MOD_ID)
  }

  def registerItemBlocks(event: RegistryEvent.Register[Item]): Unit = {
    event.getRegistry.registerAll(
      campfireWoodItem,
      campfireCookItem,
      lanternItem,
      logSeatItem,
      trapItem,
      tentItem,
      sleepingBagItem,
      hempItem
    )
  }

  def registerModels(event: ModelRegistryEvent): Unit = {
    RMMod.proxy.registerRendersFor(event, campfireCookItem, campfireCook.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, campfireWoodItem, campfireWood.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, logSeatItem, logseat.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, lanternItem, lantern.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, trapItem, trap.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, tentItem, tent.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, sleepingBagItem, sleepingBag.getInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, hempItem, hemp.getInfo, MOD_ID)
  }
     
  @SideOnly(Side.CLIENT)
  def registerClient() {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfire], new CampfireRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
  }
}