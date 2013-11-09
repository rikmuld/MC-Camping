package rikmuld.camping.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.entity.EntityMountableBlock;
import rikmuld.camping.entity.tileentity.TileEntityLog;

public class BlockLog extends BlockRotationMain{

	public BlockLog(String name)
	{
		super(name, Material.wood);
		setStepSound(soundWoodFootstep);
		this.setHardness(2.0F);
		MinecraftForge.setBlockHarvestLevel(this, "axe", 0);
	}
	
	public boolean isBlockReplaceable(World world, int x, int y, int z)
	{
		return false;
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
	public TileEntity createTileEntity(World world, int meta)
	{
		return new TileEntityLog();
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.blocksList[Block.wood.blockID].getIcon(0, 0);
	}
	
    @Override
    public boolean isWood(World world, int x, int y, int z)
    {
        return true;
    }
    
    @Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity);
	}
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{   	
    	if(world.isRemote&&!player.isRiding()&&Vec3.createVectorHelper(x+0.5F, y+0.5F, z+0.5F).distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))<=2.5F)((TileEntityLog)world.getBlockTileEntity(x, y, z)).mountable.interactFirst(player);
    	return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		boolean flag = false;
		TileEntityLog log = (TileEntityLog) world.getBlockTileEntity(x, y, z);
		
		int meta = (log.rotation+1)%2;
		
		int[][] blockInfo = BlockUtil.getBlocks((World) world, x, y, z);

		float xMin = 0.3125F, yMin = 0, zMin = 0.3125F;
		float xMax = 0.6875F, yMax = 0.375F, zMax = 0.6875F;

		if(meta==0)
		{
			zMax = 0.875F;
			zMin = 0.125F;
			
			if(blockInfo[ForgeDirection.NORTH.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(x, y, z-1)).rotation+1)%2==0) zMin = 0;
			if(blockInfo[ForgeDirection.SOUTH.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(x, y, z+1)).rotation+1)%2==0) zMax = 1;
		}
		
		if(meta==1)
		{
			xMax = 0.875F;
			xMin = 0.125F;

			if(blockInfo[ForgeDirection.WEST.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(x-1, y, z)).rotation+1)%2==1) xMin = 0;
			if(blockInfo[ForgeDirection.EAST.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(x+1, y, z)).rotation+1)%2==1) xMax = 1;
		}		
		this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
	}
}
