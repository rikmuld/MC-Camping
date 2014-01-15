package rikmuld.camping.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import rikmuld.camping.core.lib.BlockInfo;

public class ItemBlockAntlerThrophy extends ItemBlockMain{

	int blockID;
	NBTTagCompound flagTag;
	
	public ItemBlockAntlerThrophy(int id) 
	{
		super(id, BlockInfo.THROPHY, false);
		this.blockID = BlockInfo.id(BlockInfo.THROPHY);
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {                
        if(!world.isBlockOpaqueCube(x, y, z)||side<2)return false;
       
        this.flagTag = stack.getTagCompound();
        
        if(!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setIntArray("blockCoord", new int[]{x, y, z, ForgeDirection.OPPOSITES[side]-2});
        
        return super.onItemUse(stack, player, world, x, y, z, side, xHit, yHit, zHit);
    }
	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (!world.setBlock(x, y, z, this.blockID, metadata, 3))
       {
    	   stack.setTagCompound(flagTag);
           return false;
       }

       if (world.getBlockId(x, y, z) == this.blockID)
       {
           Block.blocksList[this.blockID].onBlockPlacedBy(world, x, y, z, player, stack);
           Block.blocksList[this.blockID].onPostBlockPlaced(world, x, y, z, metadata);
       }

	   stack.setTagCompound(flagTag);
       return true;
    }
}
