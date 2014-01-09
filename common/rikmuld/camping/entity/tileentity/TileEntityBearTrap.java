package rikmuld.camping.entity.tileentity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModDamageSources;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModPotions;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketItems;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileEntityBearTrap extends TileEntityInventory{

	public EntityLivingBase trappedEntity;
	public boolean open = true;
	Random random = new Random();
	public int cooldown;
	public boolean captureFlag;
	
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{					
			if(trappedEntity!=null)
			{
				List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord+0.21875F, this.yCoord, this.zCoord+0.21875, this.xCoord+0.78125, this.yCoord+0.1875, this.zCoord+0.78125));
				if(!entities.contains(this.trappedEntity))this.trappedEntity = null;
			}

			if(cooldown>0) this.cooldown = 0;
			
			if(this.trappedEntity!=null&&open)
			{
				this.open = false;	
				this.sendTileData(0, true, open? 1:0);
			}
			if((this.open||this.captureFlag)&&this.cooldown<=0)
			{
				this.captureFlag = false;

				List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord+0.21875F, this.yCoord, this.zCoord+0.21875, this.xCoord+0.78125, this.yCoord+0.1875, this.zCoord+0.78125));
				if(entities.size()>0)
				{
					if(entities.get(0)instanceof EntityPlayer&&ConfigInfoBoolean.value(ConfigInfo.ENBLED_PLAYER_TRAP))this.trappedEntity = entities.get(0);
					else if(!(entities.get(0)instanceof EntityPlayer))
					{
						this.trappedEntity = entities.get(0);
						
						if(this.getStackInSlot(0)!=null)this.getStackInSlot(0).stackSize--;
						if(this.getStackInSlot(0)!=null&&this.trappedEntity instanceof EntityAnimal&&((EntityAnimal)this.trappedEntity).isBreedingItem(this.getStackInSlot(0)))
						{
							ModAchievements.trapBait.addStatToPlayer(worldObj.getClosestPlayer(xCoord, yCoord, zCoord, -1));
						}
						else ModAchievements.trapLuckey.addStatToPlayer(worldObj.getClosestPlayer(xCoord, yCoord, zCoord, -1));
					}
				}
				if(this.trappedEntity!=null)
				{
					this.open = false;
					this.sendTileData(0, true, open? 1:0);
				}
			}
			if(this.trappedEntity!=null)
			{
				if(!(trappedEntity instanceof EntityPlayer))trappedEntity.setPositionAndUpdate(this.xCoord+0.5F, this.yCoord, this.zCoord+0.5F);
				trappedEntity.setInWeb();
				if(trappedEntity instanceof EntityPlayer)((EntityPlayer)trappedEntity).capabilities.setPlayerWalkSpeed(0.001F);
				if(random.nextInt(50)==0)
				{
					if(!(trappedEntity instanceof EntityPlayer)||!((EntityPlayer)trappedEntity).capabilities.isCreativeMode)trappedEntity.attackEntityFrom(ModDamageSources.bearTrap, trappedEntity.getMaxHealth()/20F);
				
					PotionEffect effect = new PotionEffect(ModPotions.bleeding.id, 200, 0);
					effect.getCurativeItems().clear();
					trappedEntity.addPotionEffect(effect);
				}
				if(trappedEntity.isDead)this.trappedEntity = null;
			}
			if(this.getStackInSlot(0)!=null&&this.trappedEntity==null&&open)
			{				
				List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord-20, this.yCoord-10, this.zCoord-20, this.xCoord+20, this.yCoord+10, this.zCoord+20));
				for(EntityLivingBase entity:entities)
				{
					if(entity instanceof EntityAnimal)
					{
						if(((EntityAnimal)entity).isBreedingItem(this.getStackInSlot(0)))
						{
							((EntityAnimal) entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						}
					}
					if(entity instanceof EntityZombie)
					{
						if(this.getStackInSlot(0).itemID==Item.rottenFlesh.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						if(this.getStackInSlot(0).itemID==Item.beefRaw.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						if(this.getStackInSlot(0).itemID==Item.chickenRaw.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						if(this.getStackInSlot(0).itemID==Item.porkRaw.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						if(this.getStackInSlot(0).itemID==ModItems.venisonRaw.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
						if(this.getStackInSlot(0).itemID==ModItems.hareRaw.itemID)((EntityZombie)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
					}
					if(entity instanceof EntityCreeper)
					{
						if(this.getStackInSlot(0).itemID==Item.gunpowder.itemID)((EntityCreeper)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
					}
					if(entity instanceof EntitySkeleton)
					{
						if(this.getStackInSlot(0).itemID==Item.bone.itemID)((EntitySkeleton)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
					}
					if(entity instanceof EntityEnderman)
					{
						if(this.getStackInSlot(0).itemID==Item.enderPearl.itemID)((EntityEnderman)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
					}
					if(entity instanceof EntitySpider)
					{
						if(this.getStackInSlot(0).itemID==Item.spiderEye.itemID)((EntitySpider)entity).getMoveHelper().setMoveTo(xCoord+0.5F, yCoord, zCoord+0.5F, 1);
					}
				}
			}
		}
	}
	
	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		if(!worldObj.isRemote)PacketDispatcher.sendPacketToAllPlayers(PacketTypeHandler.populatePacket(new PacketItems(0, xCoord, yCoord, zCoord, this.getStackInSlot(0))));
	}

	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)this.open = data[0]==1;
	}
	
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		cooldown = tag.getInteger("cooldown");
		open = tag.getBoolean("open");
		captureFlag = tag.getBoolean("captureFlag");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("cooldown", cooldown);
		tag.setBoolean("open", open);
		tag.setBoolean("captureFlag", this.trappedEntity!=null);
	}
}
