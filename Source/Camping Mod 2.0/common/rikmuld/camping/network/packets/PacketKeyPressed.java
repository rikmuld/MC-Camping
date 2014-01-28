package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.core.util.KeyUtil;

public class PacketKeyPressed extends PacketMain {

	String key;
	boolean keyDown;

	public PacketKeyPressed()
	{
		super(false);
	}

	public PacketKeyPressed(String keyDescription, boolean keyDown)
	{
		super(false);

		this.key = keyDescription;
		this.keyDown = keyDown;
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		KeyUtil.fireKey(keyDown, key, player);
	}

	@Override
	public void readData(DataInputStream dataInput) throws IOException
	{
		key = dataInput.readUTF();
		keyDown = dataInput.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream dataOutput) throws IOException
	{
		dataOutput.writeUTF(key);
		dataOutput.writeBoolean(keyDown);
	}
}
