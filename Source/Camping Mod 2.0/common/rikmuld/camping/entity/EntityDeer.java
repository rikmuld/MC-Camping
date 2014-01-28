package rikmuld.camping.entity;

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

public class EntityDeer extends EntityAnimal {

	public EntityDeer(World world)
	{
		super(world);
		setSize(1F, 1.5F);
		tasks.addTask(1, new EntityAIWander(this, 1.0D));
		tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(4, new EntityAISwimming(this));
		tasks.addTask(5, new EntityAIPanic(this, 1.2D));
		tasks.addTask(6, new EntityAIFollowParent(this, 1.0D));
		tasks.addTask(7, new EntityAITempt(this, 1.2D, Item.wheat.itemID, true));
		tasks.addTask(8, new EntityAIMate(this, 1.0D));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.4D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(15.0D);
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable)
	{
		return new EntityDeer(worldObj);
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		int dropChance = rand.nextInt(3) + rand.nextInt(1 + par2);
		int drops;

		for(drops = 0; drops < dropChance; ++drops)
		{
			entityDropItem(new ItemStack(Item.leather.itemID, 1, 0), 0);
		}
		dropChance = rand.nextInt(3) + 1 + rand.nextInt(1 + par2);

		for(drops = 0; drops < dropChance; ++drops)
		{
			if(isBurning())
			{
				entityDropItem(new ItemStack(ModItems.venisonCooked.itemID, 1, 0), 0);
			}
			else
			{
				entityDropItem(new ItemStack(ModItems.venisonRaw.itemID, 1, 0), 0);
			}
		}

		dropChance = (rand.nextInt(20) - 18) + rand.nextInt(1 + par2);
		if(dropChance > 2)
		{
			dropChance = 2;
		}

		for(drops = 0; drops < dropChance; ++drops)
		{
			entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.ANTLER), 0);
		}
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	public int getTotalArmorValue()
	{
		return 5;
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	public void onDeath(DamageSource source)
	{
		if(source.getEntity() instanceof EntityPlayer)
		{
			NBTTagCompound tag = PlayerUtil.getPlayerDataTag((EntityPlayer)source.getEntity());
			tag.setInteger("killedDeers", tag.getInteger("killedDeers") + 1);
			if(tag.getInteger("killedDeers") >= 10)
			{
				ModAchievements.deer.addStatToPlayer((EntityPlayer)source.getEntity());
			}
		}
		
		super.onDeath(source);
	}
}