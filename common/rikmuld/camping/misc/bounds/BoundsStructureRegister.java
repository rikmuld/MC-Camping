package rikmuld.camping.misc.bounds;

import java.util.HashMap;

import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.MathUtil;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BoundsStructureRegister {

	public static HashMap<String, BoundsStructure> structures = new HashMap<String, BoundsStructure>();
	public static HashMap<String, BoundsStructure[]> rotatedStructures = new HashMap<String, BoundsStructure[]>();
	
	public static void regsisterStructure(String name, int[] xCoords, int[] yCoords, int zCoords[], boolean rotation)
	{
		if(!rotation)structures.put(name, new BoundsStructure(new int[][]{xCoords, yCoords, zCoords}));
		else
		{
			BoundsStructure[] structure = new BoundsStructure[4];
			
			structure[0] = new BoundsStructure(new int[][]{xCoords, yCoords, zCoords});
			structure[1] = new BoundsStructure(new int[][]{MathUtil.inverse(zCoords) ,yCoords, MathUtil.inverse(xCoords)});
			structure[2] = new BoundsStructure(new int[][]{MathUtil.inverse(xCoords) ,yCoords, MathUtil.inverse(zCoords)});
			structure[3] = new BoundsStructure(new int[][]{zCoords ,yCoords, xCoords});
			
			rotatedStructures.put(name, structure);
		}
	}
	
	public static BoundsStructure getStructure(String name)
	{
		return structures.get(name);
	}
	
	public static BoundsStructure[] getRotatedStructure(String name)
	{
		return rotatedStructures.get(name);
	}
}
