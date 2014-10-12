package com.rikmuld.camping.core

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.client.event.ConfigChangedEvent
import com.rikmuld.camping.CampingMod
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.client.gui.GuiScreen
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent
import net.minecraft.client.gui.inventory.GuiInventory
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import cpw.mods.fml.relauncher.ReflectionHelper
import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.gui.inventory.GuiContainerCreative
import com.rikmuld.camping.common.inventory.gui.InventoryCampinv
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import com.rikmuld.camping.common.network.NBTPlayer
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import com.rikmuld.camping.common.network.OpenGui
import org.lwjgl.input.Mouse
import net.minecraft.client.gui.GuiButton
import cpw.mods.fml.common.gameevent.TickEvent
import net.minecraft.util.Vec3
import net.minecraft.item.ItemStack
import com.rikmuld.camping.common.objs.item.IKnife
import net.minecraft.init.Blocks
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import com.rikmuld.camping.common.network.PacketSender
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.client.gui.GuiMapHUD
import java.util.ArrayList

object Events {
  var map: GuiMapHUD = _
}

class Events {
  var pressFlag: Array[Boolean] = new Array[Boolean](1)
  var tickLight: Int = 0

  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
    if (eventArgs.modID.equals(ModInfo.MOD_ID)) Objs.config.sync
  }
  @SubscribeEvent
  def onPlayerDead(event: PlayerDropsEvent) = InventoryCampinv.dropItems(event.entityPlayer)
  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent) {
    if (event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING) != null) {
      PacketSender.to(new NBTPlayer(event.player.getEntityData.getCompoundTag("campInv")), event.player.asInstanceOf[EntityPlayerMP])
    }
  }
  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if (event.gui.isInstanceOf[GuiContainerCreative]) {
      ReflectionHelper.setPrivateValue(classOf[GuiContainerCreative], event.gui.asInstanceOf[GuiContainerCreative], CreativeTabs.tabInventory.getTabIndex, 2)
    }
  }
  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent) {
    for (slot <- 0 until event.craftMatrix.getSizeInventory if event.craftMatrix.getStackInSlot(slot) != null) {
      val itemInSlot = event.craftMatrix.getStackInSlot(slot)
      if ((itemInSlot.getItem != null) && (itemInSlot.getItem.isInstanceOf[IKnife])) {
        val returnStack = itemInSlot.addDamage(1)
        if (returnStack != null) {
          returnStack.stackSize += 1
        }
        event.craftMatrix.setInventorySlotContents(slot, returnStack)
      }
    }
  }
  @SubscribeEvent
  def onClientTick(event: ClientTickEvent) {
    if (event.phase != TickEvent.Phase.END) return
    val mc = FMLClientHandler.instance().getClient
    if (mc.currentScreen.isInstanceOf[GuiInventory] ||
      mc.currentScreen.isInstanceOf[GuiContainerCreative]) {
      val list: ArrayList[Any] = ReflectionHelper.getPrivateValue(classOf[GuiScreen], mc.currentScreen, 4)
      list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, 5, 5, 100, 10, "Camping Inventory"))
      ReflectionHelper.setPrivateValue(classOf[GuiScreen], mc.currentScreen, list, 4)
    }
  }

  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent) {
    if (event.`type` != ElementType.HOTBAR) return
    val mc = FMLClientHandler.instance().getClient
    if (mc.currentScreen.isInstanceOf[GuiInventory] || mc.currentScreen.isInstanceOf[GuiContainerCreative]) {
      if (Mouse.isButtonDown(0) && event.mouseX >= 5 && event.mouseX <= 105 && event.mouseY >= 5 && event.mouseY <= 15) {
        if (pressFlag(0)) {
          PacketSender.toServer(new OpenGui(GuiInfo.GUI_CAMPINV))
          pressFlag(0) = false
        }
      } else pressFlag(0) = true
    }
    if (Events.map == null) Events.map = new GuiMapHUD()
    if (mc.thePlayer.hasMap()) {
      Events.map.setWorldAndResolution(mc, event.resolution.getScaledWidth, event.resolution.getScaledHeight)
      Events.map.drawScreen(event.mouseX, event.mouseY, event.partialTicks)
    }
  }

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent) {
    val player = event.player
    val world = player.worldObj
    if (!world.isRemote && player.hasLantarn()) {
      tickLight += 1
      if (tickLight >= 10) {
        tickLight = 0
        player.lanternTick()
        if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt - 1, player.posZ.toInt) == Blocks.air) {
          player.worldObj.setBlock(player.posX.toInt, player.posY.toInt - 1, player.posZ.toInt, Objs.light)
        } else if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt, player.posZ.toInt) == Blocks.air) {
          player.worldObj.setBlock(player.posX.toInt, player.posY.toInt, player.posZ.toInt, Objs.light)
        } else if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt + 1, player.posZ.toInt) == Blocks.air) {
          player.worldObj.setBlock(player.posX.toInt, player.posY.toInt + 1, player.posZ.toInt, Objs.light)
        }
      }
    }
    if (!world.isRemote && player.hasMap()) {
      val data = player.getCurrentMapData()
      PacketSender.to(new com.rikmuld.camping.common.network.Map(data.scale, data.xCenter, data.zCenter, data.colors), player.asInstanceOf[EntityPlayerMP])
    }
  }
}