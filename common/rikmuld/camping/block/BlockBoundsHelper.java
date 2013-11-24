package rikmuld.camping.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.entity.tileentity.TileEntityBounds;

public class BlockBoundsHelper extends BlockMain{

	public BlockBoundsHelper(String name)
	{
		super(name, Material.cloth);
		this.setCreativeTab(null);
	}
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
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
		return new TileEntityBounds();
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);  

    	if(tile.bounds!=null)
    	{
    		tile.bounds.setBlockBounds(this);
    	}
    }
    
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB alignedBB, List list, Entity entity)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);
    	
    	if(tile.bounds!=null)
    	{
    		tile.bounds.setBlockCollision(this);
    	}
    	
    	super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity);
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);
    	return Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].onBlockActivated(world, tile.baseX, tile.baseY, tile.baseZ, player, par6, par7, par8, par9);
    }
    
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
    	TileEntityBounds tile = (TileEntityBounds) world.getBlockTileEntity(x, y, z);
    	if(world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)!=0)Block.blocksList[world.getBlockId(tile.baseX, tile.baseY, tile.baseZ)].breakBlock(world, tile.baseX, tile.baseY, tile.baseZ, par5, par6);

        super.breakBlock(world, x, y, z, par5, par6);
    }
}
