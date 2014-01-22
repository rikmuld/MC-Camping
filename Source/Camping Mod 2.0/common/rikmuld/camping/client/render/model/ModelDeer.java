package rikmuld.camping.client.render.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.MathHelper;

public class ModelDeer extends ModelBase {

	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg4;
	ModelRenderer leg3;
	ModelRenderer body;
	ModelRenderer head;
	ModelRenderer earL;
	ModelRenderer earR;
	ModelRenderer HL;
	ModelRenderer HR;
	ModelRenderer tail;
	ModelRenderer neck;

	public ModelDeer() 
	{
		textureWidth = 128;
		textureHeight = 64;

		leg1 = new ModelRenderer(this, 0, 45);
	    leg1.addBox(0F, 0F, 0F, 3, 13, 3);
	    leg1.setRotationPoint(2F, 11F, -12F);
	    leg1.setTextureSize(128, 64);
	    leg1.mirror = true;
	    setRotation(leg1, 0F, 0F, 0F);
	    leg2 = new ModelRenderer(this, 0, 29);
	    leg2.addBox(0F, 0F, 0F, 3, 13, 3);
	    leg2.setRotationPoint(-5F, 11F, 7F);
	    leg2.setTextureSize(128, 64);
	    leg2.mirror = true;
	    setRotation(leg2, 0F, 0F, 0F);
	    leg4 = new ModelRenderer(this, 0, 29);
	    leg4.addBox(0F, 0F, 0F, 3, 13, 3);
	    leg4.setRotationPoint(-5F, 11F, -12F);
	    leg4.setTextureSize(128, 64);
	    leg4.mirror = true;
	    setRotation(leg4, 0F, 0F, 0F);
	    leg3 = new ModelRenderer(this, 0, 45);
	    leg3.addBox(0F, 0F, 0F, 3, 13, 3);
	    leg3.setRotationPoint(2F, 11F, 7F);
	    leg3.setTextureSize(128, 64);
	    leg3.mirror = true;
	    setRotation(leg3, 0F, 0F, 0F);
		body = new ModelRenderer(this, 12, 10);
		body.addBox(0F, 0F, 0F, 8, 10, 25);
		body.setRotationPoint(-4F, 4F, -13F);
		body.setTextureSize(128, 64);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(0F, 0F, 0F, 6, 6, 10);
		head.setRotationPoint(-3F, -5F, -25.5F);
		head.setTextureSize(128, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		earL = new ModelRenderer(this, 6, 23);
	    earL.addBox(0F, 0F, 0F, 3, 5, 1);
	    earL.setRotationPoint(-6F, -6F, -19F);
	    earL.setTextureSize(128, 64);
	    earL.mirror = true;
	    setRotation(earL, 0F, 0F, -0.7853982F);
	    earR = new ModelRenderer(this, 6, 17);
	    earR.addBox(0F, 0F, 0F, 3, 5, 1);
	    earR.setRotationPoint(4F, -8F, -19F);
	    earR.setTextureSize(128, 64);
	    earR.mirror = true;
	    setRotation(earR, 0F, 0F, 0.7853982F);
		HL = new ModelRenderer(this, 53, 11);
		HL.addBox(0F, 0F, 0F, 1, 16, 8);
		HL.setRotationPoint(-6F, -20F, -21F);
		HL.setTextureSize(128, 64);
		HL.mirror = true;
		setRotation(HL, 0F, 0F, -0.2094395F);
		HR = new ModelRenderer(this, 53, 11);
		HR.addBox(0F, 0F, 0F, 1, 16, 8);
		HR.setRotationPoint(6F, -20F, -21F);
		HR.setTextureSize(128, 64);
		HR.mirror = true;
		setRotation(HR, 0F, 0F, 0.2094395F);
		tail = new ModelRenderer(this, 0, 23);
		tail.addBox(0F, 0F, 0F, 2, 5, 1);
		tail.setRotationPoint(-1F, 5F, 11F);
		tail.setTextureSize(128, 64);
		tail.mirror = true;
		setRotation(tail, 0.4363323F, 0F, 0F);
		neck = new ModelRenderer(this, 19, 16);
		neck.addBox(0F, 0F, 0F, 4, 14, 5);
		neck.setRotationPoint(-2F, 1F, -20F);
		neck.setTextureSize(128, 64);
		neck.mirror = true;
		setRotation(neck, 0.5235988F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		
		if(this.isChild)GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		leg1.render(f5);
		leg2.render(f5);
		leg4.render(f5);
		leg3.render(f5);
		body.render(f5);
		head.render(f5);
		earL.render(f5);
		earR.render(f5);
		HL.render(f5);
		HR.render(f5);
		tail.render(f5);
		neck.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
	{
        float f6 = (180F / (float)Math.PI);
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
