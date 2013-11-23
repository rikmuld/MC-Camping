package rikmuld.camping.misc.bounds;

import java.util.ArrayList;

public class BoundsTracker {

	Bounds bounds;
	int baseX;
	int baseY;
	int baseZ;
	
	public BoundsTracker(int x, int y, int z, Bounds bounds)
	{
		this.bounds = bounds;
		
		this.baseX = x;
		this.baseY = y;
		this.baseZ = z;
	}
	
	public Bounds getBoundsOnPoistion(int x, int y, int z)
	{		
		int xDiv = baseX-x;
		int yDiv = baseY-y;
		int zDiv = baseZ-z;
		
		return new Bounds(bounds.xMin-=xDiv, bounds.yMin-=yDiv, bounds.zMin-=zDiv, bounds.xMax-=xDiv, bounds.yMax-=yDiv, bounds.zMax-=zDiv);
	}
}
