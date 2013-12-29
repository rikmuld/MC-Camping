package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.entity.tileentity.TileEntityInventory;
import rikmuld.camping.network.PacketTypeHandler;

public class PacketItems extends PacketMain {

	public int x;
	public int y;
	public int z;
	public int slot;
	public ItemStack stack;

	public PacketItems()
	{
		super(PacketTypeHandler.ITEMS, true);
	}

	public PacketItems(int slot, int x, int y, int z, ItemStack stack)
	{
		super(PacketTypeHandler.ITEMS, true);
		
		this.slot = slot;
		this.x = x;
		this.y = y;
		this.z = z;
		this.stack = stack;
	}

	public void readData(DataInputStream dataStream) throws IOException
	{
		this.slot = dataStream.readInt();
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();
		this.stack = this.readItemStack(dataStream);
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(slot);
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
		this.writeItemStack(stack, dataStream);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		if(player.worldObj.getBlockTileEntity(x, y, z)!=null)((TileEntityInventory) player.worldObj.getBlockTileEntity(x, y, z)).setInventorySlotContents(slot, stack);
	}
}
