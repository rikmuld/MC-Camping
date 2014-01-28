package rikmuld.camping.item.food;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoInteger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFoodStew extends ItemFoodMain {

	public static final int MEAT = 0;
	public static final int VEGETABLE = 1;
	public static final int WIRED = 2;

	public static final String[] metadataIGNames = new String[]{"Meat Stew", "Vegetable Stew", "Wired Stew"};
	public static final String[] metadataNames = new String[]{"stewMeat", "stewVegetable", "stewWired"};

	public ItemFoodStew(String name)
	{
		super(name, metadataIGNames, metadataNames, true, ConfigInfoInteger.value(ConfigInfo.HEAL_STEW), 4F, false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativetabs, List list)
	{
		for(int meta = 0; meta < metadataNames.length; ++meta)
		{
			list.add(new ItemStack(id, 1, meta));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		--stack.stackSize;

		if((stack.getItemDamage() == WIRED) && !world.isRemote)
		{
			PotionEffect[] effects = new PotionEffect[23];
			for(int i = 0; i < 23; i++)
			{
				effects[i] = new PotionEffect(i, 400, 0);
			}
			Random rand = new Random();
			player.addPotionEffect(effects[rand.nextInt(23)]);
		}

		player.getFoodStats().addStats(this);

		world.playSoundAtEntity(player, "random.burp", 0.5F, (world.rand.nextFloat() * 0.1F) + 0.9F);
		onFoodEaten(stack, world, player);

		if(!player.inventory.addItemStackToInventory(new ItemStack(Item.bowlEmpty)))
		{
			player.dropPlayerItem(new ItemStack(Item.bowlEmpty));
		}

		return stack;
	}
}
