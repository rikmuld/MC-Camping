package rikmuld.camping.block.plant;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.BonemealEvent;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;

public class BlockFlowerHemp extends BlockFlowerMain {

	public BlockFlowerHemp(String name)
	{
		super(name, Material.plants);
		this.setGrowStates(5);
		this.setHardness(0.0F);
		this.disableStats();
	}

	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);

		if(world.getBlockLightValue(x, y+1, z)>=9)
		{
			int l = world.getBlockMetadata(x, y, z);

			if(l<3)
			{
				float f = this.getGrowthRate(world, x, y, z);

				if(random.nextInt((int) (25.0F/f)+1)==0)
				{
					++l;
					world.setBlockMetadataWithNotify(x, y, z, l, 2);
				}
			}
			if(l==3)
			{
				if(world.getBlockMetadata(x, y, z)==3)
				{
					float f = this.getGrowthRate(world, x, y, z);

					if(random.nextInt((int) (25.0F/f)+1)==0)
					{
						if(world.getBlockId(x, y+1, z)==0)
						{
							world.setBlock(x, y+1, z, ModBlocks.hemp.blockID, 5, 2);
							world.setBlockMetadataWithNotify(x, y, z, 4, 2);
						}
					}
				}
			}
		}
	}

	public void Grow(World world, int x, int y, int z, BonemealEvent event)
	{
		int l = world.getBlockMetadata(x, y, z);

		if(l>2)
		{
			event.setResult(Result.DENY);
		}
		else
		{
			event.setResult(Result.ALLOW);
			l += MathHelper.getRandomIntegerInRange(world.rand, 1, 3);
			if(l>3) l = 3;
			world.setBlockMetadataWithNotify(x, y, z, l, 2);
		}
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		int l = world.getBlockId(x, y, z-1);
		int i1 = world.getBlockId(x, y, z+1);
		int j1 = world.getBlockId(x-1, y, z);
		int k1 = world.getBlockId(x+1, y, z);
		int l1 = world.getBlockId(x-1, y, z-1);
		int i2 = world.getBlockId(x+1, y, z-1);
		int j2 = world.getBlockId(x+1, y, z+1);
		int k2 = world.getBlockId(x-1, y, z+1);
		boolean flag = j1==this.blockID||k1==this.blockID;
		boolean flag1 = l==this.blockID||i1==this.blockID;
		boolean flag2 = l1==this.blockID||i2==this.blockID||j2==this.blockID||k2==this.blockID;

		for(int l2 = x-1; l2<=x+1; ++l2)
		{
			for(int i3 = z-1; i3<=z+1; ++i3)
			{
				int j3 = world.getBlockId(l2, y-1, i3);
				float f1 = 0.0F;

				if(blocksList[j3]!=null&&blocksList[j3].canSustainPlant(world, l2, y-1, i3, ForgeDirection.UP, this))
				{
					f1 = 1.0F;

					if(blocksList[j3].isFertile(world, l2, y-1, i3))
					{
						f1 = 3.0F;
					}
				}

				if(l2!=x||i3!=z)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if(flag2||flag&&flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	protected int getSeedItem()
	{
		return ModItems.hemp.itemID;
	}

	protected int getCropItem()
	{
		return ModItems.hemp.itemID;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		if(world.getBlockId(x, y-1, z)==this.blockID&&world.getBlockMetadata(x, y-1, z)==4&&world.getBlockMetadata(x, y, z)==5)
		{
			return true;
		}
		else
		{
			if(world.getBlockId(x+1, y-1, z)==Block.waterStill.blockID||world.getBlockId(x-1, y-1, z)==Block.waterStill.blockID||world.getBlockId(x, y-1, z+1)==Block.waterStill.blockID
					||world.getBlockId(x, y-1, z-1)==Block.waterStill.blockID)
			{
				Block block = Block.blocksList[world.getBlockId(x, y-1, z)];
				return (block!=null&&block.canSustainPlant(world, x, y-1, z, ForgeDirection.UP, this));
			}
		}
		return false;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborId)
	{
		this.checkBlockCoordValid(world, x, y, z);

		if(world.getBlockMetadata(x, y, z)==4)
		{
			if(world.getBlockMetadata(x, y+1, z)!=5||world.getBlockId(x, y+1, z)!=this.blockID)
			{
				world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			}
		}
	}

	protected final void checkBlockCoordValid(World world, int x, int y, int z)
	{
		if(!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return this.canPlaceBlockAt(world, x, y, z);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getRenderType()
	{
		return 1;
	}

	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z)
	{
		return EnumPlantType.Beach;
	}

	@Override
	public int getPlantID(World world, int x, int y, int z)
	{
		return blockID;
	}

	@Override
	public int getPlantMetadata(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = super.getBlockDropped(world, x, y, z, metadata, fortune);

		if(metadata>=3)
		{
			ret.add(new ItemStack(this.getSeedItem(), 1, 0));
		}
		return ret;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata)
	{
		if(metadata==5&&world.getBlockId(x, y-1, z)==this.blockID&&world.getBlockMetadata(x, y-1, z)==4)
		{
			world.setBlockMetadataWithNotify(x, y-1, z, 3, 2);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, int x, int y, int z)
	{
		int metadata = iBlockAccess.getBlockMetadata(x, y, z);

		if(metadata==4)
		{
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
		}
		else if(metadata==3)
		{
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.8F, 0.7F);
		}
		else if(metadata==2||metadata==5)
		{
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
		}
		else if(metadata==1)
		{
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
		}
		else if(metadata==0)
		{
			setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.2F, 0.7F);
		}
	}
}