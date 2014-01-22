package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.entity.tileentity.TileEntityInventory;

public class PacketItems extends PacketMain {

	public int x;
	public int y;
	public int z;
	public int slot;
	public ItemStack stack;

	public PacketItems()
	{
		super(true);
	}

	public PacketItems(int slot, int x, int y, int z, ItemStack stack)
	{
		super(true);

		this.slot = slot;
		this.x = x;
		this.y = y;
		this.z = z;
		this.stack = stack;
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		if(player.worldObj.getBlockTileEntity(x, y, z) != null)
		{
			((TileEntityInventory)player.worldObj.getBlockTileEntity(x, y, z)).setInventorySlotContents(slot, stack);
		}
	}

	@Override
	public void readData(DataInputStream dataStream) throws IOException
	{
		slot = dataStream.readInt();
		x = dataStream.readInt();
		y = dataStream.readInt();
		z = dataStream.readInt();
		stack = readItemStack(dataStream);
	}

	@Override
	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(slot);
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
		writeItemStack(stack, dataStream);
	}
}
