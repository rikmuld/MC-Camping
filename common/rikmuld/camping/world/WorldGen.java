package rikmuld.camping.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.BiomeDictionary;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.world.structures.WorldGenBerryTree;
import rikmuld.camping.world.structures.WorldGenHemp;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {

	WorldGenBerryTree tree = new WorldGenBerryTree();
	WorldGenHemp hemp = new WorldGenHemp();

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
		int xCoord = blockX+random.nextInt(16);
		int yCoord = random.nextInt(15)+55;
		int zCoord = blockZ+random.nextInt(16);

		hemp.generate(world, random, xCoord, yCoord, zCoord);
		
		if(BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(blockX, blockZ), BiomeDictionary.Type.FOREST)&&random.nextInt(40)==0&&world.getBiomeGenForCoords(blockX, blockZ).getIntTemperature()==BiomeGenBase.forest.getIntTemperature())
		{		
			xCoord = blockX+random.nextInt(16);
			zCoord = blockZ+random.nextInt(16);
			
			for(int j = 75; j>60; j--)
			{
				if(world.getBlockId(xCoord, j, zCoord)==0&&world.getBlockId(xCoord, j-1, zCoord)==Block.grass.blockID)
				{
					tree.generate(world, random, xCoord, j-1, zCoord);
					break;
				}
			}
		}
		
	}

	private void generateNether(World world, Random random, int blockX, int blockZ)
	{

	}

	private void generateEnd(World world, Random random, int blockX, int blockZ)
	{

	}
}
