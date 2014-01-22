package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.core.util.KeyUtil;

public class PacketKeyPressed extends PacketMain{

	int id; 
	boolean keyDown;
	
	public PacketKeyPressed()
	{
		super(false);
	}

	public PacketKeyPressed(int id, boolean keyDown)
	{
		super(false);
		
		this.id = id;
		this.keyDown = keyDown;
	}
	
	@Override
	public void readData(DataInputStream dataInput) throws IOException
	{
		id = dataInput.readInt();
		keyDown = dataInput.readBoolean();
	}
	
	@Override
	public void writeData(DataOutputStream dataOutput) throws IOException
	{
		dataOutput.writeInt(id);
		dataOutput.writeBoolean(keyDown);
	}

	public void execute(INetworkManager network, EntityPlayer player)
	{
		KeyUtil.fireKey(keyDown, id, player);
	}
}
