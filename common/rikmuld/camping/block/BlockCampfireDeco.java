package rikmuld.camping.block;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCampfireDeco extends BlockMain {
	
	public BlockCampfireDeco(String name)
	{
		super(name, Material.fire);
		setHardness(3.0F);
		setLightValue(1.0F);
		setStepSound(soundWoodFootstep);
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
	}
	
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
		return new TileEntityCampfireDeco();
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		for(int i = 0; i<3; i++)
		{
			float motionY = random.nextFloat()/40F+0.025F;
			
			double particleX = (float) x+0.5F-0.15F+((float)random.nextInt(30)/100F);
			double particleY = (float) y+0.1F+((float)random.nextInt(15)/100F);
			double particleZ = (float) z+0.5F-0.15F+((float)random.nextInt(30)/100F);
			
			CampingMod.proxy.spawnFlame(world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, ((TileEntityCampfireDeco)world.getBlockTileEntity(x, y, z)).color);
			world.spawnParticle("smoke", particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D);
		}
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		return Block.blocksList[Block.planks.blockID].getIcon(0, 0);
	}
	
	public void dropIfCantStay(World world, int x, int y, int z)
	{
		if(!world.doesBlockHaveSolidTopSurface(x, y-1, z))
		{
			for(ItemStack item: this.getBlockDropped(world, x, y, z, 0, 1))
			{
				this.dropBlockAsItem_do(world, x, y, z, item);
			}
			this.breakBlock(world, x, y, z, blockID, 0);
			world.setBlock(x, y, z, 0);
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)this.dropIfCantStay(world, x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		BlockUtil.dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, id, meta);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        int l = world.getBlockId(x, y, z);
        Block block = Block.blocksList[l];
        return (block == null || block.isBlockReplaceable(world, x, y, z))&&world.doesBlockHaveSolidTopSurface(x, y-1, z);
    }
	
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

        stacks.add(new ItemStack(Item.stick.itemID, world.rand.nextInt(4)+1, 0));
        stacks.add(new ItemStack(ModBlocks.campfireBase.blockID, 1, 0));

        return stacks;
    }
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if(player.getCurrentEquippedItem()!=null&&player.getCurrentEquippedItem().itemID==ModItems.knife.itemID)
		{
			if(!player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.campfireDeco)))ItemStackUtil.dropItemsInWorld(new ItemStack[]{new ItemStack(ModBlocks.campfireDeco.blockID, 1, 0)}, world, x, y, z);
			this.breakBlock(world, x, y, z, world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
			world.setBlock(x, y, z, 0);
			ItemStackUtil.addDamage(player.getCurrentEquippedItem(), player, 1);
		}
		if(!world.isRemote)
		{
			if(player.getCurrentEquippedItem()==null||player.getCurrentEquippedItem().itemID!=ModItems.knife.itemID)player.openGui(CampingMod.instance, GuiInfo.GUI_CAMPFIRE_DECO, world, x, y, z);
		}
		return true;
	}
}
