package rikmuld.camping.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.entity.ai.EntityAILookAtTradePlayerCamper;
import rikmuld.camping.entity.ai.EntityAITradePlayerCamper;
import rikmuld.camping.item.ItemAnimalStuff;
import rikmuld.camping.item.ItemParts;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCamper extends EntityAgeable implements IMerchant, INpc
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Random random = new Random();
    public ChunkCoordinates chunkcoordinates;
    
    private EntityPlayer buyingPlayer;

    private MerchantRecipeList buyingList;
    private int timeUntilReset;

    private boolean needsInitilization;
    private int wealth;

    private String lastBuyingPlayer;
    private boolean field_82190_bM;
    private float field_82191_bN;

    public static final Map buyList = new HashMap();
    public static final Map sellList = new HashMap();

    public EntityCamper(World world)
    {
        super(world);
        this.setGender(random.nextInt(2));
        this.setSize(0.6F, 1.8F);
        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityBear.class, 1.0F, false));
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0F, false));
        this.tasks.addTask(1, new EntityAITradePlayerCamper(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayerCamper(this));
        this.tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(2, new EntityAIWatchClosest2(this, EntityCamper.class, 5.0F, 0.02F));
        this.tasks.addTask(2, new EntityAIWander(this, 0.6D));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityMob.class, 0, false));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityBear.class, 0, false));
    }
    
    public EntityCamper(World world, int x, int y, int z)
    {
    	this(world);
    	this.setPosition(x, y, z);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.4D);
        this.getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage).setAttribute(4.0D);
    }

    public boolean isAIEnabled()
    {
        return true;
    }

    protected void updateAITick()
    {
    	if(chunkcoordinates==null)
    	{
    		this.chunkcoordinates = new ChunkCoordinates(this.chunkCoordX, this.chunkCoordY, this.chunkCoordZ);
    	}
    	
        if (--this.randomTickDivider <= 0)
        {
        	this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 16);
        }

        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    if (this.buyingList.size() > 1)
                    {
                        Iterator iterator = this.buyingList.iterator();

                        while (iterator.hasNext())
                        {
                            MerchantRecipe merchantrecipe = (MerchantRecipe)iterator.next();

                            if (merchantrecipe.func_82784_g())
                            {
                                merchantrecipe.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                            }
                        }
                    }

                    this.addDefaultEquipmentAndRecipies(1);
                    this.needsInitilization = false;
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }

        super.updateAITick();
    }

    public boolean interact(EntityPlayer player)
    {
		ModAchievements.encounterCamper.addStatToPlayer(player);

        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.itemID == Item.monsterPlacer.itemID;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
        {
            if (!this.worldObj.isRemote)
            {
                this.setCustomer(player);
                player.displayGUIMerchant(this, "Camper");
            }

            return true;
        }
        else
        {
            return super.interact(player);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
    }

    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setInteger("gender", this.getGender());
        tag.setInteger("Riches", this.wealth);     
        
		tag.setInteger("cunkX", this.chunkcoordinates.posX);
	    tag.setInteger("cunkY", this.chunkcoordinates.posY);
	    tag.setInteger("cunkZ", this.chunkcoordinates.posZ);
           
        if (this.buyingList != null)
        {
            tag.setCompoundTag("Offers", this.buyingList.getRecipiesAsTags());
        }
    }

    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        this.setGender(tag.getInteger("gender"));
        this.wealth = tag.getInteger("Riches");

        this.chunkcoordinates = new ChunkCoordinates(tag.getInteger("chunkX"), tag.getInteger("chunkY"), tag.getInteger("chunkZ"));
        
        if (tag.hasKey("Offers"))
        {
            NBTTagCompound nbttagcompound1 = tag.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound1);
        }
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected String getLivingSound()
    {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    protected String getHurtSound()
    {
        return "mob.villager.hit";
    }

    protected String getDeathSound()
    {
        return "mob.villager.death";
    }

    public void setGender(int par1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(par1));
    }

    public int getGender()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean par1)
    {
        this.isMating = par1;
    }

    public void setPlaying(boolean par1)
    {
        this.isPlaying = par1;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public void setCustomer(EntityPlayer player)
    {
        this.buyingPlayer = player;
    }

    public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    public void useRecipe(MerchantRecipe merchantRecipe)
    {
        merchantRecipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());

        if (merchantRecipe.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1)))
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }
        }

        if (merchantRecipe.getItemToBuy().itemID == Item.emerald.itemID)
        {
            this.wealth += merchantRecipe.getItemToBuy().stackSize;
        }
    }

    public void func_110297_a_(ItemStack stack)
    {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();

            if (stack != null)
            {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            }
            else
            {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        if (this.buyingList == null)
        {
            this.addDefaultEquipmentAndRecipies(1);
        }

        return this.buyingList;
    }

    private float adjustProbability(float par1)
    {
        float f1 = par1 + this.field_82191_bN;
        return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
    }

    private void addDefaultEquipmentAndRecipies(int par1)
    {
        if (this.buyingList != null)
        {
            this.field_82191_bN = MathHelper.sqrt_float((float)this.buyingList.size()) * 0.2F;
        }
        else
        {
            this.field_82191_bN = 0.0F;
        }

        MerchantRecipeList merchantrecipelist;
        merchantrecipelist = new MerchantRecipeList();

        int j;
        
        if(this.getGender()==0)
        {
    		addBlacksmithItem(merchantrecipelist, ModItems.armorFurBoots.itemID, 0, this.rand, this.adjustProbability(0.4F));
    		addBlacksmithItem(merchantrecipelist, ModItems.armorFurChest.itemID, 0, this.rand, this.adjustProbability(0.3F));
    		addBlacksmithItem(merchantrecipelist, ModItems.armorFurHelmet.itemID, 0, this.rand, this.adjustProbability(0.4F));
    		addBlacksmithItem(merchantrecipelist, ModItems.armorFurLeg.itemID, 0, this.rand, this.adjustProbability(0.4F));
            addBlacksmithItem(merchantrecipelist, ModItems.knife.itemID, 0, this.rand, this.adjustProbability(0.6F));
    		addBlacksmithItem(merchantrecipelist, ModItems.backpack.itemID, 0, this.rand, this.adjustProbability(0.5F));
        }
        else
        {
    		addBlacksmithItem(merchantrecipelist, ModItems.venisonCooked.itemID, 0, this.rand, this.adjustProbability(0.6F));
    		addBlacksmithItem(merchantrecipelist, ModItems.hareCooked.itemID, 0, this.rand, this.adjustProbability(0.6F));
    		addBlacksmithItem(merchantrecipelist, ModItems.parts.itemID, ItemParts.PAN, this.rand, this.adjustProbability(0.6F));
        	addMerchantItem(merchantrecipelist, ModItems.animalStuff.itemID, ItemAnimalStuff.ANTLER, this.rand, 0.25F);
        }

        if (merchantrecipelist.isEmpty())
        {
        	addMerchantItem(merchantrecipelist, ModItems.animalStuff.itemID, ItemAnimalStuff.ANTLER, this.rand, 1.0F);
        }

        Collections.shuffle(merchantrecipelist);

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        for (int j1 = 0; j1 < par1 && j1 < merchantrecipelist.size(); ++j1)
        {
            this.buyingList.addToListWithCheck((MerchantRecipe)merchantrecipelist.get(j1));
        }
    }

    @SideOnly(Side.CLIENT)
    public void setRecipes(MerchantRecipeList merchantRecipeList) {}

    public static void addMerchantItem(MerchantRecipeList merchantRecipeList, int id, int meta, Random par2Random, float par3)
    {
        if (par2Random.nextFloat() < par3)
        {
            merchantRecipeList.add(new MerchantRecipe(getRandomSizedStack(id, par2Random, meta), Item.emerald));
        }
    }

    private static ItemStack getRandomSizedStack(int id, Random random, int meta)
    {
        return new ItemStack(id, getRandomCountForItem(id, random), meta);
    }

    private static int getRandomCountForItem(int par0, Random random)
    {
        Tuple tuple = (Tuple)buyList.get(Integer.valueOf(par0));
        return tuple == null ? 1 : (((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue() ? ((Integer)tuple.getFirst()).intValue() : ((Integer)tuple.getFirst()).intValue() + random.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
    }

    public static void addBlacksmithItem(MerchantRecipeList merchantRecipeList, int id, int meta, Random par2Random, float par3)
    {
        if (par2Random.nextFloat() < par3)
        {
            int j = getRandomCountForBlacksmithItem(id, par2Random);
            ItemStack itemstack;
            ItemStack itemstack1;

            if (j < 0)
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

    private static int getRandomCountForBlacksmithItem(int par0, Random random)
    {
        Tuple tuple = (Tuple)sellList.get(Integer.valueOf(par0));
        return tuple == null ? 1 : (((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue() ? ((Integer)tuple.getFirst()).intValue() : ((Integer)tuple.getFirst()).intValue() + random.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 12)
        {
            this.generateRandomParticles("heart");
        }
        else if (par1 == 13)
        {
            this.generateRandomParticles("angryVillager");
        }
        else if (par1 == 14)
        {
            this.generateRandomParticles("happyVillager");
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    @SideOnly(Side.CLIENT)
    private void generateRandomParticles(String par1Str)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(par1Str, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }

    public void func_82187_q()
    {
        this.field_82190_bM = true;
    }

    public boolean allowLeashing()
    {
        return false;
    }

    public EntityAgeable createChild(EntityAgeable entityAgeable)
    {
        return new EntityCamper(worldObj);
    }
    
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (super.attackEntityFrom(par1DamageSource, par2))
        {
            Entity entity = par1DamageSource.getEntity();

            if (this.riddenByEntity != entity && this.ridingEntity != entity)
            {
                if (entity != this)
                {
                    this.entityToAttack = entity;
                }

                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;

        if (par1Entity instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);
            i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);
        }

        boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                par1Entity.setFire(j * 4);
            }

            if (par1Entity instanceof EntityLivingBase)
            {
                EnchantmentThorns.func_92096_a(this, (EntityLivingBase)par1Entity, this.rand);
            }
        }

        return flag;
    }

    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(par1Entity);
        }
    }

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
}
