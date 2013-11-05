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
import net.minecraft.util.Vec3;
import rikmuld.camping.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;

public class PacketMain {

	public PacketTypeHandler packetType;
	public boolean isChunkDataPacket;

	public PacketMain(PacketTypeHandler packetType, boolean isChunkDataPacket)
	{
		this.packetType = packetType;
		this.isChunkDataPacket = isChunkDataPacket;
	}

	public byte[] populate()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try
		{
			dos.writeByte(packetType.ordinal());
			this.writeData(dos);
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
		return bos.toByteArray();
	}

	public void readPopulate(DataInputStream data)
	{
		try
		{
			this.readData(data);
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	public void readData(DataInputStream data) throws IOException
	{}

	public void writeData(DataOutputStream dos) throws IOException
	{}

	public void setKey(int key)
	{}

	public void execute(INetworkManager network, EntityPlayer player)
	{}
	
    public static ItemStack readItemStack(DataInput dataStream) throws IOException
    {
        ItemStack itemstack = null;
        short id = dataStream.readShort();

        if (id >= 0)
        {
            byte stackSize = dataStream.readByte();
            short damage = dataStream.readShort();
            itemstack = new ItemStack(id, stackSize, damage);
            itemstack.stackTagCompound = readNBTTagCompound(dataStream);
        }

        return itemstack;
    }

    public static void writeItemStack(ItemStack stack, DataOutput dataStream) throws IOException
    {
        if (stack == null)
        {
            dataStream.writeShort(-1);
        }
        else
        {
            dataStream.writeShort(stack.itemID);
            dataStream.writeByte(stack.stackSize);
            dataStream.writeShort(stack.getItemDamage());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag())
            {
                nbttagcompound = stack.stackTagCompound;
            }

            writeNBTTagCompound(nbttagcompound, dataStream);
        }
    }

    public static NBTTagCompound readNBTTagCompound(DataInput dataStream) throws IOException
    {
        short short1 = dataStream.readShort();

        if (short1 < 0)
        {
            return null;
        }
        else
        {
            byte[] abyte = new byte[short1];
            dataStream.readFully(abyte);
            return CompressedStreamTools.decompress(abyte);
        }
    }

    protected static void writeNBTTagCompound(NBTTagCompound tag, DataOutput dataStream) throws IOException
    {
        if (tag == null)
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
}
