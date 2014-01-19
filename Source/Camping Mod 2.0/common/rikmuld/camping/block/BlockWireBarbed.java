package rikmuld.camping.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModDamageSources;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModPotions;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityBarbedWire;
import rikmuld.camping.item.armor.ItemArmorFur;

public class BlockWireBarbed extends BlockMain {

	Random random = new Random();
	
	public BlockWireBarbed(String name) 
	{
		super(name, Material.iron);
		this.setHardness(1.5F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);
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
		return new TileEntityBarbedWire();
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityBarbedWire tile = (TileEntityBarbedWire) world.getBlockTileEntity(x, y, z);
		
		float maxMin = 0.59375F;
		float minMin = 0.40625F;
		
		setBlockBounds(tile.sides[ForgeDirection.WEST.ordinal()-2]? 0:minMin, 0.21875F, tile.sides[ForgeDirection.NORTH.ordinal()-2]? 0:minMin, tile.sides[ForgeDirection.EAST.ordinal()-2]? 1:maxMin, 0.40625F, tile.sides[ForgeDirection.SOUTH.ordinal()-2]? 1:maxMin);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
		if(player.getCurrentEquippedItem().itemID==ModItems.knife.itemID)
		{
			ItemStackUtil.dropItemInWorld(new ItemStack(ModBlocks.wireBarbed), world, x, y, z);
			world.setBlock(x, y, z, 0);
			
			return true;
		}
		return false;
    }
    
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
    	player.attackEntityFrom(ModDamageSources.barbedWire, 3);
    	
    	PotionEffect effect = new PotionEffect(ModPotions.bleeding.id, 125, 1);
		effect.getCurativeItems().clear();
		player.addPotionEffect(effect);
    }
    
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {       
        if(entity instanceof EntityLivingBase)
        {
        	if(!world.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(x+minX, y+minY, z+minZ, x+maxX, y+maxY, z+maxZ)))
        	{
        		if(entity instanceof EntityPlayer)
        		{
        			EntityPlayer player = (EntityPlayer) entity;
        			
        			int flag = 0;
        			
        			if(player.getCurrentItemOrArmor(1)!=null&&player.getCurrentItemOrArmor(1).getItem() instanceof ItemArmorFur)flag++;
        			if(player.getCurrentItemOrArmor(2)!=null&&player.getCurrentItemOrArmor(2).getItem() instanceof ItemArmorFur)flag++;
        			if(player.getCurrentItemOrArmor(3)!=null&&player.getCurrentItemOrArmor(3).getItem() instanceof ItemArmorFur)flag++;
        			if(player.getCurrentItemOrArmor(4)!=null&&player.getCurrentItemOrArmor(4).getItem() instanceof ItemArmorFur)flag++;

        			if(flag>1)return;
        		}
        		
	            entity.setInWeb();
	
	            if(random.nextInt(50)==0)
	            {
			        PotionEffect effect = new PotionEffect(ModPotions.bleeding.id, 75, 2);
					effect.getCurativeItems().clear();
					((EntityLivingBase)entity).addPotionEffect(effect);
	            }
        	}
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) 
    {
    	this.checkSide(world, x, y, z);	
    	if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
    	this.checkSide(world, x, y, z);
    	if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
    }
    
    public void checkSide(World world, int x, int y, int z)
    {
    	TileEntityBarbedWire tile = (TileEntityBarbedWire) world.getBlockTileEntity(x, y, z);
    	int[][] blocks = BlockUtil.getBlocks(world, x, y, z);

    	tile.sides[0] = world.isBlockOpaqueCube(x, y, z-1)||blocks[2][0]==this.blockID;
    	tile.sides[1] = world.isBlockOpaqueCube(x, y, z+1)||blocks[3][0]==this.blockID;
    	tile.sides[2] = world.isBlockOpaqueCube(x-1, y, z)||blocks[4][0]==this.blockID;
    	tile.sides[3] = world.isBlockOpaqueCube(x+1, y, z)||blocks[5][0]==this.blockID;
    	
    	world.markBlockForUpdate(x, y, z);
    	world.markBlockForRenderUpdate(x, y, z);
    }
    
	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.blocksList[Block.fenceIron.blockID].getIcon(0, 0);
	}
	
	public void dropIfCantStay(World world, int x, int y, int z)
	{
    	((TileEntityBarbedWire)world.getBlockTileEntity(x, y, z)).isBase = world.isBlockOpaqueCube(x-1, y, z)||world.isBlockOpaqueCube(x+1, y, z)||world.isBlockOpaqueCube(x, y, z+1)||world.isBlockOpaqueCube(x, y, z-1);

		if(!((TileEntityBarbedWire)world.getBlockTileEntity(x, y, z)).canStay(null, 5))
		{
			for(ItemStack item: this.getBlockDropped(world, x, y, z, 0, 1))this.dropBlockAsItem_do(world, x, y, z, item);
			world.setBlock(x, y, z, 0);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
    	this.checkSide(world, x, y, z);
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
    	boolean base = world.isBlockOpaqueCube(x-1, y, z)||world.isBlockOpaqueCube(x+1, y, z)||world.isBlockOpaqueCube(x, y, z+1)||world.isBlockOpaqueCube(x, y, z-1);
    	boolean wire = world.getBlockId(x-1, y, z)==blockID||world.getBlockId(x+1, y, z)==blockID||world.getBlockId(x, y, z+1)==blockID||world.getBlockId(x, y, z-1)==blockID;

        return super.canPlaceBlockAt(world, x, y, z)&&(base|wire);
    }
}