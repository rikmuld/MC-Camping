package rikmuld.camping.core.register;

import rikmuld.camping.core.lib.StructureInfo;
import rikmuld.camping.misc.bounds.BoundsStructure;
import rikmuld.camping.misc.bounds.BoundsStructureRegister;

public class ModStructures {

	public static BoundsStructure[] tent;

	public static void init()
	{
		int[] xLine = new int[]{1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0};
		int[] yLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		int[] zLine = new int[]{0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2};

		BoundsStructureRegister.regsisterStructure(StructureInfo.TENT, xLine, yLine, zLine, true);
		tent = BoundsStructureRegister.getRotatedStructure(StructureInfo.TENT);
	}
}
