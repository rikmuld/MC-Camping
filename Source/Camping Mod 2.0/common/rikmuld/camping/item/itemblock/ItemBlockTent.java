package rikmuld.camping.item.itemblock;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import rikmuld.camping.block.BlockTent;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.entity.tileentity.TileEntityLantern;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockTent extends ItemBlockMain{

	public ItemBlockTent(int id)
	{
		super(id, BlockInfo.TENT, false);
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPartHit, float yPartHit, float zPartHit)
    {
    	((BlockTent)ModBlocks.tent).rotationYaw = player.rotationYaw;
    	return super.onItemUse(stack, player, world, x, y, z, side, xPartHit, yPartHit, zPartHit);
    }
}
