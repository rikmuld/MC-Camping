package rikmuld.camping.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModBlocks;

public class ItemHemp extends ItemMain {

	int blockID = ModBlocks.hemp.blockID;

	public ItemHemp(String name)
	{
		super(name);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int i1 = par3World.getBlockId(par4, par5, par6);

		if((i1 == Block.snow.blockID) && ((par3World.getBlockMetadata(par4, par5, par6) & 7) < 1))
		{
			par7 = 1;
		}
		else if((i1 != Block.vine.blockID) && (i1 != Block.tallGrass.blockID) && (i1 != Block.deadBush.blockID) && ((Block.blocksList[i1] == null) || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
		{
			if(par7 == 0)
			{
				--par5;
			}

			if(par7 == 1)
			{
				++par5;
			}

			if(par7 == 2)
			{
				--par6;
			}

			if(par7 == 3)
			{
				++par6;
			}

			if(par7 == 4)
			{
				--par4;
			}

			if(par7 == 5)
			{
				++par4;
			}
		}

		if(par1ItemStack.stackSize == 0) return false;
		else if(!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) return false;
		else if((par5 == 255) && Block.blocksList[blockID].blockMaterial.isSolid()) return false;
		else if(par3World.canPlaceEntityOnSide(blockID, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
		{
			Block block = Block.blocksList[blockID];
			int j1 = getMetadata(par1ItemStack.getItemDamage());
			int k1 = Block.blocksList[blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, j1);

			if(placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1))
			{
				par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}

			return true;
		}
		else return false;
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		if(!world.setBlock(x, y, z, blockID, metadata, 3)) return false;

		if(world.getBlockId(x, y, z) == blockID)
		{
			Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, player, stack);
			Block.blocksList[blockID].onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true;
	}
}
