package rikmuld.camping.entity;

import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.PlayerUtil;
import rikmuld.camping.item.ItemAnimalStuff;

public class EntityHare extends EntityAnimal {

	Random random = new Random();

	public EntityHare(World world)
	{
		super(world);
		setSize(0.4F, 0.4F);
		tasks.addTask(1, new EntityAIWander(this, 1.0D));
		tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(4, new EntityAISwimming(this));
		tasks.addTask(5, new EntityAIPanic(this, 1.2D));
		tasks.addTask(6, new EntityAIFollowParent(this, 1.0D));
		tasks.addTask(7, new EntityAITempt(this, 1.2D, Item.carrot.itemID, true));
		tasks.addTask(8, new EntityAITempt(this, 1.2D, Item.carrotOnAStick.itemID, true));
		tasks.addTask(9, new EntityAIMate(this, 1.0D));

		setType(random.nextInt(2));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(3.D);
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable)
	{
		return new EntityHare(worldObj);
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		int dropChance = rand.nextInt(3) + rand.nextInt(1 + par2);
		int drops;

		for(drops = 0; drops < dropChance; ++drops)
		{
			if(getType() == 0)
			{
				entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_WHITE), 0);
			}
			else
			{
				entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_BROWN), 0);
			}
		}

		dropChance = -2 + rand.nextInt(5) + rand.nextInt(1 + par2);
		if(dropChance > 1)
		{
			dropChance = 1;
		}

		for(drops = 0; drops < dropChance; ++drops)
		{
			if(isBurning())
			{
				entityDropItem(new ItemStack(ModItems.hareCooked.itemID, 1, 0), 0);
			}
			else
			{
				entityDropItem(new ItemStack(ModItems.hareRaw.itemID, 1, 0), 0);
			}
		}
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(30, Integer.valueOf(0));
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	public int getTotalArmorValue()
	{
		return 0;
	}

	public int getType()
	{
		return dataWatcher.getWatchableObjectInt(30);
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return (stack.itemID == Item.carrot.itemID) || (stack.itemID == Item.carrotOnAStick.itemID);
	}

	@Override
	public void onDeath(DamageSource source)
	{
		if(source.getEntity() instanceof EntityPlayer)
		{
			NBTTagCompound tag = PlayerUtil.getPlayerDataTag((EntityPlayer)source.getEntity());
			tag.setInteger("killedHeers", tag.getInteger("killedHeers") + 1);
			if(tag.getInteger("killedHeers") >= 15)
			{
				ModAchievements.hare.addStatToPlayer((EntityPlayer)source.getEntity());
			}
		}
		
		super.onDeath(source);
	}

	// TODO: SOUNDS

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		setType(tag.getInteger("type"));
	}

	public void setType(int type)
	{
		dataWatcher.updateObject(30, Integer.valueOf(type));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setInteger("type", getType());
	}
}