package rikmuld.camping.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import rikmuld.camping.entity.tileentity.TileEntityRotation;

public class BlockRotationMain extends BlockMain {

	public BlockRotationMain(String name, Material material, String[] meta, Class<? extends ItemBlock> itemBlock, boolean side)
	{
		super(name, material, meta, itemBlock, side);
	}

	public BlockRotationMain(String name, Material material, boolean side)
	{
		super(name, material, side);
	}

	public BlockRotationMain(String name, Material material)
	{
		super(name, material);
	}

	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) 
    {
	    int direction = 0;
	    int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	
	    if (facing == 0) direction = ForgeDirection.NORTH.ordinal()-2;	    
	    else if (facing == 1) direction = ForgeDirection.SOUTH.ordinal()-2;
	    else if (facing == 2) direction = ForgeDirection.WEST.ordinal()-2;
	    else if (facing == 3) direction = ForgeDirection.EAST.ordinal()-2;

	    ((TileEntityRotation)world.getBlockTileEntity(x, y, z)).setRotation(direction);
    }

	public void wrenchClick(World world, EntityPlayer player, int x, int y, int z)
	{
	    ((TileEntityRotation)world.getBlockTileEntity(x, y, z)).cycleRotation();
	}

	@Override
	public TileEntityRotation createNewTileEntity(World world)
	{
		return new TileEntityRotation();
	}
}
