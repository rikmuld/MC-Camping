package rikmuld.camping.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.PlayerUtil;
import rikmuld.camping.item.ItemAnimalStuff;

public class EntityBear extends EntityAnimal {

	public EntityBear(World world)
	{
		super(world);
		setSize(1F, 1.125F);
		tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityZombie.class, 1.0D, false));
		tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, false));
		tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityCamper.class, 1.0D, false));
		tasks.addTask(4, new EntityAIWander(this, 1.0D));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		tasks.addTask(7, new EntityAISwimming(this));
		tasks.addTask(8, new EntityAITempt(this, 1.2D, Item.fishRaw.itemID, false));
		tasks.addTask(9, new EntityAIMate(this, 1.0D));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCamper.class, 0, false));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
		getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage).setAttribute(6.0D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
	}

	@Override
	protected void attackEntity(Entity par1Entity, float par2)
	{
		if((attackTime <= 0) && (par2 < 2.0F) && (par1Entity.boundingBox.maxY > boundingBox.minY) && (par1Entity.boundingBox.minY < boundingBox.maxY))
		{
			attackTime = 20;
			attackEntityAsMob(par1Entity);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if(par1Entity instanceof EntityLivingBase)
		{
			f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);
			i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);
		}

		boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if(flag)
		{
			if(i > 0)
			{
				par1Entity.addVelocity(-MathHelper.sin((rotationYaw * (float)Math.PI) / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos((rotationYaw * (float)Math.PI) / 180.0F) * i * 0.5F);
				motionX *= 0.6D;
				motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if(j > 0)
			{
				par1Entity.setFire(j * 4);
			}

			if(par1Entity instanceof EntityLivingBase)
			{
				EnchantmentThorns.func_92096_a(this, (EntityLivingBase)par1Entity, rand);
			}
		}

		return flag;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if(isEntityInvulnerable()) return false;
		else if(super.attackEntityFrom(par1DamageSource, par2))
		{
			Entity entity = par1DamageSource.getEntity();

			if((riddenByEntity != entity) && (ridingEntity != entity))
			{
				if(entity != this)
				{
					entityToAttack = entity;
				}

				return true;
			}
			else return true;
		}
		else return false;
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable)
	{
		return new EntityBear(worldObj);
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		int dropChance = rand.nextInt(3) + rand.nextInt(1 + par2);
		int drops;

		for(drops = 0; drops < (dropChance * 2); ++drops)
		{
			entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_BROWN), 0);
		}
		dropChance = rand.nextInt(5) + 1 + rand.nextInt(1 + par2);

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
	}

	@Override
	protected Entity findPlayerToAttack()
	{
		EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
		return (entityplayer != null) && canEntityBeSeen(entityplayer)? entityplayer:null;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		return (worldObj.difficultySetting > 0) && super.getCanSpawnHere();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	public int getTotalArmorValue()
	{
		return 8;
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return stack.itemID == Item.fishRaw.itemID;
	}

	@Override
	public void onDeath(DamageSource source)
	{
		if(source.getEntity() instanceof EntityPlayer)
		{
			NBTTagCompound tag = PlayerUtil.getPlayerDataTag((EntityPlayer)source.getEntity());
			tag.setInteger("killedBears", tag.getInteger("killedBears") + 1);
			if(tag.getInteger("killedBears") >= 5)
			{
				ModAchievements.bear.addStatToPlayer((EntityPlayer)source.getEntity());
			}
		}

		super.onDeath(source);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(!worldObj.isRemote && (worldObj.difficultySetting == 0))
		{
			setDead();
		}
	}

	// SOUNDS
}