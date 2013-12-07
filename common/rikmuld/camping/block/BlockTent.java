package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IShearable;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.register.ModStructures;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityBerry;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.item.itemblock.ItemBlockBerryLeaves;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.misc.bounds.BoundsTracker;

public class BlockTent extends BlockRotationMain {
		
	public BlockTent(String name)
	{
		super(name, Material.sponge, false);
		this.setHardness(0.2F);
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
	public TileEntityRotation createNewTileEntity(World world)
	{
		return new TileEntityTent();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{	
		if(player.getCurrentEquippedItem()!=null&&((TileEntityTent) world.getBlockTileEntity(x, y, z)).addContends(player.getCurrentEquippedItem()))
		{
			if(!world.isRemote)
			{
				player.getCurrentEquippedItem().stackSize--;
				if(player.getCurrentEquippedItem().stackSize<0)ItemStackUtil.setCurrentPlayerItem(player, null);
			}
		}	
		else
		{
			player.openGui(CampingMod.instance, GuiInfo.GUI_TENT, world, x, y, z);
		}
		return true;
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		TileEntityTent tile = (TileEntityTent) world.getBlockTileEntity(x, y, z);   	
		tile.bounds[tile.rotation].setBlockBounds(this);
    }

    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB alignedBB, List list, Entity entity)
    {
		TileEntityTent tile = (TileEntityTent) world.getBlockTileEntity(x, y, z);   	
    	tile.bounds[tile.rotation].setBlockCollision(this);
    	
    	super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity);
    }
    
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
		TileEntityTent tile = (TileEntityTent) world.getBlockTileEntity(x, y, z);   	
	
		if(tile!=null)
		{
			tile.structures[tile.rotation].destroyStructure(world, tile.tracker[tile.rotation]);
	        super.breakBlock(world, x, y, z, par5, par6);
	        
	        if(!tile.dropped)
			{
	        	ArrayList<ItemStack> stacks = getDroppes(world, x, y, z, world.getBlockMetadata(x, y, z), 1);
	        	stacks.addAll(tile.getContends());
	        	
		    	for(ItemStack stack:stacks)
				{
					this.dropBlockAsItem_do(world, x, y, z, stack);
				}
		    	tile.dropped = true;
			}	
		}
	
        world.setBlock(x, y, z, 0);
    }
    
    @Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.cloth.getIcon(side, metadata);
	}
    
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {		
        int l = world.getBlockId(x, y, z);
        Block block = Block.blocksList[l];
        
        int direction = 0;
	    int facing = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
	
	    if (facing == 0) direction = ForgeDirection.NORTH.ordinal()-2;	    
	    else if (facing == 1) direction = ForgeDirection.SOUTH.ordinal()-2;
	    else if (facing == 2) direction = ForgeDirection.WEST.ordinal()-2;
	    else if (facing == 3) direction = ForgeDirection.EAST.ordinal()-2;
	    
        return (block == null || block.isBlockReplaceable(world, x, y, z))&&ModStructures.tent[direction].canBePlaced(world, new BoundsTracker(x, y, z, TileEntityTent.bounds[direction]));
    }
	
	public void dropIfCantStay(World world, int x, int y, int z)
	{
		TileEntityTent tile = (TileEntityTent) world.getBlockTileEntity(x, y, z);   	

		if(!tile.structures[tile.rotation].hadSolitUnderGround(world, tile.tracker[tile.rotation]))
		{
			this.breakBlock(world, x, y, z, 0, 0);
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}
	
	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntityTent tile = (TileEntityTent) world.getBlockTileEntity(x, y, z);
		return tile.lanternDamage==0&&tile.lanterns>0? 15:0;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
    public ArrayList<ItemStack> getDroppes(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = 1;
        for(int i = 0; i < count; i++)
        {
            int id = idDropped(metadata, world.rand, fortune);
            if (id > 0)
            {
                ret.add(new ItemStack(id, 1, damageDropped(metadata)));
            }
        }
        return ret;
    }
}
