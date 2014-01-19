package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.BlockUtil;
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
    		player.addChatMessage("Meta: "+world.getBlockMetadata(x, y, z));
    		
    		if(world.getBlockMetadata(x, y, z)<1)return false;
    		
    		TileEntityBerry tile = (TileEntityBerry) world.getBlockTileEntity(x, y, z);
    		if(tile.berries==true)
    		{
				if(world.getBlockMetadata(x, y, z)==1)ModAchievements.berry2.addStatToPlayer(player);
				else ModAchievements.berry1.addStatToPlayer(player);

    			ItemStackUtil.dropItemsInWorld(new ItemStack[]{new ItemStack(ModItems.berries.itemID, tile.rand.nextInt(3)+1, world.getBlockMetadata(x, y, z)-1)}, world, x, y-1, z);
    			
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
    
    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }
    
    @Override
    public int idDropped(int par1, Random random, int par3)
    {
    	return ModBlocks.sapling.blockID;
    }
    
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
    	if(entity instanceof EntityPlayer)((TileEntityBerry)world.getBlockTileEntity(x, y, z)).noDecay = true;
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
    	if(!world.isRemote&&!(((TileEntityBerry)world.getBlockTileEntity(x, y, z)).getIsPlacedByHand()))
    	{	
    		this.triggerDecay(world, x, y, z, id);
    	}
    }
    
    public void triggerDecay(World world, int x, int y, int z, int id)
    {
    	if(id==Block.wood.blockID)
		{
    		int[][] blocks = BlockUtil.getBlocks(world, x, y, z);
    		
    		boolean hasWoodOnSide = false;
    		
    		for(int i = 0; i<6; i++)
    		{
    			if(blocks[i][0]==Block.wood.blockID)hasWoodOnSide = true;
    		}
    		
    		if(!hasWoodOnSide)((TileEntityBerry)world.getBlockTileEntity(x, y, z)).spreadDecay();
		}
		
		boolean hasGoodLeafOnSide = false;

		if(world.getBlockTileEntity(x+1, y, z)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x+1, y, z)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		if(world.getBlockTileEntity(x-1, y, z)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x-1, y, z)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		if(world.getBlockTileEntity(x, y, z+1)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x, y, z+1)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		if(world.getBlockTileEntity(x, y, z-1)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x, y, z-1)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		if(world.getBlockTileEntity(x, y+1, z)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x, y+1, z)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		if(world.getBlockTileEntity(x, y-1, z)instanceof TileEntityBerry&&!((TileEntityBerry)world.getBlockTileEntity(x, y-1, z)).getIsPlacedByHand())hasGoodLeafOnSide = true;
		
		if(!hasGoodLeafOnSide)((TileEntityBerry)world.getBlockTileEntity(x, y, z)).startDecay();
    }
}
