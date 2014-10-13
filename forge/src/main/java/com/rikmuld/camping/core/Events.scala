package com.rikmuld.camping.core

import java.util.ArrayList
import org.lwjgl.input.Mouse
import com.rikmuld.camping.client.gui.GuiMapHUD
import com.rikmuld.camping.common.inventory.gui.InventoryCampinv
import com.rikmuld.camping.common.network.NBTPlayer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.common.network.PacketSender
import com.rikmuld.camping.common.objs.item.IKnife
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import com.rikmuld.camping.core.Utils.ItemStackUtils
import com.rikmuld.camping.core.Utils.PlayerUtils
import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import cpw.mods.fml.relauncher.ReflectionHelper
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import cpw.mods.fml.relauncher.Side

class Events {
  var tickLight: Int = 0
  var marshupdate = 0
  
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
    if (!world.isRemote && (player.getCurrentEquippedItem != null) && 
      player.getCurrentEquippedItem.getItem == Objs.parts && (player.getCurrentEquippedItem.getItemDamage == PartInfo.MARSHMALLOWSTICK)) {
      val movingobjectposition = player.getMOP
      if (movingobjectposition != null) {
        val x = movingobjectposition.blockX
        val y = movingobjectposition.blockY
        val z = movingobjectposition.blockZ
        if (world.getBlock(x, y, z) == Objs.campfireCook && (Vec3.createVectorHelper(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ)) <= 2.5F)) {
          if (marshupdate > 80) {
            player.getCurrentEquippedItem.stackSize -= 1
            if (player.getCurrentEquippedItem.stackSize <= 0) player.setCurrentItem(null)
            if (!player.inventory.addItemStackToInventory(new ItemStack(Objs.marshmallow))) {
              player.dropPlayerItemWithRandomChoice(new ItemStack(Objs.marshmallow), false)
            }
            marshupdate = 0
          }
          val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityCampfireCook]
          if (tile.fuel > 0) marshupdate += 1
          else marshupdate = 0
        }
      }
    }
  }
}

@SideOnly(Side.CLIENT)
class EventsClient {
  var map: GuiMapHUD = _
  var pressFlag: Array[Boolean] = new Array[Boolean](1)

  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if (event.gui.isInstanceOf[GuiContainerCreative]) {
      ReflectionHelper.setPrivateValue(classOf[GuiContainerCreative], event.gui.asInstanceOf[GuiContainerCreative], CreativeTabs.tabInventory.getTabIndex, 2)
    }
  }
  @SubscribeEvent
  def onClientTick(event: ClientTickEvent) {
    if (event.phase != TickEvent.Phase.END) return
    val mc = FMLClientHandler.instance().getClient
    if (mc.currentScreen.isInstanceOf[GuiInventory] || mc.currentScreen.isInstanceOf[GuiContainerCreative]) {
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
    if (map == null) map = new GuiMapHUD()
    if (mc.thePlayer.hasMap()) {
      map.setWorldAndResolution(mc, event.resolution.getScaledWidth, event.resolution.getScaledHeight)
      map.drawScreen(event.mouseX, event.mouseY, event.partialTicks)
    }
  }
}