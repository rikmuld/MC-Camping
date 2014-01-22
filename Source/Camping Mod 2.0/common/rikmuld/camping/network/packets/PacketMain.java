package rikmuld.camping.network.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.network.PacketHandler;

public abstract class PacketMain {

	public boolean isChunkDataPacket;

	public PacketMain(boolean isChunkDataPacket)
	{
		this.isChunkDataPacket = isChunkDataPacket;
	}

	public static ItemStack readItemStack(DataInput dataStream) throws IOException
	{
		ItemStack itemstack = null;
		short id = dataStream.readShort();

		if(id >= 0)
		{
			byte stackSize = dataStream.readByte();
			short damage = dataStream.readShort();
			itemstack = new ItemStack(id, stackSize, damage);
			itemstack.stackTagCompound = readNBTTagCompound(dataStream);
		}

		return itemstack;
	}

	public static NBTTagCompound readNBTTagCompound(DataInput dataStream) throws IOException
	{
		short short1 = dataStream.readShort();

		if(short1 < 0) return null;
		else
		{
			byte[] abyte = new byte[short1];
			dataStream.readFully(abyte);
			return CompressedStreamTools.decompress(abyte);
		}
	}

	public static void writeItemStack(ItemStack stack, DataOutput dataStream) throws IOException
	{
		if(stack == null)
		{
			dataStream.writeShort(-1);
		}
		else
		{
			dataStream.writeShort(stack.itemID);
			dataStream.writeByte(stack.stackSize);
			dataStream.writeShort(stack.getItemDamage());
			NBTTagCompound nbttagcompound = null;

			if(stack.getItem().isDamageable() || stack.getItem().getShareTag())
			{
				nbttagcompound = stack.stackTagCompound;
			}

			writeNBTTagCompound(nbttagcompound, dataStream);
		}
	}

	protected static void writeNBTTagCompound(NBTTagCompound tag, DataOutput dataStream) throws IOException
	{
		if(tag == null)
		{
			dataStream.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(tag);
			dataStream.writeShort((short)abyte.length);
			dataStream.write(abyte);
		}
	}

	public abstract void execute(INetworkManager network, EntityPlayer player);

	public Integer getType()
	{
		return PacketHandler.packets.indexOf(getClass());
	}

	public byte[] populate()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try
		{
			dos.writeByte(getType());
			writeData(dos);
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
		return bos.toByteArray();
	}

	public abstract void readData(DataInputStream data) throws IOException;

	public void readPopulate(DataInputStream data)
	{
		try
		{
			readData(data);
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	public abstract void writeData(DataOutputStream dos) throws IOException;
}
