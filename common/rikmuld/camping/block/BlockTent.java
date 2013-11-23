package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityBerry;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityRotation;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.item.itemblock.ItemBlockBerryLeaves;

public class BlockTent extends BlockRotationMain {
	
	public BlockTent(String name)
	{
		super(name, Material.cloth, false);
		this.setHardness(0.2F);
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
	public TileEntityRotation createNewTileEntity(World world)
	{
		return new TileEntityTent();
	}
}
