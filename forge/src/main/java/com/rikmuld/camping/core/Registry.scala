package com.rikmuld.camping.core

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.client.GuiCampingInvCraft
import com.rikmuld.camping.client.GuiCampinginv
import com.rikmuld.camping.client.GuiCampinginv
import com.rikmuld.camping.common.inventory.ContainerCampinv
import com.rikmuld.camping.common.inventory.ContainerCampinvCraft
import com.rikmuld.camping.common.network.BasicPacketData
import com.rikmuld.camping.common.network.Handler
import com.rikmuld.camping.common.network.PacketDataManager
import com.rikmuld.camping.common.network.PacketGlobal
import com.rikmuld.camping.common.network.TileData
import com.rikmuld.camping.common.objs.block.BlockMain
import com.rikmuld.camping.common.objs.block.Lantern
import com.rikmuld.camping.common.objs.item.Backpack
import com.rikmuld.camping.common.objs.item.ItemMain
import com.rikmuld.camping.common.objs.item.Knife
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import com.rikmuld.camping.misc.Tab
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import net.minecraft.block.Block
import net.minecraft.client.gui.Gui
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.Container
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import com.rikmuld.camping.common.network.NBTPlayer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.common.objs.block.Lantern
import com.rikmuld.camping.common.objs.block.Light
import com.rikmuld.camping.common.objs.tile.TileLight

object Objs {
  var tab: CreativeTabs = _
  var network: SimpleNetworkWrapper = _
  var events: Events = _
  var knife, parts, backpack: ItemMain = _
  var lantern, light: BlockMain = _
  var config: Config = _
}

object MiscRegistry {
  def init(event: FMLInitializationEvent) {
      PacketDataManager.registerPacketData(classOf[TileData].asInstanceOf[Class[BasicPacketData]])
      PacketDataManager.registerPacketData(classOf[com.rikmuld.camping.common.network.Map].asInstanceOf[Class[BasicPacketData]])
      PacketDataManager.registerPacketData(classOf[OpenGui].asInstanceOf[Class[BasicPacketData]])
      PacketDataManager.registerPacketData(classOf[NBTPlayer].asInstanceOf[Class[BasicPacketData]])
  }
  def preInit(event: FMLPreInitializationEvent) {
    Objs.config = new Config(new Configuration(event.getSuggestedConfigurationFile()))
    Objs.config.sync
    Objs.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.PACKET_CHANEL)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.SERVER)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.CLIENT)
    Objs.tab = new Tab(ModInfo.MOD_ID)
    Objs.events = new Events
    MinecraftForge.EVENT_BUS.register(Objs.events)
    FMLCommonHandler.instance.bus.register(Objs.events)
    
    GameRegistry.registerTileEntity(classOf[TileEntityLantern], classOf[TileEntityLantern].toString())
    GameRegistry.registerTileEntity(classOf[TileLight], classOf[TileLight].toString())

    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV, classOf[ContainerCampinv].asInstanceOf[Class[Container]], classOf[GuiCampinginv].asInstanceOf[Class[Gui]])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV_CRAFT, classOf[ContainerCampinvCraft].asInstanceOf[Class[Container]], classOf[GuiCampingInvCraft].asInstanceOf[Class[Gui]])
  }
  def postInit(event: FMLPostInitializationEvent) {}
  def initClient {}
  def initServer {}
}

object ObjRegistry {
  def preInit {
	  Objs.knife = new Knife(classOf[KnifeInfo].asInstanceOf[Class[ObjInfo]])
	  Objs.parts = new ItemMain(classOf[PartInfo].asInstanceOf[Class[ObjInfo]])
	  Objs.backpack = new Backpack(classOf[BackpackInfo].asInstanceOf[Class[ObjInfo]])
	  Objs.lantern = new Lantern(classOf[LanternInfo].asInstanceOf[Class[ObjInfo]])
	  Objs.light = new Light(classOf[LightInfo].asInstanceOf[Class[ObjInfo]]);
  }
  def register(block: Block, name: String) = GameRegistry.registerBlock(block, name)
  def register(item: Item, name: String) = GameRegistry.registerItem(item, name)
  def register(block: Block, name: String, itemBlock: Class[ItemBlock]) = GameRegistry.registerBlock(block, itemBlock, name)
}