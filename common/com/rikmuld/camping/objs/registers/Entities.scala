package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.objs.entity.FoxRenderer
import com.rikmuld.camping.objs.entity.BearRenderer
import net.minecraftforge.fml.common.registry.GameRegistry
import com.rikmuld.corerm.CoreUtils
import com.rikmuld.camping.objs.entity.Fox
import com.rikmuld.camping.objs.entity.Bear
import com.rikmuld.camping.render.models.FoxModel
import net.minecraftforge.fml.client.registry.{IRenderFactory, RenderingRegistry}
import net.minecraftforge.fml.common.registry.EntityRegistry
import com.rikmuld.camping.render.models.BearModel
import net.minecraft.entity.EntityList
import com.rikmuld.camping.objs.entity.CamperRender
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.entity.Camper
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.relauncher.SideOnly
import com.rikmuld.corerm.misc.ModRegister
import net.minecraft.entity.Entity
import com.rikmuld.camping.world.WorldGenerator
import net.minecraftforge.common.BiomeDictionary.Type
import net.minecraft.entity.EnumCreatureType
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.CampingMod._
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.util.ResourceLocation

import scala.collection.JavaConversions._

object ModEntities extends ModRegister{
  override def register {
    if(phase==ModRegister.PERI){
      registerEntity(classOf[Bear].asInstanceOf[Class[Entity]], "bearGrizzly", EntityInfo.BEAR, true, 0x583B2D, 0xE2B572)
      registerEntity(classOf[Fox].asInstanceOf[Class[Entity]], "foxArctic", EntityInfo.FOX, true, 0xE0EEEE, 0x362819)
      registerEntity(classOf[Camper].asInstanceOf[Class[Entity]], "camper", EntityInfo.CAMPER, true, 0x747B51, 0x70471B)

      GameRegistry.registerWorldGenerator(new WorldGenerator(), 9999)
    } else if(phase == ModRegister.POST){
      val forests = BiomeDictionary.getBiomes(Type.FOREST)
      val rivers = BiomeDictionary.getBiomes(Type.RIVER)
      val snow = BiomeDictionary.getBiomes(Type.SNOWY)

      if (config.useBears) {
        for (biome <- forests) EntityRegistry.addSpawn(classOf[Bear], 3, 2, 3, EnumCreatureType.CREATURE, biome)
        for (biome <- rivers) EntityRegistry.addSpawn(classOf[Bear], 4, 2, 4, EnumCreatureType.CREATURE, biome)
      }
      if (config.useFoxes) for (biome <- snow) EntityRegistry.addSpawn(classOf[Fox], 5, 2, 4, EnumCreatureType.CREATURE, biome)
    }
  }
  @SideOnly(Side.CLIENT)
  override def registerClient {
    if(phase == ModRegister.PRE){
      RenderingRegistry.registerEntityRenderingHandler(classOf[Bear], new IRenderFactory[Bear] {
        override def createRenderFor(manager: RenderManager): Render[_ >: Bear] = new BearRenderer(manager)
      })
      RenderingRegistry.registerEntityRenderingHandler(classOf[Fox], new IRenderFactory[Fox] {
        override def createRenderFor(manager: RenderManager): Render[_ >: Fox] = new FoxRenderer(manager)
      })
      RenderingRegistry.registerEntityRenderingHandler(classOf[Camper], new IRenderFactory[Camper] {
        override def createRenderFor(manager: RenderManager): Render[_ >: Camper] = new CamperRender(manager)
      })
    }
  }
  private def registerEntity(entity: Class[Entity], name: String, id: Int, egg: Boolean, colour1: Int, colour2: Int) {
    if (egg) {
      val loc = new ResourceLocation(MOD_ID, name)

      EntityRegistry.registerModEntity(loc, entity, name, id, CampingMod, 80, 3, false, colour1, colour2)
      com.rikmuld.camping.objs.misc.TabData.eggIds.append(loc)
    } else {
      EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, name), entity, name, id, CampingMod, 80, 3, false)
    }
  }
}