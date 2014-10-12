package com.rikmuld.camping.misc

import net.minecraft.client.model.PositionTextureVertex
import net.minecraft.client.model.TexturedQuad
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.Tessellator
import cpw.mods.fml.relauncher.Side

class AbstractBox(texWidth:Int, texHeigth:Int, mirror:Boolean, texturerOffsetX:Int, textureOffsetY:Int, var x:Float, var y:Float, var z:Float, width:Int, height:Int, depth:Int, ratio:Float, xScale:Float, yScale:Float, zScale:Float) {
	private val vertexPositions = new Array[PositionTextureVertex](8)
	private val quadList = new Array[TexturedQuad](6)
	val posX2 = x + width
	val posY2 = y + height
	val posZ2 = z + depth
	var f4 = x + width
	var f5 = y + height
	var f6 = z + depth
	x -= xScale
	y -= yScale
	z -= zScale
	f4 += xScale
	f5 += yScale
	f6 += zScale
		
	var field_78247_g: String = null

	if(mirror) {
	  val f7 = f4;
	  f4 = x;
	  x = f7;
	}
	
	val positiontexturevertex = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
	val positiontexturevertex1 = new PositionTextureVertex(f4, y, z, 0.0F, 8.0F);
	val positiontexturevertex2 = new PositionTextureVertex(f4, f5, z, 8.0F, 8.0F);
	val positiontexturevertex3 = new PositionTextureVertex(x, f5, y, 8.0F, 0.0F);
	val positiontexturevertex4 = new PositionTextureVertex(x, y, f6, 0.0F, 0.0F);
	val positiontexturevertex5 = new PositionTextureVertex(f4, y, f6, 0.0F, 8.0F);
	val positiontexturevertex6 = new PositionTextureVertex(f4, f5, f6, 8.0F, 8.0F);
	val positiontexturevertex7 = new PositionTextureVertex(x, f5, f6, 8.0F, 0.0F);
	
	vertexPositions(0) = positiontexturevertex;
	vertexPositions(1) = positiontexturevertex1;
	vertexPositions(2) = positiontexturevertex2;
	vertexPositions(3) = positiontexturevertex3;
	vertexPositions(4) = positiontexturevertex4;
	vertexPositions(5) = positiontexturevertex5;
	vertexPositions(6) = positiontexturevertex6;
	vertexPositions(7) = positiontexturevertex7;
	
	quadList(0) = new TexturedQuad(Array(positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6), texturerOffsetX + depth + width, textureOffsetY + depth, texturerOffsetX + depth + width + depth, textureOffsetY + depth + height, texWidth, texHeigth);
	quadList(1) = new TexturedQuad(Array(positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3), texturerOffsetX, textureOffsetY + depth, texturerOffsetX + depth, textureOffsetY + depth + height, texWidth, texHeigth);
	quadList(2) = new TexturedQuad(Array(positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1), texturerOffsetX + depth, textureOffsetY, texturerOffsetX + depth + width, textureOffsetY + depth, texWidth, texHeigth);
	quadList(3) = new TexturedQuad(Array(positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6), texturerOffsetX + depth + width, textureOffsetY + depth, texturerOffsetX + depth + width + width, textureOffsetY, texWidth, texHeigth);
	quadList(4) = new TexturedQuad(Array(positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2), texturerOffsetX + depth, textureOffsetY + depth, texturerOffsetX + depth + width, textureOffsetY + depth + height, texWidth, texHeigth);
	quadList(5) = new TexturedQuad(Array(positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7), texturerOffsetX + depth + width + depth, textureOffsetY + depth, texturerOffsetX + depth + width + depth + width, textureOffsetY + depth + height, texWidth, texHeigth);

	if(mirror)quadList.foreach(_.flipFace())
	
	def func_78244_a(par1Str:String): AbstractBox = {
		field_78247_g = par1Str;
		return this;
	}
	@SideOnly(Side.CLIENT)
	def render(tessellator:Tessellator) = quadList.foreach(_.draw(tessellator, ratio))
}