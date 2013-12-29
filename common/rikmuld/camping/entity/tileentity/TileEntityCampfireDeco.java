package rikmuld.camping.entity.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityCampfireDeco extends TileEntityInventory{

	public int color = 16;
	public int oldTime;
	public int time;
	
	Random rand = new Random();

	public float[][] coals = new float[3][20];
	
	public PotionEffect[] effectsOrderd = new PotionEffect[16];
	public PotionEffect[] effectsRaw = new PotionEffect[23];

	public TileEntityCampfireDeco()
	{	
		for(int i = 0; i<23; i++)
		{
			effectsRaw[i] = new PotionEffect(i, 400, 0);
		}
		
		effectsOrderd[0] = effectsRaw[20];
		effectsOrderd[1] = effectsRaw[5];
		effectsOrderd[2] = effectsRaw[19];
		effectsOrderd[3] = effectsRaw[11];
		effectsOrderd[4] = effectsRaw[16];
		effectsOrderd[5] = effectsRaw[9];
		effectsOrderd[6] = effectsRaw[1];
		effectsOrderd[7] = effectsRaw[18];
		effectsOrderd[8] = effectsRaw[15];
		effectsOrderd[9] = effectsRaw[10];
		effectsOrderd[10] = effectsRaw[17];
		effectsOrderd[11] = effectsRaw[3];
		effectsOrderd[12] = effectsRaw[13];
		effectsOrderd[13] = effectsRaw[10];
		effectsOrderd[14] = effectsRaw[12];
		effectsOrderd[15] = effectsRaw[14];

		for(int i = 0; i<20; i++)
		{
			coals[0][i] = rand.nextFloat()/5F;
			coals[1][i] = rand.nextFloat()/5F;
			coals[2][i] = rand.nextFloat()*360;
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			this.oldTime = time;
			if(this.getStackInSlot(0)!=null&&time==0)
			{
				colorFlame(this.getStackInSlot(0).getItemDamage());
				time = 6000;
				this.decrStackSize(0, 1);
			}
			if(time!=0)time--;
			if(color!=16&&time==0)this.colorFlame(16);	
						
			if(oldTime!=time)this.sendTileData(1, true, time);	
			if(time>0&&time%120==0)
			{
				ArrayList<EntityLivingBase> entitys = (ArrayList<EntityLivingBase>) worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getAABBPool().getAABB(xCoord-8, yCoord-8, zCoord-8, xCoord+8, yCoord+8, zCoord+8));
				for(EntityLivingBase entity:entitys)
				{
					entity.addPotionEffect(new PotionEffect(effectsOrderd[color]));
				}
			}
		}
	}
	
	private void colorFlame(int color)
	{
		this.color = color;
		if(!worldObj.isRemote) this.sendTileData(0, true, color);
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)this.colorFlame(data[0]);
		if(id==1)this.time = data[0];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		color = tag.getInteger("color");
		time = tag.getInteger("time");
		
		for(int i = 0; i<coals.length; i++)
		{
			for(int j = 0; j<coals[i].length; j++)
			{
				coals[i][j]=tag.getFloat("coals"+i+j);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("color", color);
		tag.setInteger("time", time);
		
		for(int i = 0; i<coals.length; i++)
		{
			for(int j = 0; j<coals[i].length; j++)
			{
				tag.setFloat("coals"+i+j, coals[i][j]);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
	}
}
