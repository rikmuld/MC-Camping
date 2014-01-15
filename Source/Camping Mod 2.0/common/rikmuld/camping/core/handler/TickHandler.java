package rikmuld.camping.core.handler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import rikmuld.camping.CampingMod;
import rikmuld.camping.client.gui.container.GuiContainerPlayerInv;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.CampingInvUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.network.packets.PacketMap;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;

public class TickHandler implements ITickHandler {

	private int tickLight;
	private int marshupdate = 0;
	private boolean sync = false;
	public final float playerWalkSpeed = 0.1F;
	public float playerWalkSpeedAmplifier = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if(type.equals(EnumSet.of(TickType.PLAYER)))
		{			
			EntityPlayer player = (EntityPlayer) tickData[0];
			World world = player.worldObj;
				
			if(!world.isRemote)
			{
				for(ItemStack stack:player.inventory.mainInventory)
				{
					if(stack!=null)
					{
						if(stack.isItemEqual(new ItemStack(ModItems.hemp)))ModAchievements.hemp.addStatToPlayer(player);
					}
				}
			}
			
			if(world.isRemote&&Minecraft.getMinecraft().currentScreen!=null&&Minecraft.getMinecraft().currentScreen instanceof GuiInventory&&!(Minecraft.getMinecraft().currentScreen instanceof GuiContainerPlayerInv))
			{
				player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, world, 0, 0, 0);
			}
				
			if(!world.isRemote&&CampingInvUtil.hasLantarn(player))
			{
				tickLight++;
				if(tickLight>=10)
				{
					tickLight = 0;
					CampingInvUtil.lanternTick(player);
					if(player.worldObj.getBlockId((int)player.posX, (int)player.posY-1, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY-1, (int)player.posZ, ModBlocks.light.blockID);
					else if(player.worldObj.getBlockId((int)player.posX, (int)player.posY, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY, (int)player.posZ, ModBlocks.light.blockID);
					else if(player.worldObj.getBlockId((int)player.posX, (int)player.posY+1, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY+1, (int)player.posZ, ModBlocks.light.blockID);
				}
			}
			
			if(!world.isRemote&&CampingInvUtil.hasMap(player))
			{				
				MapData data = CampingInvUtil.getMapData(player);
				PacketUtil.sendToPlayer(new PacketMap(data.scale, data.xCenter, data.zCenter, data.colors), (Player) player);
			}
			
			if(!world.isRemote&&player.getCurrentEquippedItem()!=null&&player.getCurrentEquippedItem().itemID == ModItems.parts.itemID&&player.getCurrentEquippedItem().getItemDamage()==ItemParts.MARSHMALLOW_STICK)
			{		
				MovingObjectPosition movingobjectposition = ((ItemParts)player.getCurrentEquippedItem().getItem()).getMovingObjectPositionFromPlayer(world, player, true);
	             
				if(movingobjectposition!=null)
				{
		            int x = movingobjectposition.blockX;
		            int y = movingobjectposition.blockY;
		            int z = movingobjectposition.blockZ;
		            
		            if(world.getBlockId(x, y, z)==ModBlocks.campfireBase.blockID&&Vec3.createVectorHelper(x+0.5F, y+0.5F, z+0.5F).distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))<=2.5F)
		            {
		            	if(marshupdate > 80)
		            	{
		            		player.getCurrentEquippedItem().stackSize--;
		            		if(player.getCurrentEquippedItem().stackSize<=0)ItemStackUtil.setCurrentPlayerItem(player, null);
		            		if(!player.inventory.addItemStackToInventory(new ItemStack(ModItems.marshmallowCooked)))
		            		{
		            			player.dropPlayerItem(new ItemStack(ModItems.marshmallowCooked));
		            		}
		            		marshupdate = 0;
		            	}
		            	
		            	TileEntityCampfireCook tile = (TileEntityCampfireCook) world.getBlockTileEntity(x, y, z);
		            	if(tile.fuel>0) marshupdate++;
		            	else marshupdate = 0;
		            }
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if(type.equals(EnumSet.of(TickType.PLAYER)))
		{			
			EntityPlayer player = (EntityPlayer) tickData[0];
			World world = player.worldObj;
			
			if(!world.isRemote)
			{				
				if(player.getCurrentItemOrArmor(4)!=null&&player.getCurrentItemOrArmor(4).itemID==ModItems.armorFurHelmet.itemID)this.playerWalkSpeedAmplifier+=0.00625F;
				if(player.getCurrentItemOrArmor(3)!=null&&player.getCurrentItemOrArmor(3).itemID==ModItems.armorFurChest.itemID)this.playerWalkSpeedAmplifier+=0.00625F;
				if(player.getCurrentItemOrArmor(2)!=null&&player.getCurrentItemOrArmor(2).itemID==ModItems.armorFurLeg.itemID)this.playerWalkSpeedAmplifier+=0.00625F;
				if(player.getCurrentItemOrArmor(1)!=null&&player.getCurrentItemOrArmor(1).itemID==ModItems.armorFurBoots.itemID)this.playerWalkSpeedAmplifier+=0.00625F;
				
				if(playerWalkSpeedAmplifier>=(0.00625F*4F))ModAchievements.armor.addStatToPlayer(player);
				
				if(ConfigInfoBoolean.value(ConfigInfo.ENBLED_SPEEDUP))player.capabilities.setPlayerWalkSpeed(this.playerWalkSpeed+this.playerWalkSpeedAmplifier);
				playerWalkSpeedAmplifier = 0;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel()
	{
		return ModInfo.MOD_ID+": "+this.getClass().getSimpleName();
	}
}
