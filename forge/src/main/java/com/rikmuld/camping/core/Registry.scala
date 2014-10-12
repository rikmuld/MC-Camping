package com.rikmuld.camping.core

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.client.gui.GuiBackpack
import com.rikmuld.camping.client.gui.GuiCampingInvCraft
import com.rikmuld.camping.client.gui.GuiCampinginv
import com.rikmuld.camping.client.gui.GuiCampinginv
import com.rikmuld.camping.common.inventory.gui.ContainerBackpack
import com.rikmuld.camping.common.inventory.gui.ContainerCampinv
import com.rikmuld.camping.common.inventory.gui.ContainerCampinvCraft
import com.rikmuld.camping.common.network.BasicPacketData
import com.rikmuld.camping.common.network.Handler
import com.rikmuld.camping.common.network.NBTPlayer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.common.network.PacketDataManager
import com.rikmuld.camping.common.network.PacketGlobal
import com.rikmuld.camping.common.network.TileData
import com.rikmuld.camping.common.objs.block.BlockMain
import com.rikmuld.camping.common.objs.block.Lantern
import com.rikmuld.camping.common.objs.block.Lantern
import com.rikmuld.camping.common.objs.block.Light
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
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.techne.TechneModel
import net.minecraftforge.client.model.techne.TechneModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import com.rikmuld.camping.common.inventory.gui.ContainerCampfire
import com.rikmuld.camping.client.gui.GuiCampfire
import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraftforge.client.MinecraftForgeClient
import com.rikmuld.camping.client.render.objs.CampfireItemRender
import com.rikmuld.camping.client.render.objs.CampfireRender
import com.rikmuld.camping.common.objs.block.Campfire
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire
import com.rikmuld.camping.common.objs.tile.TileEntityLight
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.item.ItemStack
import com.rikmuld.camping.misc.Grill
import com.rikmuld.camping.misc.Spit
import com.rikmuld.camping.misc.Pan
import net.minecraft.init.Items
import net.minecraft.init.Blocks
import com.rikmuld.camping.common.objs.item.Backpack
import com.rikmuld.camping.common.objs.item.Kit
import com.rikmuld.camping.common.objs.item.Backpack
import com.rikmuld.camping.common.inventory.gui.ContainerKit
import com.rikmuld.camping.client.gui.GuiKit

object Objs {
  var tab: CreativeTabs = _
  var network: SimpleNetworkWrapper = _
  var events: Events = _
  var knife, parts, backpack, kit: ItemMain = _
  var lantern, light, campfire: BlockMain = _
  var spit, grill, pan: CookingEquipment = _
  var config: Config = _
  var modelLoader: TechneModelLoader = _
  var campfireM: TechneModel = _
}

object MiscRegistry {
  def init(event: FMLInitializationEvent) {
    Objs.modelLoader = new TechneModelLoader();
    Objs.campfireM = Objs.modelLoader.loadInstance(new ResourceLocation(ModelInfo.CAMPFIRE)).asInstanceOf[TechneModel];
   
    PacketDataManager.registerPacketData(classOf[TileData].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[com.rikmuld.camping.common.network.Map].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[OpenGui].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[NBTPlayer].asInstanceOf[Class[BasicPacketData]])
    
    GameRegistry.registerTileEntity(classOf[TileEntityLantern], ModInfo.MOD_ID+"_lantern")
    GameRegistry.registerTileEntity(classOf[TileEntityLight], ModInfo.MOD_ID+"_light")
    GameRegistry.registerTileEntity(classOf[TileEntityCampfire], ModInfo.MOD_ID+"_campfire")
    
    val stick = new ItemStack(Items.stick)
    val ironStick = new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON)

    CookingEquipment.addEquipmentRecipe(Objs.spit, stick, stick, ironStick);
    CookingEquipment.addEquipmentRecipe(Objs.grill, stick, stick, stick, stick, ironStick, ironStick, new ItemStack(Blocks.iron_bars));
	CookingEquipment.addEquipmentRecipe(Objs.pan, stick, stick, ironStick, new ItemStack(Items.string), new ItemStack(Objs.parts, 1, PartInfo.PAN));
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
    
    CampingMod.proxy.registerGui(GuiInfo.GUI_KIT, classOf[ContainerKit].asInstanceOf[Class[Container]], classOf[GuiKit].asInstanceOf[Class[Gui]])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPFIRE, classOf[ContainerCampfire].asInstanceOf[Class[Container]], classOf[GuiCampfire].asInstanceOf[Class[Gui]])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV, classOf[ContainerCampinv].asInstanceOf[Class[Container]], classOf[GuiCampinginv].asInstanceOf[Class[Gui]])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV_CRAFT, classOf[ContainerCampinvCraft].asInstanceOf[Class[Container]], classOf[GuiCampingInvCraft].asInstanceOf[Class[Gui]])
    CampingMod.proxy.registerGui(GuiInfo.GUI_BACKPACK, classOf[ContainerBackpack].asInstanceOf[Class[Container]], classOf[GuiBackpack].asInstanceOf[Class[Gui]])
  }
  def postInit(event: FMLPostInitializationEvent) {}
  def initClient {
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objs.campfire), new CampfireItemRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityCampfire], new CampfireRender())
  }
  def initServer {}
}

object ObjRegistry {
  def preInit {
    Objs.knife = new Knife(classOf[KnifeInfo].asInstanceOf[Class[ObjInfo]])
    Objs.parts = new ItemMain(classOf[PartInfo].asInstanceOf[Class[ObjInfo]])
    Objs.backpack = new Backpack(classOf[BackpackInfo].asInstanceOf[Class[ObjInfo]])
    Objs.lantern = new Lantern(classOf[LanternInfo].asInstanceOf[Class[ObjInfo]])
    Objs.light = new Light(classOf[LightInfo].asInstanceOf[Class[ObjInfo]]);
    Objs.campfire = new Campfire(classOf[CampfireInfo].asInstanceOf[Class[ObjInfo]])
    Objs.kit = new Kit(classOf[KitInfo].asInstanceOf[Class[ObjInfo]])
    Objs.grill = new Grill(new ItemStack(Objs.kit, 1, KitInfo.KIT_GRILL))
    Objs.spit = new Spit(new ItemStack(Objs.kit, 1, KitInfo.KIT_SPIT))
    Objs.pan = new Pan(new ItemStack(Objs.kit, 1, KitInfo.KIT_PAN))
  }
  def register(block: Block, name: String) = GameRegistry.registerBlock(block, name)
  def register(item: Item, name: String) = GameRegistry.registerItem(item, name)
  def register(block: Block, name: String, itemBlock: Class[ItemBlock]) = GameRegistry.registerBlock(block, itemBlock, name)
}