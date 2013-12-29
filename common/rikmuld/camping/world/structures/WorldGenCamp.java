package rikmuld.camping.world.structures;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.entity.EntityCamper;
import rikmuld.camping.entity.tileentity.TileEntityTent;

public class WorldGenCamp extends WorldGenerator {

	public boolean LocationIsValidSpawn(World world, int i, int j, int k)
	{
		return (world.getBlockId(i, j, k)==Block.grass.blockID);
	}

	public boolean generate(World world, Random rand, int x, int y, int z)
	{		
		while(world.getBlockId(x, y+1, z)!=0||world.getBlockId(x, y+2, z)!=0)y++;
		
		if(!LocationIsValidSpawn(world, x-1, y, z)||!LocationIsValidSpawn(world, x-1, y, z+2)||!LocationIsValidSpawn(world, x+4, y, z+2)||!LocationIsValidSpawn(world, x+4, y, z)||!this.isValitSpawn(world, x, y, z, 6, 3)) return false;
			
		world.setBlock(x+0, y+1, z+1, ModBlocks.campfireDeco.blockID, 0, 2);
		world.setBlock(x+2, y+1, z+1, ModBlocks.tent.blockID, 2, 2);
		((TileEntityTent)world.getBlockTileEntity(x+2, y+1, z+1)).setRotation(3);
		((TileEntityTent)world.getBlockTileEntity(x+2, y+1, z+1)).setContends(1, TileEntityTent.BEDS, true, 0);

		world.spawnEntityInWorld(new EntityCamper(world, x, y+1, z));
		return true;
	}
	
	public boolean isValitSpawn(World world, int x, int y, int z, int xLength, int zLength)
	{
		for(int i = 0; i<xLength; i++)
		{
			for(int j = 0; j<zLength; j++)
			{
				if(world.getBlockId(x+xLength, y+1, z+zLength)!=0)return false;
			}
		}
		return true;
	}
}
