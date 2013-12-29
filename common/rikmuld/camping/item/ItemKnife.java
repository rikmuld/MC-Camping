package rikmuld.camping.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.entity.tileentity.TileEntityTent;

public class ItemKnife extends ItemMain implements IKnife {

	public ItemKnife(String name)
	{
		super(name);
		this.setHasSubtypes(true);
		this.setMaxDamage(250);
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

		if(movingobjectposition==null)
		{
			return stack;
		}

		if(stack.getItemDamage()<this.getMaxDamage()&&!world.isRemote)
		{
			int x = movingobjectposition.blockX;
			int y = movingobjectposition.blockY;
			int z = movingobjectposition.blockZ;

			int[] ids = new int[]{Block.stairsBrick.blockID, Block.stairsCobblestone.blockID, Block.stairsNetherBrick.blockID, Block.stairsNetherQuartz.blockID, Block.stairsSandStone.blockID,
					Block.stairsWoodBirch.blockID, Block.stairsWoodJungle.blockID, Block.stairsWoodOak.blockID, Block.stairsWoodSpruce.blockID, Block.chest.blockID, Block.chestTrapped.blockID,
					Block.wood.blockID, Block.pumpkin.blockID, Block.pumpkinLantern.blockID, Block.dispenser.blockID, Block.dropper.blockID, Block.pistonBase.blockID, Block.pistonStickyBase.blockID,
					Block.enderChest.blockID, Block.anvil.blockID, Block.signPost.blockID, Block.redstoneComparatorIdle.blockID, Block.redstoneComparatorActive.blockID,
					Block.redstoneRepeaterIdle.blockID, Block.redstoneRepeaterActive.blockID, Block.furnaceIdle.blockID, Block.furnaceBurning.blockID, Block.fenceGate.blockID};

			for(int i : ids)
			{
				if(world.getBlockId(x, y, z)==i)
				{
					if(i==Block.wood.blockID) world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+4, 2);
					else if(i==Block.chest.blockID||i==Block.chestTrapped.blockID||i==Block.enderChest.blockID||i==Block.furnaceBurning.blockID||i==Block.furnaceIdle.blockID)
					{
						if(world.getBlockMetadata(x, y, z)>4) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
						else world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);
					}
					else if(i==Block.pumpkin.blockID||i==Block.pumpkinLantern.blockID)
					{
						if(world.getBlockMetadata(x, y, z)>2) world.setBlockMetadataWithNotify(x, y, z, 0, 2);
						else world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);
					}
					else if(i==Block.dispenser.blockID||i==Block.dropper.blockID||i==Block.pistonBase.blockID||i==Block.pistonStickyBase.blockID)
					{
						if(world.getBlockMetadata(x, y, z)>4) world.setBlockMetadataWithNotify(x, y, z, 0, 2);
						else world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);

						if(i==Block.pistonBase.blockID||i==Block.pistonStickyBase.blockID)
						{
							int meta = world.getBlockMetadata(x, y, z);

							world.setBlock(x, y, z, 0);
							world.notifyBlocksOfNeighborChange(x, y, z, i);

							world.setBlock(x, y, z, i, meta, 2);
							world.notifyBlockOfNeighborChange(x, y, z, i);
						}
					}
					else if(i==Block.anvil.blockID)
					{
						int meta = world.getBlockMetadata(x, y, z);

						if(meta==0||meta%2==0) world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
						else world.setBlockMetadataWithNotify(x, y, z, meta-1, 2);
					}
					else if(i==Block.fenceGate.blockID||i==Block.redstoneRepeaterActive.blockID||i==Block.redstoneRepeaterIdle.blockID||i==Block.redstoneComparatorActive.blockID
							||i==Block.redstoneComparatorIdle.blockID)
					{
						int meta = world.getBlockMetadata(x, y, z);

						if(meta==3||meta%4==3) world.setBlockMetadataWithNotify(x, y, z, meta-3, 2);
						else world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
					}
					else
					{
						world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);
					}
					
					if(!player.capabilities.isCreativeMode)
					{
						stack = ItemStackUtil.addDamage(stack, 1);
					}
					
					return stack;
				}
				
				if(world.getBlockTileEntity(x, y, z) instanceof TileEntityRotation&&!(world.getBlockTileEntity(x, y, z) instanceof TileEntityTent))
				{
					((TileEntityRotation)world.getBlockTileEntity(x, y, z)).cycleRotation();
					world.markBlockForRenderUpdate(x, y, z);
					world.markBlockForUpdate(x, y, z);
					
					if(!player.capabilities.isCreativeMode)
					{
						stack = ItemStackUtil.addDamage(stack, 1);
					}
					
					return stack;
				}
			}
		}
		return stack;
	}
}
