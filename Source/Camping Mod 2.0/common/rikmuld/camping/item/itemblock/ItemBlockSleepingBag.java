package rikmuld.camping.item.itemblock;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockSleepingBag extends ItemBlockMain {

	public static final int BED_HEAD = 0;
	public static final int BED_FOOD = 1;

	public static final String[] metadataIGNames = new String[]{"Sleeping Bag", ""};
	public static final String[] metadataNames = new String[]{"sleepingBag", ""};

	public ItemBlockSleepingBag(int id)
	{
		super(id, metadataIGNames, metadataNames, true, true);
		setMaxStackSize(1);
	}

	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativetabs, List stackList)
	{
		stackList.add(new ItemStack(id, 1, 0));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}
}
