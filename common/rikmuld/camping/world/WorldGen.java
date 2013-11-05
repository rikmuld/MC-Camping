package rikmuld.camping.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import rikmuld.camping.world.structures.WorldGenHemp;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {

 	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch(world.provider.dimensionId)
		{
			case -1:
				generateNether(world, random, chunkX*16, chunkZ*16);
				break;
			case 0:
				generateSurface(world, random, chunkX*16, chunkZ*16);
				break;
			case 1:
				generateEnd(world, random, chunkX*16, chunkZ*16);
				break;
		}

		if(world.provider.dimensionId!=-1&&world.provider.dimensionId!=1)
		{
			generateSurface(world, random, chunkX*16, chunkZ*16);
		}
	}

	private void generateSurface(World world, Random random, int blockX, int blockZ)
	{
		int Xcoord = blockX+random.nextInt(16);
		int Ycoord = random.nextInt(15)+55;
		int Zcoord = blockZ+random.nextInt(16);

		WorldGenHemp hemp = new WorldGenHemp();
		hemp.generate(world, random, Xcoord, Ycoord, Zcoord);
	}

	private void generateNether(World world, Random random, int blockX, int blockZ)
	{

	}

	private void generateEnd(World world, Random random, int blockX, int blockZ)
	{

	}
}
