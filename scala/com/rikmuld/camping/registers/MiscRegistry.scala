package com.rikmuld.camping.registers

import com.rikmuld.camping.CampingMod.{MOD_ID, config}
import com.rikmuld.camping.Library._
import com.rikmuld.camping.features.blocks.campfire.cook.RendererCampfireCook
import com.rikmuld.camping.features.blocks.campfire.wood
import com.rikmuld.camping.features.blocks.tent.{RendererTent, TileEntityTent}
import com.rikmuld.camping.features.blocks.trap.RendererTrap
import com.rikmuld.camping.features.entities.bear.{EntityBear, RendererBear}
import com.rikmuld.camping.features.entities.camper.{EntityCamper, RendererCamper}
import com.rikmuld.camping.features.entities.fox.{EntityFox, RendererFox}
import com.rikmuld.camping.features.inventory.TileEntityLight
import com.rikmuld.camping.world.WorldGenerator
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EnumCreatureType
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import net.minecraftforge.fml.client.registry.{ClientRegistry, IRenderFactory, RenderingRegistry}
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object MiscRegistry {
  var keyOpenCamping: KeyBinding =
    _

  def register(event: FMLInitializationEvent): Unit = {
    registerTileEntities(event)
    registerOthers(event)

    if(event.getSide == Side.CLIENT)
      registerKeys(event)
  }

  def register(event: FMLPostInitializationEvent): Unit =
    registerSpawn(event)

  def register(event: FMLPreInitializationEvent): Unit =
    if (event.getSide == Side.CLIENT)
      registerRenders(event)

  @SideOnly(Side.CLIENT)
  def registerRenders(event: FMLPreInitializationEvent): Unit = {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new RendererTrap)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[wood.TileCampfireCook], new RendererCampfireCook)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityTent], new RendererTent)

    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityBear], new IRenderFactory[EntityBear] {
      override def createRenderFor(manager: RenderManager): Render[_ >: EntityBear] = new RendererBear(manager)
    })
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityFox], new IRenderFactory[EntityFox] {
      override def createRenderFor(manager: RenderManager): Render[_ >: EntityFox] = new RendererFox(manager)
    })
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityCamper], new IRenderFactory[EntityCamper] {
      override def createRenderFor(manager: RenderManager): Render[_ >: EntityCamper] = new RendererCamper(manager)
    })
  }

  @SideOnly(Side.CLIENT)
  def registerKeys(event: FMLInitializationEvent): Unit = {
    keyOpenCamping = new KeyBinding(KeyInfo.INVENTORY_DESC, KeyInfo.INVENTORY_DEFAULT, KeyInfo.MOD_CAT)

    ClientRegistry.registerKeyBinding(keyOpenCamping)
  }

  def registerOthers(event: FMLInitializationEvent): Unit =
    GameRegistry.registerWorldGenerator(new WorldGenerator(), 9999)

  def registerTileEntities(event: FMLInitializationEvent): Unit = {
    GameRegistry.registerTileEntity(classOf[TileLantern], MOD_ID + "_tileLantern")
    GameRegistry.registerTileEntity(classOf[TileLogseat], MOD_ID + "_tileLogseat")
    GameRegistry.registerTileEntity(classOf[TileEntityLight], MOD_ID + "_tileLight")
    GameRegistry.registerTileEntity(classOf[TileTrap], MOD_ID + "_tileTrap")
    GameRegistry.registerTileEntity(classOf[wood.TileCampfireCook], MOD_ID + "_tileCampfireCook")
    GameRegistry.registerTileEntity(classOf[TileCampfireWoodOn], MOD_ID + "_tileCampfireWoodOn")
    GameRegistry.registerTileEntity(classOf[wood.TileCampfireWoodOff], MOD_ID + "_tileCampfireWoodOff")
    GameRegistry.registerTileEntity(classOf[TileEntityTent], MOD_ID + "_tileTent")
  }

  def registerSpawn(event: FMLPostInitializationEvent): Unit = {
    val forests = BiomeDictionary.getBiomes(Type.FOREST)
    val rivers = BiomeDictionary.getBiomes(Type.RIVER)
    val snow = BiomeDictionary.getBiomes(Type.SNOWY)

    if (config.useBears) {
      for (biome <- forests)
        EntityRegistry.addSpawn(classOf[EntityBear], 3, 2, 3, EnumCreatureType.CREATURE, biome)

      for (biome <- rivers)
        EntityRegistry.addSpawn(classOf[EntityBear], 4, 2, 4, EnumCreatureType.CREATURE, biome)
    }

    if (config.useFoxes)
      for (biome <- snow)
        EntityRegistry.addSpawn(classOf[EntityFox], 5, 2, 4, EnumCreatureType.CREATURE, biome)
  }
}
  