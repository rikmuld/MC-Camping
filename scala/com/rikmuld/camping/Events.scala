package com.rikmuld.camping

import java.util.{ArrayList, Random, UUID}

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.Utils._
import com.rikmuld.camping.inventory.camping.{GuiMapHUD, InventoryCamping}
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.block.{Hemp, Tent}
import com.rikmuld.camping.objs.misc.{KeyData, MapData, NBTPlayer, OpenGui}
import com.rikmuld.camping.objs.tile.{Roaster, TileTrap}
import com.rikmuld.corerm.RMMod
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{GuiButton, GuiChat, GuiScreen}
import net.minecraft.client.gui.inventory.{GuiContainer, GuiInventory}
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.{Item, ItemArmor, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.{BlockPos, Vec3d}
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.{GuiOpenEvent, RenderGameOverlayEvent}
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.{BonemealEvent, PlayerDropsEvent}
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.{Event, SubscribeEvent}
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.{ItemCraftedEvent, PlayerLoggedInEvent, PlayerRespawnEvent}
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

class EventsS {
  var tickLight: Int = 0
  var marshupdate: Float = 0
  val UUIDSpeedCamping = new UUID(new Random(83746763).nextLong, new Random(28647556).nextLong)

  @SubscribeEvent
  def onBoneMealUsed(event: BonemealEvent) {
    if (event.getBlock.getBlock == Objs.hemp) {
      event.setResult(
          if (event.getBlock.getBlock.asInstanceOf[Hemp].grow((event.getWorld, event.getPos))) 
            Event.Result.ALLOW 
          else Event.Result.DENY
          )
    }
  }
  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
    if (eventArgs.getModID.equals(MOD_ID)) config.sync
  }
  @SubscribeEvent
  def onPlayerDeath(event: PlayerDropsEvent) {
    if (!event.getEntity.world.getGameRules.getBoolean("keepInventory")){
      InventoryCamping.dropItems(event.getEntityPlayer)
    }
  }
  @SubscribeEvent
  def onEntityDeath(event: LivingDeathEvent) {
    if (event.getEntity.isInstanceOf[EntityPlayer]) {
      if (event.getEntity.world.getGameRules().getBoolean("keepInventory")) {
        val tag = event.getEntity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)
        if (!event.getEntity.asInstanceOf[EntityPlayer].getEntityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) event.getEntity.asInstanceOf[EntityPlayer].getEntityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound())
        event.getEntity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setTag(NBTInfo.INV_CAMPING, tag)
      }
    }
  }
  @SubscribeEvent
  def onPlayerRespawn(event: PlayerRespawnEvent) {
    if (event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey(NBTInfo.INV_CAMPING)) {
      event.player.getEntityData.setTag(NBTInfo.INV_CAMPING, event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(NBTInfo.INV_CAMPING))
      event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).removeTag(NBTInfo.INV_CAMPING)
    }
  }
  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent) = Option(event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)) map (tag => PacketSender.to(new NBTPlayer(tag), event.player.asInstanceOf[EntityPlayerMP]))
  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent) {
    if(event.crafting.getItem == Objs.backpack) {
      val data = event.craftMatrix.getStackInSlot(4).getTagCompound
      event.crafting.setTagCompound(data)
    }

    for (slot <- 0 until event.craftMatrix.getSizeInventory if Option(event.craftMatrix.getStackInSlot(slot)).isDefined) {
      val stackInSlot = event.craftMatrix.getStackInSlot(slot)
      val itemInSlot = Option(stackInSlot.getItem)
      if (itemInSlot.isDefined && itemInSlot.get.eq(Objs.knife)) {
        val returnStack = Option(stackInSlot.addDamage(1))
        returnStack map (stack => stack.setCount(stack.getCount + 1))
        event.craftMatrix.setInventorySlotContents(slot, returnStack.getOrElse(ItemStack.EMPTY))
      } else if (itemInSlot.isDefined && itemInSlot.get == Items.BOWL && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == Parts.MARSHMALLOW)) {
        val returnStack = new ItemStack(Items.BOWL, stackInSlot.getCount + 1, 0)
        event.craftMatrix.setInventorySlotContents(slot, returnStack)
      } else if (itemInSlot.isDefined && itemInSlot.get == Items.POTIONITEM && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == Parts.MARSHMALLOW)) {
        if (!event.player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) 
          event.player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false)
      }
      if ((event.crafting.getItem() == Item.getItemFromBlock(Objs.lantern)) && (event.crafting.getItemDamage == Lantern.ON)) {
        event.crafting.setTagCompound(new NBTTagCompound())
        event.crafting.getTagCompound.setInteger("time", 1500)
      }
    }
  }
  def keyPressedServer(player: EntityPlayer, id: Int) {
    if(id==KeyInfo.INVENTORY_KEY)player.openGui(RMMod, Objs.guiCamping.get.get, player.world, 0, 0, 0)
  }
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent) {
    val player = event.player
    val world = player.world
    Objs.tent.asInstanceOf[Tent].facingFlag = player.facing.getHorizontalIndex
    
    if (event.phase.equals(Phase.START)) {
      if (player.getEntityData().getInteger("isInTrap") <= 0) {
        player.getEntityData().setInteger("isInTrap", player.getEntityData().getInteger("isInTrap") - 1)
      } else if (Option(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap)).isDefined)
        player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap))
    }
    if (!world.isRemote && player.hasLantarn()) {
      tickLight += 1
      if (tickLight >= 20) {
        tickLight = 0
        player.lanternTick()
        val bd = (player.world, new BlockPos(player.posX.toInt, player.posY.toInt, player.posZ.toInt))
        if (bd.down.block == Blocks.AIR) bd.down.setState(Objs.light.getDefaultState)
        else if (bd.block == Blocks.AIR) bd.setState(Objs.light.getDefaultState)
        else if (bd.up.block == Blocks.AIR) bd.up.setState(Objs.light.getDefaultState)
      }
    }
    if (!world.isRemote && player.hasMap()) {
      val data = player.getCurrentMapData()
      PacketSender.to(new MapData(data.scale, data.xCenter, data.zCenter, data.colors), player.asInstanceOf[EntityPlayerMP])
    }

    if (!world.isRemote) {
      val oldMarsh = marshupdate
      
      if(Option(player.inventory.getCurrentItem).isDefined){
        val mob = Option(player.getMOP)
        if (mob.isDefined) {
          val x = mob.get.getBlockPos.getX
          val y = mob.get.getBlockPos.getY
          val z = mob.get.getBlockPos.getZ
          val bd = (player.world, mob.get.getBlockPos)
          val item = player.inventory.getCurrentItem
          
          if(bd.tile.isInstanceOf[Roaster] && (new Vec3d(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(new Vec3d(player.posX, player.posY, player.posZ)) <= 2.5F)){
            val roaster = bd.tile.asInstanceOf[Roaster]
            if(roaster.canRoast(item)){
              if(marshupdate > roaster.roastTime(item)){
                player.inventory.getCurrentItem.setCount(player.inventory.getCurrentItem.getCount - 1)
                if (player.inventory.getCurrentItem.getCount <= 0) player.setHeldItem(player.getActiveHand, ItemStack.EMPTY)
                
                val cooked = roaster.roast(player, item).get
                
                if (!player.inventory.addItemStackToInventory(cooked)) player.dropItem(cooked, false)
                marshupdate = 0
              } else marshupdate += roaster.roastSpeed(item)
            } 
          } 
        } 
      }
      
      if(marshupdate == oldMarsh)marshupdate = 0
    }
        
    var campNum = 0.0f
    for (i <- 0 until 4 if (player.inventory.armorInventory(i) != null && player.inventory.armorInventory(i).getItem.isInstanceOf[ItemArmor] && player.inventory.armorInventory(i).getItem.asInstanceOf[ItemArmor].getArmorMaterial.equals(Objs.fur))) campNum += 0.25f
    if (player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(UUIDSpeedCamping) != null) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(UUIDSpeedCamping))
    player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(UUIDSpeedCamping, "camping.speedBoost", 0.04 * campNum, 0))
  }
}

@SideOnly(Side.CLIENT)
class EventsC {
  var map: GuiMapHUD = _

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent) {
    if (!FMLClientHandler.instance.isGUIOpen(classOf[GuiChat])) {
      for (key <- 0 until KeyInfo.default.length) {
        if (Objs.keyOpenCamping.isPressed) {
          val id = KeyInfo.default.indexOf(KeyInfo.default.find { default => default == Objs.keyOpenCamping.getKeyCodeDefault }.get)
          keyPressedClient(id)
          PacketSender.toServer(new KeyData(id))
        }
      }
    }
  }
  def keyPressedClient(id: Int) {
    val player = Minecraft.getMinecraft.player
  }
  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if(event.getGui.isInstanceOf[GuiInventory]&&config.alwaysCampingInv){
      if(Minecraft.getMinecraft.player.capabilities.isCreativeMode) return;
      event.setCanceled(true)
      PacketSender.toServer(new OpenGui(Objs.guiCamping.get.get, 0, 0, 0))
    }
  }

  @SubscribeEvent
  def onPlayerJoinWorld(event: PlayerLoggedInEvent){
    if(config.welcomeMess){
      event.player.sendMessage(new TextComponentString("The Camping Iventory overides the normal inventory. To change this, " +
                                                                 "just press the configuration tab in the Camping Invenory; search for: " +
                                                                 "'primary inventory option'."))

      config.disableMess
    }
  }
  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent) {
    if (event.getType != ElementType.HOTBAR) return
    val mc = FMLClientHandler.instance().getClient
    if (map == null) map = new GuiMapHUD()
    if (mc.player.hasMap) {
      map.setWorldAndResolution(mc, event.getResolution.getScaledWidth, event.getResolution.getScaledHeight)
      map.drawScreen(0, 0, event.getPartialTicks)
    }
  }
}