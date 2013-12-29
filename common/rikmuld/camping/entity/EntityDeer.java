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
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemAnimalStuff;

public class EntityDeer extends EntityAnimal{

	public EntityDeer(World world) 
	{
		super(world);
        this.setSize(1F, 1.5F);
        this.tasks.addTask(1, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAISwimming(this)); 
        this.tasks.addTask(5, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(6, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(7, new EntityAITempt(this, 1.2D, Item.wheat.itemID, true));
        this.tasks.addTask(8, new EntityAIMate(this, 1.0D));
	}
	
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(15.0D);
    }
    
    public boolean isAIEnabled()
    {
        return true;
    } 
    
    public int getTotalArmorValue()
    {
        return 5;
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
		return new EntityDeer(worldObj);
	}
    
	protected void dropFewItems(boolean par1, int par2)
    {
        int dropChance = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);
        int drops;

        for (drops = 0; drops < dropChance; ++drops)this.entityDropItem(new ItemStack(Item.leather.itemID, 1, 0), 0);           
        dropChance = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

        for (drops = 0; drops < dropChance; ++drops)
        {
            if(this.isBurning())this.entityDropItem(new ItemStack(ModItems.vanisonCooked.itemID, 1, 0), 0);
            else this.entityDropItem(new ItemStack(ModItems.vanisonRaw.itemID, 1, 0), 0);
        }
        
        dropChance = this.rand.nextInt(20) - 18 + this.rand.nextInt(1 + par2);
        if(dropChance>2)dropChance=2;
        
        for (drops = 0; drops < dropChance; ++drops)this.entityDropItem(new ItemStack(ModItems.animalStuff.itemID, 1, ItemAnimalStuff.ANTLER), 0);        
    }

    //SOUNDS
}