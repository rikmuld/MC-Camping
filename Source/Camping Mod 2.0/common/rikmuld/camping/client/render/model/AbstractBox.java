package rikmuld.camping.client.render.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AbstractBox {

	private PositionTextureVertex[] vertexPositions;

	private TexturedQuad[] quadList;

	public final float posX1;
	public final float posY1;
	public final float posZ1;
	public final float posX2;
	public final float posY2;
	public final float posZ2;

	public String field_78247_g;

	public float modelBlockToWorldBlockRatio;

	public AbstractBox(int texWidth, int texHeigth, boolean mirror, int texturerOffsetX, int textureOffsetY, float x, float y, float z, int width, int higth, int depth, float ratio, float xScale, float yScale, float zScale)
	{
		modelBlockToWorldBlockRatio = ratio;

		posX1 = x;
		posY1 = y;
		posZ1 = z;
		posX2 = x + width;
		posY2 = y + higth;
		posZ2 = z + depth;
		vertexPositions = new PositionTextureVertex[8];
		quadList = new TexturedQuad[6];
		float f4 = x + width;
		float f5 = y + higth;
		float f6 = z + depth;
		x -= xScale;
		y -= yScale;
		z -= zScale;
		f4 += xScale;
		f5 += yScale;
		f6 += zScale;

		if(mirror)
		{
			float f7 = f4;
			f4 = x;
			x = f7;
		}

		PositionTextureVertex positiontexturevertex = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, y, z, 0.0F, 8.0F);
		PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(f4, f5, z, 8.0F, 8.0F);
		PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(x, f5, y, 8.0F, 0.0F);
		PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(x, y, f6, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, y, f6, 0.0F, 8.0F);
		PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(f4, f5, f6, 8.0F, 8.0F);
		PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, f5, f6, 8.0F, 0.0F);
		vertexPositions[0] = positiontexturevertex;
		vertexPositions[1] = positiontexturevertex1;
		vertexPositions[2] = positiontexturevertex2;
		vertexPositions[3] = positiontexturevertex3;
		vertexPositions[4] = positiontexturevertex4;
		vertexPositions[5] = positiontexturevertex5;
		vertexPositions[6] = positiontexturevertex6;
		vertexPositions[7] = positiontexturevertex7;
		quadList[0] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6}, texturerOffsetX + depth + width, textureOffsetY + depth, texturerOffsetX + depth + width + depth, textureOffsetY + depth + higth, texWidth, texHeigth);
		quadList[1] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3}, texturerOffsetX, textureOffsetY + depth, texturerOffsetX + depth, textureOffsetY + depth + higth, texWidth, texHeigth);
		quadList[2] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1}, texturerOffsetX + depth, textureOffsetY, texturerOffsetX + depth + width, textureOffsetY + depth, texWidth, texHeigth);
		quadList[3] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6}, texturerOffsetX + depth + width, textureOffsetY + depth, texturerOffsetX + depth + width + width, textureOffsetY, texWidth, texHeigth);
		quadList[4] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2}, texturerOffsetX + depth, textureOffsetY + depth, texturerOffsetX + depth + width, textureOffsetY + depth + higth, texWidth, texHeigth);
		quadList[5] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7}, texturerOffsetX + depth + width + depth, textureOffsetY + depth, texturerOffsetX + depth + width + depth + width, textureOffsetY + depth + higth, texWidth, texHeigth);

		if(mirror)
		{
			for(int j1 = 0; j1 < quadList.length; ++j1)
			{
				quadList[j1].flipFace();
			}
		}
	}

	public AbstractBox func_78244_a(String par1Str)
	{
		field_78247_g = par1Str;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public void render(Tessellator par1Tessellator)
	{
		for(int i = 0; i < quadList.length; ++i)
		{
			quadList[i].draw(par1Tessellator, modelBlockToWorldBlockRatio);
		}
	}
}
