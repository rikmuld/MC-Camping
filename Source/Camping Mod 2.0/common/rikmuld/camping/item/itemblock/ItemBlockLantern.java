package rikmuld.camping.item.itemblock;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import rikmuld.camping.entity.tileentity.TileEntityLantern;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockLantern extends ItemBlockMain {

	public static final int LANTERN_ON = 0;
	public static final int LANTERN_OFF = 1;

	public static final String[] metadataIGNames = new String[]{"Lantern", "Burnt-Out Lantern"};
	public static final String[] metadataNames = new String[]{"lanternOn", "lanternOff"};

	public ItemBlockLantern(int id)
	{
		super(id, metadataIGNames, metadataNames, true, true);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if(stack.hasTagCompound())
		{
			list.add("Burning time left: " + (stack.getTagCompound().getInteger("time") / 2) + " seconds");
		}
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
		ItemStack log = new ItemStack(id, 1, 0);
		log.setTagCompound(new NBTTagCompound());
		log.getTagCompound().setInteger("time", 1500);
		stackList.add(log);

		stackList.add(new ItemStack(id, 1, 1));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
		{
			if(stack.hasTagCompound() && (stack.getItemDamage() == LANTERN_ON))
			{
				((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime = stack.getTagCompound().getInteger("time");
			}
			else
			{
				((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime = 0;
			}
			return true;
		}
		return false;
	}
}
