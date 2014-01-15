package rikmuld.camping.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.entity.tileentity.TileEntityAntlerThrophy;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.item.itemblock.ItemBlockAntlerThrophy;

public class BlockAntlerThrophy extends BlockMain{

	public BlockAntlerThrophy(String name) 
	{
		super(name, Material.wood, null, ItemBlockAntlerThrophy.class, false);
		this.setHardness(0.5F);
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityAntlerThrophy();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) 
	{
	    ((TileEntityAntlerThrophy)world.getBlockTileEntity(x, y, z)).block = itemStack.getTagCompound().getIntArray("blockCoord");
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityAntlerThrophy tile = (TileEntityAntlerThrophy) world.getBlockTileEntity(x, y, z);
		
		float widthMin, widthMax;
		
		boolean switched = tile.block[3]<2;
		boolean flipped = tile.block[3]%2==0;

		if(switched||flipped)
		{
			widthMin = 0;
			widthMax = 0.0625F;
		}
		else
		{
			widthMin = 1-0.0625F;
			widthMax = 1;
		}
		
		if(!flipped||!switched)this.setBlockBounds(widthMin, 0, 0, widthMax, 1, 1);
		else this.setBlockBounds(0, 0, widthMin, 1, 1, widthMax);
		
		if(tile.block[3]==1)
		{
			widthMin = 1-0.0625F;
			widthMax = 1;
			this.setBlockBounds(0, 0, widthMin, 1, 1, widthMax);
		}
	}
}