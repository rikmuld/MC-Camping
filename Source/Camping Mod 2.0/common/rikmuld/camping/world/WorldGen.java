package rikmuld.camping.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoInteger;
import rikmuld.camping.world.structures.WorldGenBerryTree;
import rikmuld.camping.world.structures.WorldGenCamp;
import rikmuld.camping.world.structures.WorldGenHemp;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {

	WorldGenBerryTree tree = new WorldGenBerryTree();
	WorldGenHemp hemp = new WorldGenHemp();
	int xCoord, yCoord, zCoord;

 	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
 		if(ConfigInfoBoolean.value(ConfigInfo.USE_WORLDGEN))
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
 	}
	
	private void generateSurface(World world, Random random, int blockX, int blockZ)
	{	
 		if(ConfigInfoBoolean.value(ConfigInfo.USE_WORLDGEN_HEMP))
 		{
			for(int i = 0; i<((50-ConfigInfoInteger.value(ConfigInfo.RARENESS_HEMP))>0? (50-ConfigInfoInteger.value(ConfigInfo.RARENESS_HEMP)):1); i++)
			{
				xCoord = blockX+random.nextInt(16);
				yCoord = random.nextInt(15)+55;
				zCoord = blockZ+random.nextInt(16);
			
				hemp.generate(world, random, xCoord, yCoord, zCoord);
			}
 		}
 		
		if(BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(blockX, blockZ), BiomeDictionary.Type.FOREST)&&world.getBiomeGenForCoords(blockX, blockZ).getIntTemperature()==BiomeGenBase.forest.getIntTemperature())
		{	
			if(ConfigInfoBoolean.value(ConfigInfo.USE_WORLDGEN_TREE))
		 	{
				if(random.nextInt(ConfigInfoInteger.value(ConfigInfo.RARENESS_TREE))==0)
				{
					xCoord = blockX+random.nextInt(16);
					yCoord = random.nextInt(15)+55;
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
			
			if(ConfigInfoBoolean.value(ConfigInfo.USE_WORLDGEN_CAMP))
	 		{
				if(random.nextInt(ConfigInfoInteger.value(ConfigInfo.RARENESS_CAMP))==0)
				{
					for(int j = 0; j<6; j++)
					{
						xCoord = blockX+random.nextInt(16);
						yCoord = 50;
						zCoord = blockZ+random.nextInt(16);
			
						if((new WorldGenCamp()).generate(world, random, xCoord, yCoord, zCoord))break;
					}	
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
