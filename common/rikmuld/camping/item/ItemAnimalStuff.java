package rikmuld.camping.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAnimalStuff extends ItemMain{

	public static final int FUR_WHITE = 0;
	public static final int FUR_BROWN = 1;
	public static final int ANTLER = 2;

	public static final String[] metadataIGNames = new String[]{"White Fur", "Brown Fur", "Antler"};
	public static final String[] metadataNames = new String[]{"furWhite", "furBrown", "antler"};

	public ItemAnimalStuff(String name)
	{
		super(name, metadataIGNames, metadataNames, true);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativetabs, List list)
	{
		for(int meta = 0; meta<metadataNames.length; ++meta)
		{
			list.add(new ItemStack(id, 1, meta));
		}
	}
}
