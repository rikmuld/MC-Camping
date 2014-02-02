package rikmuld.camping.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.entity.ai.EntityAILookAtTradePlayerCamper;
import rikmuld.camping.entity.ai.EntityAITradePlayerCamper;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.item.ItemAnimalStuff;
import rikmuld.camping.item.ItemParts;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCamper extends EntityAgeable implements IMerchant, INpc {

	private boolean isMating;
	private boolean isPlaying;
	Random random = new Random();

	public int[] theCampfire = new int[]{0, 0, 0};
	public int updateHome = 0;

	private EntityPlayer buyingPlayer;

	private MerchantRecipeList buyingList;
	private int timeUntilReset;

	private boolean needsInitilization;
	private int wealth;

	private String lastBuyingPlayer;
	private float field_82191_bN;

	public static final Map buyList = new HashMap();
	public static final Map sellList = new HashMap();

	static
	{
		buyList.put(Integer.valueOf(ModItems.animalStuff.itemID), new Tuple(Integer.valueOf(-4), Integer.valueOf(1)));

		sellList.put(Integer.valueOf(ModItems.hareCooked.itemID), new Tuple(Integer.valueOf(-4), Integer.valueOf(1)));
		sellList.put(Integer.valueOf(ModItems.venisonCooked.itemID), new Tuple(Integer.valueOf(-4), Integer.valueOf(1)));
		sellList.put(Integer.valueOf(ModItems.knife.itemID), new Tuple(Integer.valueOf(1), Integer.valueOf(2)));
		sellList.put(Integer.valueOf(ModItems.backpack.itemID), new Tuple(Integer.valueOf(1), Integer.valueOf(2)));
		sellList.put(Integer.valueOf(ModItems.armorFurLeg.itemID), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		sellList.put(Integer.valueOf(ModItems.armorFurHelmet.itemID), new Tuple(Integer.valueOf(3), Integer.valueOf(5)));
		sellList.put(Integer.valueOf(ModItems.armorFurChest.itemID), new Tuple(Integer.valueOf(5), Integer.valueOf(8)));
		sellList.put(Integer.valueOf(ModItems.armorFurBoots.itemID), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		sellList.put(Integer.valueOf(ModItems.parts.itemID), new Tuple(Integer.valueOf(1), Integer.valueOf(2)));
	}

	public EntityCamper(World world)
	{
		super(world);
		setGender(random.nextInt(2));
		setSize(0.6F, 1.8F);
		getNavigator().setBreakDoors(true);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAIMoveTowardsRestriction(this, 1.0F));
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityBear.class, 1.0F, false));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0F, false));
		tasks.addTask(2, new EntityAITradePlayerCamper(this));
		tasks.addTask(2, new EntityAILookAtTradePlayerCamper(this));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityCamper.class, 5.0F, 0.02F));
		tasks.addTask(3, new EntityAIWander(this, 0.6D));
		targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityMob.class, 0, false));
		targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityBear.class, 0, false));
	}

	public EntityCamper(World world, int x, int y, int z)
	{
		this(world);
		setPosition(x, y, z);
		setHomeArea(x, y, z, 8);
	}

	public static void addBlacksmithItem(MerchantRecipeList merchantRecipeList, int id, int meta, Random par2Random, float par3)
	{
		if(par2Random.nextFloat() < par3)
		{
			int j = getRandomCountForBlacksmithItem(id, par2Random);
			ItemStack itemstack;
			ItemStack itemstack1;

			if(j < 0)
			{
				itemstack = new ItemStack(Item.emerald.itemID, 1, 0);
				itemstack1 = new ItemStack(id, -j, meta);
			}
			else
			{
				itemstack = new ItemStack(Item.emerald.itemID, j, 0);
				itemstack1 = new ItemStack(id, 1, meta);
			}

			merchantRecipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	public static void addMerchantItem(MerchantRecipeList merchantRecipeList, int id, int meta, Random par2Random, float par3)
	{
		if(par2Random.nextFloat() < par3)
		{
			merchantRecipeList.add(new MerchantRecipe(getRandomSizedStack(id, par2Random, meta), Item.emerald));
		}
	}

	private static int getRandomCountForBlacksmithItem(int par0, Random random)
	{
		Tuple tuple = (Tuple)sellList.get(Integer.valueOf(par0));
		return tuple == null? 1:(((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue()? ((Integer)tuple.getFirst()).intValue():((Integer)tuple.getFirst()).intValue() + random.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
	}

	private static int getRandomCountForItem(int par0, Random random)
	{
		Tuple tuple = (Tuple)buyList.get(Integer.valueOf(par0));
		return tuple == null? 1:(((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue()? ((Integer)tuple.getFirst()).intValue():((Integer)tuple.getFirst()).intValue() + random.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
	}

	private static ItemStack getRandomSizedStack(int id, Random random, int meta)
	{
		return new ItemStack(id, getRandomCountForItem(id, random), meta);
	}

	private void addDefaultEquipmentAndRecipies(int par1)
	{
		if(buyingList != null)
		{
			field_82191_bN = MathHelper.sqrt_float(buyingList.size()) * 0.2F;
		}
		else
		{
			field_82191_bN = 0.0F;
		}

		MerchantRecipeList merchantrecipelist;
		merchantrecipelist = new MerchantRecipeList();

		if(getGender() == 0)
		{
			addBlacksmithItem(merchantrecipelist, ModItems.armorFurBoots.itemID, 0, rand, adjustProbability(0.4F));
			addBlacksmithItem(merchantrecipelist, ModItems.armorFurChest.itemID, 0, rand, adjustProbability(0.3F));
			addBlacksmithItem(merchantrecipelist, ModItems.armorFurHelmet.itemID, 0, rand, adjustProbability(0.4F));
			addBlacksmithItem(merchantrecipelist, ModItems.armorFurLeg.itemID, 0, rand, adjustProbability(0.4F));
			addBlacksmithItem(merchantrecipelist, ModItems.knife.itemID, 0, rand, adjustProbability(0.6F));
			addBlacksmithItem(merchantrecipelist, ModItems.backpack.itemID, 0, rand, adjustProbability(0.5F));
		}
		else
		{
			addBlacksmithItem(merchantrecipelist, ModItems.venisonCooked.itemID, 0, rand, adjustProbability(0.6F));
			addBlacksmithItem(merchantrecipelist, ModItems.hareCooked.itemID, 0, rand, adjustProbability(0.6F));
			addBlacksmithItem(merchantrecipelist, ModItems.parts.itemID, ItemParts.PAN, rand, adjustProbability(0.6F));
			addMerchantItem(merchantrecipelist, ModItems.animalStuff.itemID, ItemAnimalStuff.ANTLER, rand, 0.25F);
		}

		if(merchantrecipelist.isEmpty())
		{
			addMerchantItem(merchantrecipelist, ModItems.animalStuff.itemID, ItemAnimalStuff.ANTLER, rand, 1.0F);
		}

		Collections.shuffle(merchantrecipelist);

		if(buyingList == null)
		{
			buyingList = new MerchantRecipeList();
		}

		for(int j1 = 0; (j1 < par1) && (j1 < merchantrecipelist.size()); ++j1)
		{
			buyingList.addToListWithCheck((MerchantRecipe)merchantrecipelist.get(j1));
		}
	}

	private float adjustProbability(float par1)
	{
		float f1 = par1 + field_82191_bN;
		return f1 > 0.9F? 0.9F - (f1 - 0.9F):f1;
	}

	@Override
	public boolean allowLeashing()
	{
		return false;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.4D);
		getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage).setAttribute(4.0D);
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
	public EntityAgeable createChild(EntityAgeable entityAgeable)
	{
		return new EntityCamper(worldObj);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}

	@Override
	public void func_110297_a_(ItemStack stack)
	{
		if(!worldObj.isRemote && (livingSoundTime > (-getTalkInterval() + 20)))
		{
			livingSoundTime = -getTalkInterval();

			if(stack != null)
			{
				playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
			}
			else
			{
				playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
			}
		}
	}

	public void func_82187_q()
	{}

	@SideOnly(Side.CLIENT)
	private void generateRandomParticles(String par1Str)
	{
		for(int i = 0; i < 5; ++i)
		{
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(par1Str, (posX + (rand.nextFloat() * width * 2.0F)) - width, posY + 1.0D + (rand.nextFloat() * height), (posZ + (rand.nextFloat() * width * 2.0F)) - width, d0, d1, d2);
		}
	}

	@Override
	public EntityPlayer getCustomer()
	{
		return buyingPlayer;
	}

	@Override
	protected String getDeathSound()
	{
		return "mob.villager.death";
	}

	public int getGender()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}

	@Override
	protected String getHurtSound()
	{
		return "mob.villager.hit";
	}

	@Override
	protected String getLivingSound()
	{
		return isTrading()? "mob.villager.haggle":"mob.villager.idle";
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player)
	{
		if(buyingList == null)
		{
			addDefaultEquipmentAndRecipies(1);
		}

		return buyingList;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte par1)
	{
		if(par1 == 12)
		{
			generateRandomParticles("heart");
		}
		else if(par1 == 13)
		{
			generateRandomParticles("angryVillager");
		}
		else if(par1 == 14)
		{
			generateRandomParticles("happyVillager");
		}
		else
		{
			super.handleHealthUpdate(par1);
		}
	}

	@Override
	public boolean interact(EntityPlayer player)
	{
		ModAchievements.encounterCamper.addStatToPlayer(player);

		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = (itemstack != null) && (itemstack.itemID == Item.monsterPlacer.itemID);

		if(!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking())
		{
			if(!worldObj.isRemote)
			{
				setCustomer(player);
				player.displayGUIMerchant(this, "Camper");
			}

			return true;
		}
		else return super.interact(player);
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	public boolean isMating()
	{
		return isMating;
	}

	public boolean isPlaying()
	{
		return isPlaying;
	}

	public boolean isTrading()
	{
		return buyingPlayer != null;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		setGender(tag.getInteger("gender"));
		wealth = tag.getInteger("Riches");
		theCampfire = tag.getIntArray("theCampfire");

		if(tag.hasKey("Offers"))
		{
			NBTTagCompound nbttagcompound1 = tag.getCompoundTag("Offers");
			buyingList = new MerchantRecipeList(nbttagcompound1);
		}
	}

	@Override
	public void setCustomer(EntityPlayer player)
	{
		buyingPlayer = player;
	}

	public void setGender(int par1)
	{
		dataWatcher.updateObject(16, Integer.valueOf(par1));
	}

	public void setMating(boolean par1)
	{
		isMating = par1;
	}

	public void setPlaying(boolean par1)
	{
		isPlaying = par1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRecipes(MerchantRecipeList merchantRecipeList)
	{}

	@Override
	protected void updateAITick()
	{
		if((theCampfire == null) || (theCampfire.length < 3))
		{
			theCampfire = new int[]{0, 0, 0};
		}

		if((updateHome-- <= 0) && (worldObj.getBlockId(theCampfire[0], theCampfire[1], theCampfire[2]) != ModBlocks.campfireBase.blockID))
		{
			setHomeArea(theCampfire[0], theCampfire[1], theCampfire[2], -1);

			Iterator tiles = worldObj.getChunkFromBlockCoords((int)posX, (int)posZ).chunkTileEntityMap.values().iterator();

			while(tiles.hasNext())
			{
				TileEntity tile = (TileEntity)tiles.next();

				if(tile instanceof TileEntityCampfireCook)
				{
					theCampfire = new int[]{tile.xCoord, tile.yCoord, tile.zCoord};
					setHomeArea(theCampfire[0], theCampfire[1], theCampfire[2], 8);
					break;
				}
			}
			updateHome = 100;
		}
		else if(updateHome == 20)
		{
			setHomeArea(theCampfire[0], theCampfire[1], theCampfire[2], 8);
		}

		if(!isTrading() && (timeUntilReset > 0))
		{
			--timeUntilReset;

			if(timeUntilReset <= 0)
			{
				if(needsInitilization)
				{
					if(buyingList.size() > 1)
					{
						Iterator iterator = buyingList.iterator();

						while(iterator.hasNext())
						{
							MerchantRecipe merchantrecipe = (MerchantRecipe)iterator.next();

							if(merchantrecipe.func_82784_g())
							{
								merchantrecipe.func_82783_a(rand.nextInt(6) + rand.nextInt(6) + 2);
							}
						}
					}

					addDefaultEquipmentAndRecipies(1);
					needsInitilization = false;
				}

				addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITick();
	}

	@Override
	public void useRecipe(MerchantRecipe merchantRecipe)
	{
		merchantRecipe.incrementToolUses();
		livingSoundTime = -getTalkInterval();
		playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());

		if(merchantRecipe.hasSameIDsAs((MerchantRecipe)buyingList.get(buyingList.size() - 1)))
		{
			timeUntilReset = 40;
			needsInitilization = true;

			if(buyingPlayer != null)
			{
				lastBuyingPlayer = buyingPlayer.getCommandSenderName();
			}
			else
			{
				lastBuyingPlayer = null;
			}
		}

		if(merchantRecipe.getItemToBuy().itemID == Item.emerald.itemID)
		{
			wealth += merchantRecipe.getItemToBuy().stackSize;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setInteger("gender", getGender());
		tag.setInteger("Riches", wealth);
		tag.setIntArray("theCampfire", theCampfire);

		if(buyingList != null)
		{
			tag.setCompoundTag("Offers", buyingList.getRecipiesAsTags());
		}
	}
}
