package rikmuld.camping.client.render.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelHare extends ModelBase {

	ModelRenderer leg1;
	ModelRenderer leg3;
	ModelRenderer earF;
	ModelRenderer tail;
	ModelRenderer leg2;
	ModelRenderer body;
	ModelRenderer head;
	ModelRenderer earR;
	ModelRenderer leg4;
    ModelRenderer head0;
    ModelRenderer head1;
    ModelRenderer head2;
    ModelRenderer head3;
    ModelRenderer head4;
    ModelRenderer head5;
    ModelRenderer head6;
    
	public ModelHare() 
	{
		textureWidth = 64;
		textureHeight = 32;

		  leg1 = new ModelRenderer(this, 0, 8);
	      leg1.addBox(0F, 0F, 0F, 1, 3, 1);
	      leg1.setRotationPoint(-1.95F, 21F, -2.9F);
	      leg1.setTextureSize(64, 32);
	      leg1.mirror = true;
	      setRotation(leg1, 0F, 0F, 0F);
	      leg3 = new ModelRenderer(this, 4, 8);
	      leg3.addBox(0F, 0F, 0F, 1, 2, 1);
	      leg3.setRotationPoint(-1.95F, 22F, 2F);
	      leg3.setTextureSize(64, 32);
	      leg3.mirror = true;
	      setRotation(leg3, 0F, 0F, 0F);
	      earF = new ModelRenderer(this, 20, 0);
	      earF.addBox(0F, 0F, 0F, 1, 4, 1);
	      earF.setRotationPoint(2F, 14.75F, -3.2F);
	      earF.setTextureSize(64, 32);
	      earF.mirror = true;
	      setRotation(earF, 0F, 0F, 0.3490659F);
	      tail = new ModelRenderer(this, 0, 0);
	      tail.addBox(0F, 0F, 0F, 1, 1, 1);
	      tail.setRotationPoint(-0.5F, 21F, 3F);
	      tail.setTextureSize(64, 32);
	      tail.mirror = true;
	      setRotation(tail, 0F, 0F, 0F);
	      leg2 = new ModelRenderer(this, 0, 8);
	      leg2.addBox(0F, 0F, 0F, 1, 3, 1);
	      leg2.setRotationPoint(0.95F, 21F, -2.9F);
	      leg2.setTextureSize(64, 32);
	      leg2.mirror = true;
	      setRotation(leg2, 0F, 0F, 0F);
	      body = new ModelRenderer(this, 0, 0);
	      body.addBox(0F, 0F, 0F, 4, 2, 6);
	      body.setRotationPoint(-2F, 19.7F, -2.7F);
	      body.setTextureSize(64, 32);
	      body.mirror = true;
	      setRotation(body, -0.1858931F, 0F, 0F);
	      head = new ModelRenderer(this, 2, 4);
	      head.addBox(0F, 0F, 0F, 1, 1, 0);
	      head.setRotationPoint(-0.5F, 20.3F, -6.1F);
	      head.setTextureSize(64, 32);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      earR = new ModelRenderer(this, 20, 0);
	      earR.addBox(0F, 0F, 0F, 1, 4, 1);
	      earR.setRotationPoint(-3F, 15F, -3.2F);
	      earR.setTextureSize(64, 32);
	      earR.mirror = true;
	      setRotation(earR, 0F, 0F, -0.3490659F);
	      leg4 = new ModelRenderer(this, 4, 8);
	      leg4.addBox(0F, 0F, 0F, 1, 2, 1);
	      leg4.setRotationPoint(0.95F, 22F, 2F);
	      leg4.setTextureSize(64, 32);
	      leg4.mirror = true;
	      setRotation(leg4, 0F, 0F, 0F);
	      head0 = new ModelRenderer(this, 0, 13);
	      head0.addBox(0F, 0F, 0F, 4, 4, 4);
	      head0.setRotationPoint(-2F, 18F, -6F);
	      head0.setTextureSize(64, 32);
	      head0.mirror = true;
	      setRotation(head0, 0F, 0F, 0F);
	      head1 = new ModelRenderer(this, 0, 2);
	      head1.addBox(0F, 0F, 0F, 2, 1, 0);
	      head1.setRotationPoint(1.8F, 19.3F, -5.6F);
	      head1.setTextureSize(64, 32);
	      head1.mirror = true;
	      setRotation(head1, 0F, 0F, 0F);
	      head2 = new ModelRenderer(this, 0, 4);
	      head2.addBox(0F, 0F, 0F, 1, 1, 0);
	      head2.setRotationPoint(0.8F, 18.9F, -6.1F);
	      head2.setTextureSize(64, 32);
	      head2.mirror = true;
	      setRotation(head2, 0F, 0F, 0F);
	      head3 = new ModelRenderer(this, 0, 4);
	      head3.addBox(0F, 0F, 0F, 1, 1, 0);
	      head3.setRotationPoint(-1.8F, 18.9F, -6.1F);
	      head3.setTextureSize(64, 32);
	      head3.mirror = true;
	      setRotation(head3, 0F, 0F, 0F);
	      head4 = new ModelRenderer(this, 0, 2);
	      head4.addBox(0F, 0F, 0F, 2, 1, 0);
	      head4.setRotationPoint(-3.8F, 19.3F, -5.6F);
	      head4.setTextureSize(64, 32);
	      head4.mirror = true;
	      setRotation(head4, 0F, 0F, 0F);
	      head5 = new ModelRenderer(this, 0, 2);
	      head5.addBox(0F, 0F, 0F, 2, 1, 0);
	      head5.setRotationPoint(1.8F, 20.6F, -5.6F);
	      head5.setTextureSize(64, 32);
	      head5.mirror = true;
	      setRotation(head5, 0F, 0F, 0.0174533F);
	      head6 = new ModelRenderer(this, 0, 2);
	      head6.addBox(0F, 0F, 0F, 2, 1, 0);
	      head6.setRotationPoint(-3.8F, 20.6F, -5.6F);
	      head6.setTextureSize(64, 32);
	      head6.mirror = true;
	      setRotation(head6, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		
		if(this.isChild)GL11.glScalef(0.5F, 0.5F, 0.5F);

	    leg1.render(f5);
	    leg3.render(f5);
	    earF.render(f5);
	    tail.render(f5);
	    leg2.render(f5);
	    body.render(f5);
	    head.render(f5);
	    earR.render(f5);
	    leg4.render(f5);
	    head0.render(f5);
	    head1.render(f5);
	    head2.render(f5);
	    head3.render(f5);
	    head4.render(f5);
	    head5.render(f5);
	    head6.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) 
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
	}
}
