package rikmuld.camping.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCampfireCook extends BlockUnstableMain {

	public BlockCampfireCook(String name)
	{
		super(name, Material.fire);
		setHardness(2.0F);
		setLightValue(1.0F);
		setStepSound(soundStoneFootstep);
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		BlockUtil.dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, id, meta);
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		return new TileEntityCampfireCook();
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.blocksList[Block.stone.blockID].getIcon(0, 0);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntityCampfireCook tile = (TileEntityCampfireCook)world.getBlockTileEntity(x, y, z);
		return tile.fuel > 0? 15:0;
	}

	@Override
	public final int getRenderType()
	{
		return -1;
	}

	@Override
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if(!world.isRemote)
		{
			player.openGui(CampingMod.instance, GuiInfo.GUI_COOK, world, x, y, z);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		TileEntityCampfireCook tile = (TileEntityCampfireCook)world.getBlockTileEntity(x, y, z);

		if(tile.fuel > 0)
		{
			for(int i = 0; i < 3; i++)
			{
				float motionY = (random.nextFloat() / 40F) + 0.025F;

				double particleX = ((x + 0.5F) - 0.15F) + (random.nextInt(30) / 100F);
				double particleY = y + 0.1F + (random.nextInt(15) / 100F);
				double particleZ = ((z + 0.5F) - 0.15F) + (random.nextInt(30) / 100F);

				CampingMod.proxy.spawnFlame(world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, 16);
				world.spawnParticle("smoke", particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D);
			}
		}
	}

	@Override
	public final boolean renderAsNormalBlock()
	{
		return false;
	}
}
