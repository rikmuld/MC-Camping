package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModStructures;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.item.itemblock.ItemBlockTent;
import rikmuld.camping.misc.bounds.BoundsTracker;
import rikmuld.camping.network.packets.PacketOpenGui;
import cpw.mods.fml.common.network.Player;

public class BlockTent extends BlockRotationMain {

	int color;

	public float rotationYaw = 0;

	public BlockTent(String name)
	{
		super(name, Material.sponge, null, ItemBlockTent.class, false);
		setHardness(0.2F);
	}

	public static boolean isBlockHeadOfBed(int meta)
	{
		return true;
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB alignedBB, List list, Entity entity)
	{
		TileEntityTent tile = (TileEntityTent)world.getBlockTileEntity(x, y, z);
		TileEntityTent.bounds[tile.rotation].setBlockCollision(this);

		super.addCollisionBoxesToList(world, x, y, z, alignedBB, list, entity);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntityTent tile = (TileEntityTent)world.getBlockTileEntity(x, y, z);

		if(tile != null)
		{
			color = tile.color;

			BlockUtil.dropItems(world, x, y, z);

			tile.structures[tile.rotation].destroyStructure(world, tile.tracker[tile.rotation]);
			super.breakBlock(world, x, y, z, par5, par6);

			if(!tile.dropped)
			{
				ArrayList<ItemStack> stacks = getDroppes(world, x, y, z, world.getBlockMetadata(x, y, z), 1);
				stacks.addAll(tile.getContends());

				for(ItemStack stack: stacks)
				{
					dropBlockAsItem_do(world, x, y, z, stack);
				}
				tile.dropped = true;
			}
		}

		world.setBlock(x, y, z, 0);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		int l = world.getBlockId(x, y, z);
		Block block = Block.blocksList[l];

		int direction = 0;
		int facing = MathHelper.floor_double(((rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3;

		if(facing == 0)
		{
			direction = ForgeDirection.NORTH.ordinal() - 2;
		}
		else if(facing == 1)
		{
			direction = ForgeDirection.SOUTH.ordinal() - 2;
		}
		else if(facing == 2)
		{
			direction = ForgeDirection.WEST.ordinal() - 2;
		}
		else if(facing == 3)
		{
			direction = ForgeDirection.EAST.ordinal() - 2;
		}

		return ((block == null) || block.isBlockReplaceable(world, x, y, z)) && ModStructures.tent[direction].canBePlaced(world, new BoundsTracker(x, y, z, TileEntityTent.bounds[direction]));
	}

	@Override
	public TileEntityRotation createNewTileEntity(World world)
	{
		return new TileEntityTent();
	}

	@Override
	protected void dropBlockAsItem_do(World world, int x, int y, int z, ItemStack stack)
	{
		if(stack != null)
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("color", color);
		}
		super.dropBlockAsItem_do(world, x, y, z, stack);
	}

	public void dropIfCantStay(World world, int x, int y, int z)
	{
		TileEntityTent tile = (TileEntityTent)world.getBlockTileEntity(x, y, z);

		if((tile.structures != null) && !tile.structures[tile.rotation].hadSolitUnderGround(world, tile.tracker[tile.rotation]))
		{
			breakBlock(world, x, y, z, 0, 0);
		}
	}

	@Override
	public int getBedDirection(IBlockAccess world, int x, int y, int z)
	{
		return ((TileEntityRotation)world.getBlockTileEntity(x, y, z)).rotation;
	}

	public ArrayList<ItemStack> getDroppes(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = 1;
		for(int i = 0; i < count; i++)
		{
			int id = idDropped(metadata, world.rand, fortune);
			if(id > 0)
			{
				ret.add(new ItemStack(id, 1, damageDropped(metadata)));
			}
		}
		return ret;
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.cloth.getIcon(0, 0);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntityTent tile = (TileEntityTent)world.getBlockTileEntity(x, y, z);
		return (tile.lanternDamage == 0) && (tile.lanterns > 0)? 15:0;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLivingBase player)
	{
		return true;
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, int x, int y, int z)
	{
		return !isBlockHeadOfBed(world.getBlockMetadata(x, y, z));
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if(!world.isRemote)
		{
			if((player.getCurrentEquippedItem() != null) && ((TileEntityTent)world.getBlockTileEntity(x, y, z)).addContends(player.getCurrentEquippedItem()))
			{
				if(player.getCurrentEquippedItem().itemID == ModBlocks.lantern.blockID)
				{
					ModAchievements.tentLight.addStatToPlayer(player);
				}
				if(player.getCurrentEquippedItem().itemID == ModBlocks.sleepingbag.blockID)
				{
					ModAchievements.tentSleep.addStatToPlayer(player);
				}
				if(player.getCurrentEquippedItem().itemID == Block.chest.blockID)
				{
					ModAchievements.tentStore.addStatToPlayer(player);
				}

				player.getCurrentEquippedItem().stackSize--;
				if(player.getCurrentEquippedItem().stackSize < 0)
				{
					ItemStackUtil.setCurrentPlayerItem(player, null);
				}
				return true;
			}
			else if((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().itemID == Item.dyePowder.itemID) && (((TileEntityTent)world.getBlockTileEntity(x, y, z)).color != player.getCurrentEquippedItem().getItemDamage()))
			{
				((TileEntityTent)world.getBlockTileEntity(x, y, z)).setColor(player.getCurrentEquippedItem().getItemDamage());
				player.getCurrentEquippedItem().stackSize--;
				return true;
			}
			else
			{
				PacketUtil.sendToPlayer(new PacketOpenGui(GuiInfo.GUI_TENT, x, y, z), (Player)player);
				return true;
			}
		}
		return true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, blockID, 10);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
		((TileEntityTent)world.getBlockTileEntity(x, y, z)).setColor(itemStack.hasTagCompound()? itemStack.getTagCompound().getInteger("color"):15);;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		world.scheduleBlockUpdate(x, y, z, blockID, 10);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityTent tile = (TileEntityTent)world.getBlockTileEntity(x, y, z);
		TileEntityTent.bounds[tile.rotation].setBlockBounds(this);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)
		{
			dropIfCantStay(world, x, y, z);
		}
	}
}