package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.BlockInfo;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.entity.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLight extends BlockMain {
	
	public BlockLight(String name)
	{
		super(name, Material.air);
		setLightValue(1.0F);
		setBlockBounds(0F, 0F, 0F, 0F, 0F, 0F);
		setCreativeTab(null);
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
	public TileEntity createTileEntity(World world, int meta)
	{
		return new TileEntityLight();
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
}
