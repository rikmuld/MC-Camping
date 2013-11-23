package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityBerry;
import rikmuld.camping.item.itemblock.ItemBlockBerryLeaves;

public class BlockBerryLeaves extends BlockMain implements IShearable{

	public BlockBerryLeaves(String name)
	{
		super(name, Material.leaves, ItemBlockBerryLeaves.metadataNames, ItemBlockBerryLeaves.class, false);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setStepSound(soundGrassFootstep);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityBerry();
	}
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, 0));
        return ret;
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z)
    {
        return true;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	return true;
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
    	if(!world.isRemote)
    	{
    		if(world.getBlockMetadata(x, y, z)<1)return false;
    		
    		TileEntityBerry tile = (TileEntityBerry) world.getBlockTileEntity(x, y, z);
    		if(tile.berries==true)
    		{
    			ItemStackUtil.dropItemsInWorld(new ItemStack[]{new ItemStack(ModItems.berries.itemID, tile.rand.nextInt(3)+1, world.getBlockMetadata(x, y, z)-1)}, world, x, y, z);
    			
    			tile.getBerries();
    			return true;
    		}
    		return false;
    	}
    	return true;
    }
    
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
    	this.onBlockActivated(world, x, y, z, player, 0, 0, 0, 0);
    }
    
    @Override
	public Icon getIcon(int side, int metadata)
	{
		blockIcon = iconBuffer[0][0];
		return this.blockIcon;
	}
}
