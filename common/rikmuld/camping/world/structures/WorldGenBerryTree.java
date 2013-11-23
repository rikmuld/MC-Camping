package rikmuld.camping.world.structures;

import java.util.Random;

import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModLogger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBerryTree extends WorldGenerator {

	int[][] leafLoc = new int[][]{{2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, 1, 1, 1, 0, 0, 0, -1, -1, -1},{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4},{-2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -1, 0, 1, -1, 0, 1, -1, 0, 1}};
	
	public boolean generate(World world, Random random, int x, int y, int z)
	{		
		for(int i = 1; i<6; i++)if(world.getBlockId(x, y+i, z)!=0)return false;	
		for(int i = 0; i<leafLoc[0].length; i++)
		if(world.getBlockId(x+leafLoc[0][i], y+leafLoc[1][i]+1, z+leafLoc[2][i])==0)
		{
			if(leafLoc[1][i]==4)ModLogger.logMulti("X-Z: " + Integer.toString(leafLoc[0][i]) + " " + Integer.toString(leafLoc[2][i]));
			world.setBlock(x+leafLoc[0][i], y+leafLoc[1][i]+1, z+leafLoc[2][i], ModBlocks.leaves.blockID, random.nextInt(4)!=0? 0:random.nextInt(2)==0? 1:2, 2);
		}
		for(int i = 1; i<5; i++) world.setBlock(x, y+i, z, Block.wood.blockID);
		return true;
	}
}
