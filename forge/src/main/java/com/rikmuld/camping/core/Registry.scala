package com.rikmuld.camping.core

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.client.gui.GuiBackpack
import com.rikmuld.camping.client.gui.GuiCampfire
import com.rikmuld.camping.client.gui.GuiCampfireCook
import com.rikmuld.camping.client.gui.GuiCampingInvCraft
import com.rikmuld.camping.client.gui.GuiCampinginv
import com.rikmuld.camping.client.gui.GuiKit
import com.rikmuld.camping.client.render.objs.CampfireCookItemRender
import com.rikmuld.camping.client.render.objs.CampfireCookRender
import com.rikmuld.camping.client.render.objs.CampfireItemRender
import com.rikmuld.camping.client.render.objs.CampfireRender
import com.rikmuld.camping.client.render.objs.LogItemRender
import com.rikmuld.camping.client.render.objs.LogRender
import com.rikmuld.camping.client.render.objs.SleepingBagRender
import com.rikmuld.camping.client.render.objs.TentItemRender
import com.rikmuld.camping.client.render.objs.TentRender
import com.rikmuld.camping.common.inventory.gui.ContainerBackpack
import com.rikmuld.camping.common.inventory.gui.ContainerCampfire
import com.rikmuld.camping.common.inventory.gui.ContainerCampfireCook
import com.rikmuld.camping.common.inventory.gui.ContainerCampinv
import com.rikmuld.camping.common.inventory.gui.ContainerCampinvCraft
import com.rikmuld.camping.common.inventory.gui.ContainerKit
import com.rikmuld.camping.common.inventory.gui.ContainerTentChests
import com.rikmuld.camping.common.inventory.gui.ContainerTentLanterns
import com.rikmuld.camping.common.network.BasicPacketData
import com.rikmuld.camping.common.network.BoundsData
import com.rikmuld.camping.common.network.Handler
import com.rikmuld.camping.common.network.Items
import com.rikmuld.camping.common.network.Map
import com.rikmuld.camping.common.network.NBTPlayer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.common.network.PacketDataManager
import com.rikmuld.camping.common.network.PacketGlobal
import com.rikmuld.camping.common.network.PlayerSleepInTent
import com.rikmuld.camping.common.network.TileData
import com.rikmuld.camping.common.objs.block.BoundsHelper
import com.rikmuld.camping.common.objs.block.Campfire
import com.rikmuld.camping.common.objs.block.CampfireCook
import com.rikmuld.camping.common.objs.block.Hemp
import com.rikmuld.camping.common.objs.block.Hemp
import com.rikmuld.camping.common.objs.block.Lantern
import com.rikmuld.camping.common.objs.block.Light
import com.rikmuld.camping.common.objs.block.Log
import com.rikmuld.camping.common.objs.block.SleepingBag
import com.rikmuld.camping.common.objs.block.Tent
import com.rikmuld.camping.common.objs.item.Backpack
import com.rikmuld.camping.common.objs.item.HempItem
import com.rikmuld.camping.common.objs.item.ItemBlockMain
import com.rikmuld.camping.common.objs.item.ItemMain
import com.rikmuld.camping.common.objs.item.Kit
import com.rikmuld.camping.common.objs.item.Knife
import com.rikmuld.camping.common.objs.item.Marshmallow
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import com.rikmuld.camping.common.objs.tile.TileEntityLantern
import com.rikmuld.camping.common.objs.tile.TileEntityLight
import com.rikmuld.camping.common.objs.tile.TileEntityLog
import com.rikmuld.camping.common.objs.tile.TileEntitySleepingBag
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import com.rikmuld.camping.common.objs.tile.TileEntityWithBounds
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation
import com.rikmuld.camping.common.world.WorldGenerator
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.misc.BoundsStructure
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.misc.Grill
import com.rikmuld.camping.misc.Pan
import com.rikmuld.camping.misc.Spit
import com.rikmuld.camping.misc.Tab

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.client.model.techne.TechneModel
import net.minecraftforge.client.model.techne.TechneModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import rikmuld.camping.client.gui.screen.GuiTent
import rikmuld.camping.client.gui.screen.GuiTentChests
import rikmuld.camping.client.gui.screen.GuiTentLanterns
import rikmuld.camping.client.gui.screen.GuiTentSleeping
import rikmuld.camping.misc.CustomModel
import rikmuld.camping.misc.CustomModelLoader

object Objs {
  var tab: CreativeTabs = _
  var network: SimpleNetworkWrapper = _
  var events: Events = _
  var eventsClient: EventsClient = _
  var knife, parts, backpack, kit, marshmallow, hempItem: Item = _
  var lantern, light, campfire, campfireCook, log, hemp, sleepingBag, bounds, tent: Block = _
  var spit, grill, pan: CookingEquipment = _
  var config: Config = _
  var modelLoader: TechneModelLoader = _
  var modelLoaderC: CustomModelLoader = _
  var campfireM, logM: TechneModel = _
  var tentM: CustomModel = _
  var tentStructure: Array[BoundsStructure] = _
}

object MiscRegistry {
  def init(event: FMLInitializationEvent) {
    PacketDataManager.registerPacketData(classOf[TileData].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[com.rikmuld.camping.common.network.Map].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[OpenGui].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[NBTPlayer].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[com.rikmuld.camping.common.network.Items].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[PlayerSleepInTent].asInstanceOf[Class[BasicPacketData]])
    PacketDataManager.registerPacketData(classOf[BoundsData].asInstanceOf[Class[BasicPacketData]])

    GameRegistry.registerTileEntity(classOf[TileEntityLantern], ModInfo.MOD_ID + "_lantern")
    GameRegistry.registerTileEntity(classOf[TileEntityLight], ModInfo.MOD_ID + "_light")
    GameRegistry.registerTileEntity(classOf[TileEntityCampfire], ModInfo.MOD_ID + "_campfire")
    GameRegistry.registerTileEntity(classOf[TileEntityWithRotation], ModInfo.MOD_ID + "_withRotation")
    GameRegistry.registerTileEntity(classOf[TileEntityCampfireCook], ModInfo.MOD_ID + "_campfireCook")
    GameRegistry.registerTileEntity(classOf[TileEntityLog], ModInfo.MOD_ID + "_log")
    GameRegistry.registerTileEntity(classOf[TileEntitySleepingBag], ModInfo.MOD_ID + "_sleepingBag")
    GameRegistry.registerTileEntity(classOf[TileEntityWithBounds], ModInfo.MOD_ID + "_bounds")
    GameRegistry.registerTileEntity(classOf[TileEntityTent], ModInfo.MOD_ID + "_tent")
    GameRegistry.registerWorldGenerator(new WorldGenerator(), 9999)

    val stick = new ItemStack(Items.stick)
    val ironStick = new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON)

    CookingEquipment.addEquipmentRecipe(Objs.spit, stick, stick, ironStick)
    CookingEquipment.addEquipmentRecipe(Objs.grill, stick, stick, stick, stick, ironStick, ironStick, new ItemStack(Blocks.iron_bars))
    CookingEquipment.addEquipmentRecipe(Objs.pan, stick, stick, ironStick, new ItemStack(Items.string), new ItemStack(Objs.parts, 1, PartInfo.PAN))
    CookingEquipment.addGrillFood(new ItemStack(Items.fish, 1, 0), new ItemStack(Items.cooked_fished, 1, 0))
    CookingEquipment.addGrillFood(new ItemStack(Items.fish, 1, 1), new ItemStack(Items.cooked_fished, 1, 1))
    CookingEquipment.addGrillFood(new ItemStack(Items.beef, 1, 0), new ItemStack(Items.cooked_beef, 1, 0))
    CookingEquipment.addGrillFood(new ItemStack(Items.porkchop, 1, 0), new ItemStack(Items.cooked_porkchop, 1, 0))
    //CookingEquipment.addGrillFood(ModItems.venisonRaw, 0, new ItemStack(ModItems.venisonCooked, 1, 0))
    CookingEquipment.addPanFood(new ItemStack(Items.potato, 1, 0), new ItemStack(Items.baked_potato, 1, 0))
    CookingEquipment.addPanFood(new ItemStack(Items.rotten_flesh, 1, 0), new ItemStack(Items.leather, 1, 0))
    //CookingEquipment.addSpitFood(ModItems.hareRaw, 0, new ItemStack(ModItems.hareCooked, 1, 0))
    CookingEquipment.addSpitFood(new ItemStack(Items.chicken, 1, 0), new ItemStack(Items.cooked_chicken, 1, 0))
    CookingEquipment.addSpitFood(new ItemStack(Items.fish, 1, 0), new ItemStack(Items.cooked_fished, 1, 0))
    CookingEquipment.addSpitFood(new ItemStack(Items.fish, 1, 1), new ItemStack(Items.cooked_fished, 1, 1))
  }
  def preInit(event: FMLPreInitializationEvent) {
    Objs.modelLoader = new TechneModelLoader()
    Objs.modelLoaderC = new CustomModelLoader()
    Objs.config = new Config(new Configuration(event.getSuggestedConfigurationFile()))
    Objs.config.sync
    Objs.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.PACKET_CHANEL)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.SERVER)
    Objs.network.registerMessage(classOf[Handler], classOf[PacketGlobal], 0, Side.CLIENT)
    Objs.tab = new Tab(ModInfo.MOD_ID)
    Objs.events = new Events
    MinecraftForge.EVENT_BUS.register(Objs.events)
    FMLCommonHandler.instance.bus.register(Objs.events)
  }
  def postInit(event: FMLPostInitializationEvent) {}
  @SideOnly(Side.CLIENT)
  def initClient {
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPFIRE_COOK, classOf[ContainerCampfireCook], classOf[GuiCampfireCook])
    CampingMod.proxy.registerGui(GuiInfo.GUI_KIT, classOf[ContainerKit], classOf[GuiKit])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPFIRE, classOf[ContainerCampfire], classOf[GuiCampfire])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV, classOf[ContainerCampinv], classOf[GuiCampinginv])
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV_CRAFT, classOf[ContainerCampinvCraft], classOf[GuiCampingInvCraft])
    CampingMod.proxy.registerGui(GuiInfo.GUI_BACKPACK, classOf[ContainerBackpack], classOf[GuiBackpack])
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT, null, classOf[GuiTent])
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_CHESTS, classOf[ContainerTentChests], classOf[GuiTentChests])
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_LANTERN, classOf[ContainerTentLanterns], classOf[GuiTentLanterns])
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_SLEEP, null, classOf[GuiTentSleeping])

    Objs.eventsClient = new EventsClient
    MinecraftForge.EVENT_BUS.register(Objs.eventsClient)
    FMLCommonHandler.instance.bus.register(Objs.eventsClient)

    Objs.campfireM = Objs.modelLoader.loadInstance(new ResourceLocation(ModelInfo.CAMPFIRE)).asInstanceOf[TechneModel];
    Objs.logM = Objs.modelLoader.loadInstance(new ResourceLocation(ModelInfo.LOG)).asInstanceOf[TechneModel];
    Objs.tentM = Objs.modelLoaderC.loadInstance(128, 64, new ResourceLocation(ModelInfo.TENT)).asInstanceOf[CustomModel];

    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objs.campfire), new CampfireItemRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityCampfire], new CampfireRender())
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objs.campfireCook), new CampfireCookItemRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityCampfireCook], new CampfireCookRender())
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objs.log), new LogItemRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityLog], new LogRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntitySleepingBag], new SleepingBagRender())
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Objs.tent), new TentItemRender())
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityTent], new TentRender())
  }
  def initServer {
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPFIRE_COOK, classOf[ContainerCampfireCook], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_KIT, classOf[ContainerKit], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPFIRE, classOf[ContainerCampfire], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV, classOf[ContainerCampinv], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_CAMPINV_CRAFT, classOf[ContainerCampinvCraft], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_BACKPACK, classOf[ContainerBackpack], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT, null, null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_CHESTS, classOf[ContainerTentChests], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_LANTERN, classOf[ContainerTentLanterns], null)
    CampingMod.proxy.registerGui(GuiInfo.GUI_TENT_SLEEP, null, null)
  }
}

object ObjRegistry {
  def preInit {
    Objs.knife = new Knife(classOf[KnifeInfo])
    Objs.parts = new ItemMain(classOf[PartInfo])
    Objs.backpack = new Backpack(classOf[BackpackInfo])
    Objs.lantern = new Lantern(classOf[LanternInfo])
    Objs.light = new Light(classOf[LightInfo]);
    Objs.campfire = new Campfire(classOf[CampfireInfo])
    Objs.campfireCook = new CampfireCook(classOf[CampfireCookInfo])
    Objs.kit = new Kit(classOf[KitInfo])
    Objs.marshmallow = new Marshmallow(classOf[MarshMallowInfo])
    Objs.log = new Log(classOf[LogInfo])
    Objs.hemp = new Hemp(classOf[HempInfo])
    Objs.hempItem = new HempItem(Objs.hemp, classOf[HempItemInfo])
    Objs.sleepingBag = new SleepingBag(classOf[SleepingBagInfo])
    Objs.bounds = new BoundsHelper(classOf[BoundsHelperInfo])
    Objs.tent = new Tent(classOf[TentInfo])

    Objs.grill = new Grill(new ItemStack(Objs.kit, 1, KitInfo.KIT_GRILL))
    Objs.spit = new Spit(new ItemStack(Objs.kit, 1, KitInfo.KIT_SPIT))
    Objs.pan = new Pan(new ItemStack(Objs.kit, 1, KitInfo.KIT_PAN))

    val xLine = Array(1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0)
    val yLine = Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    val zLine = Array(0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2)
    Objs.tentStructure = BoundsStructure.regsisterStructure(xLine, yLine, zLine, true)
  }
  def init {
    val dye = Items.dye.getMetaCycle(16)
    val parts = Objs.parts.getMetaCycle(Objs.parts.asInstanceOf[ItemMain].metadata.length)
    val lantern = Objs.lantern.getMetaCycle(2)

    GameRegistry.addRecipe(Objs.knife.toStack(1), "010", "010", "010", '0': Character, dye(1), '1': Character, Items.iron_ingot)
    GameRegistry.addRecipe(Objs.campfireCook.toStack(1), " 0 ", "0 0", " 0 ", '0': Character, Blocks.cobblestone)
    GameRegistry.addRecipe(Objs.campfire.toStack(1), " 0 ", "010", "020", '0': Character, Items.stick, '1': Character, Items.flint, '2': Character, Objs.campfireCook)
    GameRegistry.addRecipe(Objs.kit.toStack(1), "000", "111", "000", '0': Character, Items.iron_ingot, '1': Character, dye(11))
    GameRegistry.addRecipe(lantern(LanternInfo.LANTERN_ON).toStack(1), "000", "010", "222", '1': Character, Items.glowstone_dust, '0': Character, Blocks.glass_pane, '2': Character, Items.gold_ingot)
    GameRegistry.addRecipe(lantern(LanternInfo.LANTERN_OFF).toStack(1), "000", "0 0", "111", '1': Character, Items.gold_ingot, '0': Character, Blocks.glass_pane)
    GameRegistry.addRecipe(parts(PartInfo.STICK_IRON).toStack(1), " 0 ", " 0 ", '0': Character, Items.iron_ingot)
    GameRegistry.addRecipe(parts(PartInfo.PAN).toStack(1), " 0 ", "121", " 1 ", '0': Character, dye(1), '1': Character, Items.iron_ingot, '2': Character, Items.bowl)
    GameRegistry.addRecipe(parts(PartInfo.MARSHMALLOW).toStack(3), "010", "020", "030", '0': Character, Items.sugar, '1': Character, Items.potionitem, '2': Character, Items.egg, '3': Character, Items.bowl)
    GameRegistry.addShapelessRecipe(Objs.log.toStack(1), new ItemStack(Blocks.log, 1, 0).getWildValue, new ItemStack(Objs.knife).getWildValue)
    GameRegistry.addShapelessRecipe(Objs.log.toStack(1), new ItemStack(Blocks.log2, 1, 0).getWildValue, new ItemStack(Objs.knife).getWildValue)
    GameRegistry.addShapelessRecipe(lantern(LanternInfo.LANTERN_ON).toStack(1), lantern(LanternInfo.LANTERN_OFF), Items.glowstone_dust)
    GameRegistry.addShapelessRecipe(lantern(LanternInfo.LANTERN_ON).toStack(1), lantern(LanternInfo.LANTERN_ON), Items.glowstone_dust)
    GameRegistry.addShapelessRecipe(parts(PartInfo.MARSHMALLOWSTICK).toStack(3), parts(PartInfo.MARSHMALLOW), parts(PartInfo.STICK_IRON), parts(PartInfo.STICK_IRON), parts(PartInfo.STICK_IRON))
    GameRegistry.addRecipe(Objs.tent.toStack(1), "000", "0 0", "1 1", '0': Character, parts(PartInfo.CANVAS), '1': Character, parts(PartInfo.STICK_IRON));
    GameRegistry.addRecipe(Objs.sleepingBag.toStack(1), "1  ", "000", '0': Character, new ItemStack(Blocks.wool, 1, 0).getWildValue, '1': Character, new ItemStack(Objs.knife).getWildValue)
    GameRegistry.addRecipe(Objs.sleepingBag.toStack(1), " 1 ", "000", '0': Character, new ItemStack(Blocks.wool, 1, 0).getWildValue, '1': Character, new ItemStack(Objs.knife).getWildValue)
    GameRegistry.addRecipe(Objs.sleepingBag.toStack(1), "  1", "000", '0': Character, new ItemStack(Blocks.wool, 1, 0).getWildValue, '1': Character, new ItemStack(Objs.knife).getWildValue)
  	GameRegistry.addShapelessRecipe(parts(PartInfo.CANVAS).toStack(1), Objs.hemp, new ItemStack(Objs.knife).getWildValue);
    GameRegistry.addShapelessRecipe(Objs.tent.toStack(1), Objs.tent, new ItemStack(Items.dye).getWildValue);
  }
  def register(block: Block, name: String) = GameRegistry.registerBlock(block, name)
  def register(item: Item, name: String) = GameRegistry.registerItem(item, name)
  def register(block: Block, name: String, itemBlock: Class[ItemBlock]) = GameRegistry.registerBlock(block, itemBlock, name)
}