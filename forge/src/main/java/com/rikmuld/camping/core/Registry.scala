package com.rikmuld.camping.core

import com.rikmuld.camping.misc.Tab
import net.minecraft.item.ItemBlock
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraft.item.Item
import net.minecraft.block.Block
import com.rikmuld.camping.misc.Tab
import com.rikmuld.camping.common.network.PacketGlobal
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.common.network.Handler
import com.rikmuld.camping.misc.Tab
import com.rikmuld.camping.misc.Tab
import com.rikmuld.camping.common.network.BasicPacketData
import com.rikmuld.camping.common.network.PacketDataManager
import com.rikmuld.camping.common.network.TileData

object Objs {
  var tab: CreativeTabs = _
  var network: SimpleNetworkWrapper = _
  var events: Events = _
}

object MiscRegistry {
  def init(event: FMLInitializationEvent) {
      PacketDataManager.registerPacketData(classOf[TileData].asInstanceOf[Class[BasicPacketData]])
  }
  def preInit(event: FMLPreInitializationEvent) {
    Objs.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.PACKET_CHANEL)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.SERVER)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.CLIENT)
    Objs.tab = new Tab(ModInfo.MOD_ID)
    Objs.events = new Events;
    MinecraftForge.EVENT_BUS.register(Objs.events);
    FMLCommonHandler.instance.bus.register(Objs.events);
  }
  def postInit(event: FMLPostInitializationEvent) {}
  def initClient {}
  def initServer {}
}

object ObjRegistry {
  def preInit {

  }
  def register(block: Block, name: String) = GameRegistry.registerBlock(block, name)
  def register(item: Item, name: String) = GameRegistry.registerItem(item, name)
  def register(block: Block, name: String, itemBlock: Class[ItemBlock]) = GameRegistry.registerBlock(block, itemBlock, name)
}