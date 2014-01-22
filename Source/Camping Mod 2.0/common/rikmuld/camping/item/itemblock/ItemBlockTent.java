package rikmuld.camping.item.itemblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.block.BlockTent;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.register.ModBlocks;

public class ItemBlockTent extends ItemBlockMain {

	public ItemBlockTent(int id)
	{
		super(id, BlockInfo.TENT, false);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xPartHit, float yPartHit, float zPartHit)
	{
		((BlockTent)ModBlocks.tent).rotationYaw = player.rotationYaw;
		return super.onItemUse(stack, player, world, x, y, z, side, xPartHit, yPartHit, zPartHit);
	}
}
