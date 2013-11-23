package rikmuld.camping.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.entity.tileentity.TileEntityBounds;

public class BlockBoundsHelper extends BlockMain{

	public BlockBoundsHelper(String name)
	{
		super(name, Material.air);
		this.setCreativeTab(null);
	}
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityBounds();
	}
	
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);   	
    	tile.bounds.setBlockBounds(this);
    }
    
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB alignedBB, List list, Entity entity)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);
    	tile.bounds.setBlockCollision(this);
    }
}
