package rikmuld.camping.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.entity.tileentity.TileEntityBearTrap;

public class BlockTrap extends BlockUnstableMain{

	public BlockTrap(String name) 
	{
		super(name, Material.iron);
		this.setBlockBounds(0.21875F, 0, 0.21875F, 0.78125F, 0.1875F, 0.78125F);
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
		return new TileEntityBearTrap();
	}
	
	@Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAllignedBB, List list, Entity entity)
	{}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		TileEntityBearTrap tile = (TileEntityBearTrap) world.getBlockTileEntity(x, y, z);
		if(!tile.open)
		{
			tile.open = true;
			tile.cooldown = 10;
			tile.trappedEntity=null;
			return  true;
		}
		else 
		{
			player.openGui(CampingMod.instance, GuiInfo.GUI_TRAP, world, x, y, z);
			return true;
		}
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityBearTrap tile = (TileEntityBearTrap) world.getBlockTileEntity(x, y, z);
		if(tile.open)setBlockBounds(0.21875F, 0, 0.21875F, 0.78125F, 0.1875F, 0.78125F);
		else setBlockBounds(0.21875F, 0, 0.34375F, 0.78125F, 0.25F, 0.65F);
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.blockIron.getIcon(0, 0);
	}
}
