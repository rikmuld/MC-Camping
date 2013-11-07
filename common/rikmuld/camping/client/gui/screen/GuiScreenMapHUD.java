package rikmuld.camping.client.gui.screen;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.HashMap;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;

public class GuiScreenMapHUD extends GuiScreen {

	public HashMap<EntityPlayer, byte[]> colorData = new HashMap<EntityPlayer, byte[]>();
	public HashMap<EntityPlayer, int[]> posData = new HashMap<EntityPlayer, int[]>();

	private int[] rgbColors = new int[16384];

	@Override
	public void drawScreen(int mouseX, int mouseY, float partTicks)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glScalef(0.5F,0.5F,0.5F);
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_UTILS));
		drawTexturedModalRect(width*2-133, 5, 0, 42, 128, 128);
		GL11.glScalef(2F, 2F, 2F);

		if(mc.thePlayer.worldObj.isRemote&&colorData!=null&&posData!=null&&colorData.get(mc.thePlayer)!=null&&posData.get(mc.thePlayer)!=null)
		{
			for(int i = 0; i<colorData.get(mc.thePlayer).length; ++i)
			{
				byte colorIndex = colorData.get(mc.thePlayer)[i];
				
				if(colorIndex/4==0)this.rgbColors[i] = (i+i/128&1)*8+16<<24;			
				else
				{
					int colorValue = MapColor.mapColorArray[colorIndex/4].colorValue;
					int heightFlag = colorIndex&3;
					short heigthDarkness = 220;

					if(heightFlag==2)heigthDarkness = 255;
					if(heightFlag==0)heigthDarkness = 180;
					
					int Red = (colorValue>>16&255)*heigthDarkness/255;
					int Green = (colorValue>>8&255)*heigthDarkness/255;
					int Blue = (colorValue&255)*heigthDarkness/255;
					this.rgbColors[i] = -16777216|Red<<16|Green<<8|Blue;
				}
			}

			ByteBuffer textureBuffer = BufferUtils.createByteBuffer(128*128*3);
			for(int i = 0; i<128*128; i++)
			{
				int pixel = rgbColors[i];
				Color color = Color.decode(Integer.toString(pixel));
				textureBuffer.put((byte) color.getRed());
				textureBuffer.put((byte) color.getGreen());
				textureBuffer.put((byte) color.getBlue());
			}
			textureBuffer.flip();

			int textureID = GL11.glGenTextures();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 128, 128, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, textureBuffer);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0f, 0f);
			GL11.glVertex2f(width-126f/2, 12f/2);

			GL11.glTexCoord2f(0f, 1f);
			GL11.glVertex2f(width-126f/2, 126f/2);

			GL11.glTexCoord2f(1f, 1f);
			GL11.glVertex2f(width-12/2, 126f/2);

			GL11.glTexCoord2f(1f, 0f);
			GL11.glVertex2f(width-12f/2, 12f/2);
			GL11.glEnd();
	
			for(int i = 0; i<mc.theWorld.playerEntities.size(); i++)
			{
				EntityPlayer player = (EntityPlayer) mc.theWorld.playerEntities.get(i);
				
				float scale = (float) (57F/(64F*(Math.pow(2, posData.get(mc.thePlayer)[0]))));

				int xDivision = (int)(scale*(player.posX-posData.get(mc.thePlayer)[1]));
				int zDivision = (int)(scale*(player.posZ-posData.get(mc.thePlayer)[2]));
			
				if(xDivision>57)xDivision = 57;
				if(xDivision<-57)xDivision = -57;

				if(zDivision>57)zDivision = 57;
				if(zDivision<-57)zDivision = -57;
				
				if(mc.thePlayer==player)mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.RED_DOT));
				else mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.BLUE_DOT));
				
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0f, 0f);
				GL11.glVertex2f(width-69/2+xDivision/2-3, 69/2+zDivision/2-3);

				GL11.glTexCoord2f(0f, 1f);
				GL11.glVertex2f(width-69/2+xDivision/2-3, 69/2+zDivision/2+3);

				GL11.glTexCoord2f(1f, 1f);
				GL11.glVertex2f(width-69/2+xDivision/2+3, 69/2+zDivision/2+3);

				GL11.glTexCoord2f(1f, 0f);
				GL11.glVertex2f(width-69/2+xDivision/2+3, 69/2+zDivision/2-3);
				GL11.glEnd();
			}
		}

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
	}
}
