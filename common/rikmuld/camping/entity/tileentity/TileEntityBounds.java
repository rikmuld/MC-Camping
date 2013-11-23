package rikmuld.camping.entity.tileentity;

import rikmuld.camping.misc.bounds.Bounds;


public class TileEntityBounds extends TileEntityMain {

	public Bounds bounds;
	
	public void setBounds(Bounds bounds)
	{
		this.bounds = bounds;
	}
}
