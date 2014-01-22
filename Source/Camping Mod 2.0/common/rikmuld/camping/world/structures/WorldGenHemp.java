package rikmuld.camping.world.structures;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import rikmuld.camping.core.register.ModBlocks;

public class WorldGenHemp extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		for(int l = 0; l < 20; ++l)
		{
			int i1 = (x + random.nextInt(4)) - random.nextInt(4);
			int j1 = y;
			int k1 = (z + random.nextInt(4)) - random.nextInt(4);

			if(world.isAirBlock(i1, y, k1) && ((world.getBlockMaterial(i1 - 1, y - 1, k1) == Material.water) || (world.getBlockMaterial(i1 + 1, y - 1, k1) == Material.water) || (world.getBlockMaterial(i1, y - 1, k1 - 1) == Material.water) || (world.getBlockMaterial(i1, y - 1, k1 + 1) == Material.water)))
			{
				int l1 = 0 + random.nextInt(random.nextInt(4) + 1);

				if(ModBlocks.hemp.canBlockStay(world, i1, j1, k1))
				{
					world.setBlock(i1, j1, k1, ModBlocks.hemp.blockID, l1, 2);
				}

				if(l1 == 4)
				{
					world.setBlock(i1, j1 + 1, k1, ModBlocks.hemp.blockID, 5, 2);
				}
			}
		}

		return true;
	}
}
