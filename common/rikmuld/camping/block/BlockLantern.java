package rikmuld.camping.block;

import java.util.List;
import java.util.Random;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityLantern;
import rikmuld.camping.item.itemblock.ItemBlockLantern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockLantern extends BlockMain{

	int burnTime;
	
	public BlockLantern(String name)
	{
		super(name, Material.glass, ItemBlockLantern.metadataNames, ItemBlockLantern.class, false);
		setBlockBounds(0.3F, 0, 0.3F, 0.7F, 0.9F, 0.7F);
	}
	
	@Override
	public int getRenderType()
	{
		return 1;
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
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityLantern();
	}
	
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if(!world.isRemote)
		{
			if(world.getBlockMetadata(x, y, z)==1&&player.getCurrentEquippedItem().itemID==Item.glowstone.itemID)
			{
				world.setBlockMetadataWithNotify(x, y, z, 0, 2); 
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y, z);
				
				((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime = 1500;
				
				return true;
			}
		}
		return false;
	}
	
	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z)==0? 15:0;
	}
	
	@Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
    	this.burnTime = world.getBlockTileEntity(x, y, z)!=null? ((TileEntityLantern)world.getBlockTileEntity(x, y, z)).burnTime:0;
		super.breakBlock(world, x, y, z, par5, par6);
    }
	
	@Override
	protected void dropBlockAsItem_do(World world, int x, int y, int z, ItemStack stack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {        	
        	stack.setTagCompound(new NBTTagCompound());
        	stack.getTagCompound().setInteger("time", burnTime);
        	
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
	
	public void dropIfCantStay(World world, int x, int y, int z)
	{
		if(!BlockUtil.isTouchableBlockPartitionalSolidForSideAndHasCorrectBounds(world, x, y, z, 0, 1))
		{
			for(ItemStack item: this.getBlockDropped(world, x, y, z, 0, 1))
			{
				this.dropBlockAsItem_do(world, x, y, z, item);
			}
			this.breakBlock(world, x, y, z, blockID, 0);
			world.setBlock(x, y, z, 0);
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
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        int l = world.getBlockId(x, y, z);
        Block block = Block.blocksList[l];
        return (block == null || block.isBlockReplaceable(world, x, y, z))&&BlockUtil.isTouchableBlockPartitionalSolidForSideAndHasCorrectBounds(world, x, y, z, 0, 1);
    }
}
