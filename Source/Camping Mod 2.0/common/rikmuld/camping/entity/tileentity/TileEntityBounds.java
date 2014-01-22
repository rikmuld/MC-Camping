package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.network.packets.PacketBounds;

public class TileEntityBounds extends TileEntityMain {

	public Bounds bounds;

	public int baseX;
	public int baseY;
	public int baseZ;

	private boolean update;

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		baseX = tag.getInteger("baseX");
		baseY = tag.getInteger("baseY");
		baseZ = tag.getInteger("baseZ");
		if(tag.hasKey("xMin"))
		{
			setBounds(Bounds.readBoundsToNBT(tag));
		}
	}

	public void setBaseCoords(int x, int y, int z)
	{
		baseX = x;
		baseY = y;
		baseZ = z;

		sendTileData(0, true, x, y, z);
	}

	public void setBounds(Bounds bounds)
	{
		this.bounds = bounds;
		update = true;
	}

	@Override
	public void setTileData(int id, int[] data)
	{
		if(id == 0)
		{
			baseX = data[0];
			baseY = data[1];
			baseZ = data[2];
		}
	}

	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote && update)
		{
			PacketUtil.sendToAllPlayers(new PacketBounds(bounds, xCoord, yCoord, zCoord));
			update = false;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setInteger("baseX", baseX);
		tag.setInteger("baseY", baseY);
		tag.setInteger("baseZ", baseZ);
		if(bounds != null)
		{
			bounds.writeBoundsToNBT(tag);
		}
	}
}
