package rikmuld.camping.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockUnstableMain extends BlockMain{

	public BlockUnstableMain(String name, Material material) 
	{
		super(name, material);
	}
	
	public BlockUnstableMain(String name, Material material, boolean side) 
	{
		super(name, material, side);
	}

	public BlockUnstableMain(String name, Material material, String[] meta, Class<? extends ItemBlock> itemBlock, boolean side) 
	{
		super(name, material, meta, itemBlock, side);
	}
	
	public void dropIfCantStay(World world, int x, int y, int z)
	{
		if(!world.doesBlockHaveSolidTopSurface(x, y-1, z))
		{
			for(ItemStack item: this.getBlockDropped(world, x, y, z, 0, 1))this.dropBlockAsItem_do(world, x, y, z, item);
			world.setBlock(x, y, z, 0);
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        int l = world.getBlockId(x, y, z);
        Block block = Block.blocksList[l];
        return (block == null || block.isBlockReplaceable(world, x, y, z))&&world.doesBlockHaveSolidTopSurface(x, y-1, z);
    }
}
