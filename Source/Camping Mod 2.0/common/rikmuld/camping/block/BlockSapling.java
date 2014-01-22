package rikmuld.camping.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.BonemealEvent;
import rikmuld.camping.world.structures.WorldGenBerryTree;

public class BlockSapling extends BlockUnstableMain {

	public BlockSapling(String name)
	{
		super(name, Material.grass);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setHardness(0.0F);
		setStepSound(soundGrassFootstep);
		setTickRandomly(true);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAllignedBB, List list, Entity entity)
	{}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		world.getBlockId(x, y, z);
		return(super.canPlaceBlockAt(world, x, y, z) && ((world.getBlockId(x, y - 1, z) == Block.grass.blockID) || (world.getBlockId(x, y - 1, z) == Block.dirt.blockID)));
	}

	@Override
	public int getRenderType()
	{
		return 1;
	}

	public void growTree(World world, int x, int y, int z, Random random, BonemealEvent event)
	{
		if(new WorldGenBerryTree().generate(world, random, x, y - 1, z))
		{
			if(event != null)
			{
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	public void markOrGrowMarked(World world, int x, int y, int z, Random random)
	{
		growTree(world, x, y, z, random, null);
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)
		{
			super.updateTick(world, x, y, z, random);

			if((world.getBlockLightValue(x, y + 1, z) >= 9) && (random.nextInt(10) == 0))
			{
				markOrGrowMarked(world, x, y, z, random);
			}
		}
	}
}
