package rikmuld.camping.misc.bounds;


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
	
	public Bounds getBoundsOnRelativePoistion(int xDiv, int yDiv, int zDiv)
	{				
		return new Bounds(bounds.xMin-xDiv, bounds.yMin-yDiv, bounds.zMin-zDiv, bounds.xMax-xDiv, bounds.yMax-yDiv, bounds.zMax-zDiv);
	}
}
