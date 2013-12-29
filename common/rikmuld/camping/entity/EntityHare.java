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
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemAnimalStuff;

public class EntityHare extends EntityAnimal{

	Random random = new Random(); 

	public EntityHare(World world) 
	{
		super(world);
        this.setSize(0.4F, 0.4F);
        this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAISwimming(this)); 
        this.tasks.addTask(5, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(6, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(7, new EntityAITempt(this, 1.2D, Item.carrot.itemID, true));
        this.tasks.addTask(8, new EntityAITempt(this, 1.2D, Item.carrotOnAStick.itemID, true));
        this.tasks.addTask(9, new EntityAIMate(this, 1.0D));

        this.setType(random.nextInt(2));
  	}
	
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(3.D);
    }
    
    public boolean isAIEnabled()
    {
        return true;
    } 
    
    public int getTotalArmorValue()
    {
        return 0;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }
    
    protected boolean canDespawn()
    {
        return false;
    }

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) 
	{
		return new EntityHare(worldObj);
	}
	
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(30, Integer.valueOf(0));
	}

	public void setType(int type)
	{
		this.dataWatcher.updateObject(30, Integer.valueOf(type));
	}

	public int getType()
	{
		return this.dataWatcher.getWatchableObjectInt(30);
	}
	
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setInteger("type", this.getType());
	}

	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		this.setType(tag.getInteger("type"));
	}
	
	protected void dropFewItems(boolean par1, int par2)
    {
        int dropChance = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);
        int drops;

        for (drops = 0; drops < dropChance; ++drops)
        {
        	if (this.getType()==0)this.entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_WHITE), 0);
            else this.entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.FUR_BROWN), 0);
        }

        dropChance = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);
        if(dropChance>1)dropChance=1;
        
        for (drops = 0; drops < dropChance; ++drops)
        {
            if(this.isBurning())this.entityDropItem(new ItemStack(ModItems.hareCooked.itemID, 1, 0), 0);
            else this.entityDropItem(new ItemStack(ModItems.hareRaw.itemID, 1, 0), 0);
        }
    }
	
	public boolean isBreedingItem(ItemStack stack)
    {
        return stack.itemID == Item.carrot.itemID||stack.itemID == Item.carrotOnAStick.itemID;
    }
    //TODO: SOUNDS
}