package rikmuld.camping.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Mouse;

import rikmuld.camping.client.gui.screen.GuiScreenInvExtention;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.core.util.PlayerUtil;
import rikmuld.camping.network.packets.PacketCampInvPos;
import cpw.mods.fml.common.network.Player;

public class GuiContendHandler {

	public int[] guiXFlag;
	public int[] guiYFlag;
	public int[] guiXStart;
	public int[] guiYStart;
	
	public boolean[] clickFlag;
	public boolean[] clickedFlag;

	String name;
	
	public GuiContendHandler(int contendNum, String name)
	{
		this.name = name;
		
		guiXFlag = new int[contendNum];
		guiYFlag = new int[contendNum];
		guiXStart = new int[contendNum];
		guiYStart = new int[contendNum];
		clickFlag = new boolean[contendNum];
		clickedFlag = new boolean[contendNum];
	}
	
	private void resetContend(int contend)
	{
		guiXFlag[contend] = 0;
		guiYFlag[contend] = 0;
	}
	

	public void resetAll() 
	{
		guiXFlag = new int[guiXFlag.length];
		guiYFlag = new int[guiYFlag.length];
		guiXStart = new int[guiXStart.length];
		guiYStart = new int[guiYStart.length];
		clickFlag = new boolean[clickFlag.length];
		clickedFlag = new boolean[clickedFlag.length];
	}
	
	private void stopMovement(int contend)
	{
		guiXStart[contend] = -1000;
		guiYStart[contend] = -1000;
	}
	
	private void startMovement(int contend, int pointX, int pointY)
	{
		guiXStart[contend] = pointX-guiXFlag[contend];
		guiYStart[contend] = pointY-guiYFlag[contend];
	}
	
	private void move(int contend, int pointX, int pointY)
	{
		guiXFlag[contend]=pointX-guiXStart[contend];
		guiYFlag[contend]=pointY-guiYStart[contend];
	}
	
	public void updateContend(int contend, int pointX, int pointY, boolean pointed)
	{
		if(Mouse.isButtonDown(1))
		{	
			if(pointed&&clickedFlag[contend])clickFlag[contend] = true;
			if(clickFlag[contend])
			{
				if(guiXStart[contend]==-1000)this.startMovement(contend, pointX, pointY);
				this.move(contend, pointX, pointY);
			}
			else clickedFlag[contend] = false;
		}
		else 
		{
			this.stopMovement(contend);
			clickFlag[contend] = false;
			clickedFlag[contend] = true;
		}
		
		if(Mouse.isButtonDown(2)&&pointed) this.resetContend(contend);
	}
	
	public int posX(int contend)
	{
		return this.guiXFlag[contend];
	}
	
	public int posY(int contend)
	{
		return this.guiYFlag[contend];
	}
	
	public static GuiContendHandler readFromNBT(EntityPlayer player, String name)
	{
		if(!PlayerUtil.getPlayerDataTag(player).hasKey(name+".guiContends"))return null;
		NBTTagCompound tag = PlayerUtil.getPlayerDataTag(player).getCompoundTag(name+".guiContends");
						
		int contendNum = tag.getInteger("contendNum");
		
		GuiContendHandler handler = new GuiContendHandler(contendNum, name);
		
		handler.guiXFlag = tag.getIntArray("guiXFlag");
		handler.guiYFlag = tag.getIntArray("guiYFlag");
		handler.guiXStart = tag.getIntArray("guiXStart");
		handler.guiYStart = tag.getIntArray("guiYStart");
		
		return handler;
	}
	
	public void writeToNBT(EntityPlayer player)
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setIntArray("guiXFlag", guiXFlag);
		tag.setIntArray("guiYFlag", guiYFlag);
		tag.setIntArray("guiXStart", guiXStart);
		tag.setIntArray("guiYStart", guiYStart);
		tag.setInteger("contendNum", guiYStart.length);

		PlayerUtil.getPlayerDataTag(player).setCompoundTag(name+".guiContends", tag);
		this.sendClientContendsToServer(name, player, tag);
	}
	
	public void sendClientContendsToServer(String name, EntityPlayer player, NBTTagCompound tag)
	{
		if(PlayerUtil.getPlayerDataTag(player).hasKey(name+".guiContends"))
		{
			PacketUtil.sendToSever(new PacketCampInvPos(name+".guiContends", tag));
		}
	}
	
	public static void sendServerContendsToClient(String name, EntityPlayer player, NBTTagCompound tag)
	{
		if(PlayerUtil.getPlayerDataTag(player).hasKey(name+".guiContends"))
		{
			 PacketUtil.sendToPlayer(new PacketCampInvPos(name+".guiContends", tag), (Player) player);
		}
	}
}
