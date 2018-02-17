package com.rikmuld.camping

import java.util.{Random, UUID}

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.inventory.camping.InventoryCamping
import com.rikmuld.camping.inventory.gui.GuiMapHUD
import com.rikmuld.camping.misc.{KeyData, MapData, NBTPlayer}
import com.rikmuld.camping.objs.Definitions._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.camping.objs.blocks.Hemp
import com.rikmuld.camping.tileentity.TileTrap
import com.rikmuld.corerm.gui.GuiHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase
//import com.rikmuld.camping.objs.blocks.{Hemp, Tent}
//import com.rikmuld.camping.objs.misc.{MapData, NBTPlayer}
//import com.rikmuld.camping.tileentity.{Roaster, TileTrap}
import com.rikmuld.camping.registers.Objs
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.entity.SharedMonsterAttributes._
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.init.Items
import net.minecraft.item.{ItemArmor, ItemStack}
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.{GuiOpenEvent, RenderGameOverlayEvent}
import net.minecraftforge.event.entity.player.{BonemealEvent, PlayerDropsEvent}
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.{ItemCraftedEvent, PlayerLoggedInEvent, PlayerRespawnEvent}
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

class EventsS {
  var tickLight: Int = 0
  var marshupdate: Float = 0
  val UUIDSpeedCamping = new UUID(new Random(83746763).nextLong, new Random(28647556).nextLong)

  @SubscribeEvent
  def onBoneMealUsed(event: BonemealEvent): Unit =
    if (event.getBlock.getBlock == Registry.hemp) {
      val world = event.getWorld
      val pos = event.getPos
      val hemp = Registry.hemp.asInstanceOf[Hemp]
      val result =
        if (hemp.getInt(world, pos, Hemp.STATE_AGE) <= Hemp.STATE_AGE_READY) {
          hemp.asInstanceOf[Hemp].grow(world, pos)
          Event.Result.ALLOW
        } else
          Event.Result.DENY

      event.setResult(result)
    }

  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent):Unit =
    if (eventArgs.getModID.equals(MOD_ID))
      config.sync

  @SubscribeEvent
  def onPlayerDeath(event: PlayerDropsEvent) {
    if (!event.getEntity.world.getGameRules.getBoolean("keepInventory")){
      InventoryCamping.dropItems(event.getEntityPlayer)
    } else {
      val tag = event.getEntity.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
      val store = event.getEntity.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)

      store.setTag(NBTInfo.INV_CAMPING, tag)
      event.getEntity.getEntityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, store)
    }
  }

  @SubscribeEvent
  def onPlayerRespawn(event: PlayerRespawnEvent):Unit = {
    val store = event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
    val tag = store.getCompoundTag(NBTInfo.INV_CAMPING)

    event.player.getEntityData.setTag(NBTInfo.INV_CAMPING, tag)
  }

  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent): Unit = {
    PacketSender.sendToPlayer(new NBTPlayer(
      event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
    ), event.player.asInstanceOf[EntityPlayerMP])

    InventoryCamping.refreshInventory(event.player)

    val map = InventoryCamping.getMap
    val theMap =
      if(map.isEmpty) None
      else Some(map)

    CampingMod.proxy.eventsS.mapChanged(theMap, event.player)
  }

  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent) {
    val crafting = event.crafting

    val marshmallow =
      crafting.isItemEqual(new ItemStack(Registry.parts, 1, Parts.MARSHMALLOW))

    for(slot <- 0 until event.craftMatrix.getSizeInventory)
      event.craftMatrix.getStackInSlot(slot) match {
        case stack if stack.getItem == Registry.knife =>
          stack.setCount(stack.getCount + 1)
          stack.damageItem(1, event.player)

        case stack if stack.getItem == Items.BOWL && marshmallow =>
          stack.setCount(stack.getCount + 1)

        case stack if stack.getItem == Items.POTIONITEM  && marshmallow =>
          event.craftMatrix.setInventorySlotContents(slot, new ItemStack(Items.GLASS_BOTTLE))

        case stack if stack.getItem == Registry.backpack =>
          crafting.setTagCompound(stack.getTagCompound)

        case _ =>
      }
  }

  def keyPressedServer(player: EntityPlayer, id: Int) {
    if(id==KeyInfo.INVENTORY_KEY)
      GuiHelper.openGui(GuiInfo.CAMPING, player)
  }

  def mapChanged(map: Option[ItemStack], player: EntityPlayer): Unit =
    if(!player.world.isRemote) {
      val packet =
        map.fold(
          new MapData(0, 0, 0, Array())
        )(map => {
          val data = InventoryCamping.getMapData(map, player.world)
          new MapData(data.scale, data.xCenter, data.zCenter, data.colors)
        })

      PacketSender.sendToPlayer(packet, player.asInstanceOf[EntityPlayerMP])
    }

  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent) {
    val player = event.player
    val world = player.world

    if (event.phase.equals(Phase.START)) {
      val trapTime = player.getEntityData.getInteger("isInTrap")

      if (trapTime > 0) {
        player.getEntityData.setInteger("isInTrap", trapTime - 1)
      } else if (trapTime == 0) {
        player.getEntityData.setInteger("isInTrap", -1)

        val speed = player.getEntityAttribute(MOVEMENT_SPEED)

        Option(speed.getModifier(TileTrap.UUIDSpeedTrap)).foreach(modifier =>
          speed.removeModifier(modifier)
        )
      }

      if (!world.isRemote) {
        InventoryCamping.refreshInventory(player)

        val lantern = InventoryCamping.getLantern

        if(!lantern.isEmpty) {
          tickLight += 1

          val pos = player.getPosition

          Vector(pos, pos.down, pos.up, pos.north, pos.south, pos.west, pos.east).find(pos => {
            if(world.getBlockState(pos).getBlock == Registry.light)
              true
            else if(world.isAirBlock(pos))
              world.setBlockState(pos, Registry.light.getDefaultState)
            else
              false
          })

          if (tickLight >= 20) {
            tickLight = 0

            InventoryCamping.lanternTick(player)
          }
        }
      }
    }

//    if (!world.isRemote) {
//      val oldMarsh = marshupdate
//
//      if(Option(player.inventory.getCurrentItem).isDefined){
//        val mob = Option(PlayerUtils.getMOP(player))
//        if (mob.isDefined) {
//          val x = mob.get.getBlockPos.getX
//          val y = mob.get.getBlockPos.getY
//          val z = mob.get.getBlockPos.getZ
//          val bd = (player.world, mob.get.getBlockPos)
//          val item = player.inventory.getCurrentItem
//
//          if(bd.tile.isInstanceOf[Roaster] && (new Vec3d(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(new Vec3d(player.posX, player.posY, player.posZ)) <= 2.5F)){
//            val roaster = bd.tile.asInstanceOf[Roaster]
//            if(roaster.canRoast(item)){
//              if(marshupdate > roaster.roastTime(item)){
//                val cooked = roaster.roast(player, item).get
//
//                player.inventory.getCurrentItem.setCount(player.inventory.getCurrentItem.getCount - 1)
//
//                if (player.inventory.getCurrentItem.getCount <= 0) player.setHeldItem(player.getActiveHand, ItemStack.EMPTY)
//                if (!player.inventory.addItemStackToInventory(cooked)) player.dropItem(cooked, false)
//                marshupdate = 0
//              } else marshupdate += roaster.roastSpeed(item)
//            }
//          }
//        }
//      }
//
//      if(marshupdate == oldMarsh)marshupdate = 0
//    }


    val increase = event.player.getArmorInventoryList.count(_.getItem match {
      case item: ItemArmor =>
        item.getArmorMaterial == Objs.fur
      case _ =>
        false
    })

    val speed = event.player.getEntityAttribute(MOVEMENT_SPEED)
    val modifier = Option(speed.getModifier(UUIDSpeedCamping))

    if(!modifier.exists(_.getAmount == 0.01 * increase)){
      modifier.foreach(mod => speed.removeModifier(mod))
      speed.applyModifier(new AttributeModifier(UUIDSpeedCamping, "camping.speedBoost", 0.01 * increase, 0))
    }
  }
}

@SideOnly(Side.CLIENT)
class EventsC {
  var map: Option[GuiMapHUD] =
    None

  var updateMap: Boolean =
    false

  var mapData: com.rikmuld.camping.inventory.gui.MapData =
    _

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent) {
    if (!FMLClientHandler.instance.isGUIOpen(classOf[GuiChat])) {
      for (key <- KeyInfo.default.indices) {
        if (Objs.keyOpenCamping.isPressed) {
          keyPressedClient(KeyInfo.INVENTORY_KEY)
          PacketSender.sendToServer(new KeyData(KeyInfo.INVENTORY_KEY))
        }
      }
    }
  }

  def keyPressedClient(id: Int): Unit =
    Unit

  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if(event.getGui.isInstanceOf[GuiInventory] && config.alwaysCampingInv){
      if(Minecraft.getMinecraft.player.capabilities.isCreativeMode) return;
      event.setCanceled(true)
      GuiHelper.forceOpenGui(GuiInfo.CAMPING, Minecraft.getMinecraft.player)
    }
  }

  @SubscribeEvent
  def onPlayerJoinWorld(event: PlayerLoggedInEvent){
    if(config.welcomeMess){
      event.player.sendMessage(new TextComponentString("The Camping Iventory overides the normal inventory. To change this, " +
                                                                 "just press the configuration tab in the Camping Invenory; search for: " +
                                                                 "'primary inventory option'."))

      config.disableMess()
    }
  }
  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent): Unit = {
    if (updateMap) {
      map =
        if (mapData.colors.isEmpty) None
        else Some(new GuiMapHUD(mapData))

      updateMap = false
    }

    map.foreach(map =>
      if (event.getType == ElementType.HOTBAR) {
        val mc = FMLClientHandler.instance().getClient

        InventoryCamping.refreshInventory(mc.player)

        if (!InventoryCamping.getMap.isEmpty)
          map.drawMap(mc, event.getResolution.getScaledWidth, event.getResolution.getScaledHeight)
      }
    )
  }
}