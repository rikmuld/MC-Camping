package rikmuld.camping.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.TextInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;

public class ItemKit extends ItemMain {

	public static final int KIT_EMPTY = 0;
	public static final int KIT_SPIT = 1;
	public static final int KIT_GRILL = 2;
	public static final int KIT_PAN = 3;
	public static final int KIT_USELESS = 4;

	static String[] metadataIGNames = new String[]{"Empty Kit", "Spit Kit", "Grill Kit", "Pan Kit", "Useless Kit"};
	static String[] metadataNames = new String[]{"kit", "spitKit", "grillKit", "panKit", "uselessKit"};

	public ItemKit(String name)
	{
		super(name, metadataIGNames, metadataNames, false);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if((stack.getItemDamage() > 0) && stack.hasTagCompound() && Keyboard.isKeyDown(42))
		{
			list.clear();
			list.add("This kit contains:");
			Map<String, Integer> itemMap = new HashMap<String, Integer>();
			NBTTagList containingItems = (NBTTagList)stack.getTagCompound().getTag("Items");
			for(int itemCound = 0; itemCound < containingItems.tagCount(); itemCound++)
			{
				ItemStack item = ItemStack.loadItemStackFromNBT((NBTTagCompound)containingItems.tagAt(itemCound));
				if(!itemMap.containsKey(item.getDisplayName()))
				{
					itemMap.put(item.getDisplayName(), 1);
				}
				else
				{
					itemMap.put(item.getDisplayName(), itemMap.get(item.getDisplayName()) + 1);
				}
			}

			Iterator<String> names = itemMap.keySet().iterator();
			Iterator<Integer> values = itemMap.values().iterator();

			while(names.hasNext())
			{
				int value = values.next();
				String name = names.next();

				list.add(Integer.toString(value) + " " + name);
			}
		}
		else if(stack.getItemDamage() > 0)
		{
			list.add(TextInfo.FORMAT_ITALIC + "Hold shift for more information");
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return metadataNames[itemstack.getItemDamage()];
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			player.openGui(CampingMod.instance, GuiInfo.GUI_KIT, world, 0, 0, 0);
		}
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativetabs, List list)
	{
		ItemStack stick = new ItemStack(Item.stick);
		ItemStack stickIron = new ItemStack(ModItems.parts, 1, ItemParts.STICK);
		ItemStack string = new ItemStack(ModItems.parts, 1, ItemParts.PAN);
		ItemStack pan = new ItemStack(Item.silk);
		ItemStack fenceIron = new ItemStack(Block.fenceIron);

		ItemStack[][] inventoryContents = new ItemStack[][]{{stick, stick, stickIron},{stick, stick, stick, stick, stickIron, stickIron, fenceIron},{stick, stick, stickIron, string, pan}};
		
		list.add(new ItemStack(id, 1, 0));
		
		for(int meta = 1; meta < metadataNames.length-1; ++meta)
		{
			ItemStack stack = new ItemStack(id, 1, meta);

			NBTTagList inventory = new NBTTagList();
			for(int slot = 0; slot < inventoryContents[meta-1].length; ++slot)
			{
				if(inventoryContents[meta-1][slot] != null)
				{
					NBTTagCompound Slots = new NBTTagCompound();
					Slots.setByte("Slot", (byte)slot);
					inventoryContents[meta-1][slot].writeToNBT(Slots);
					inventory.appendTag(Slots);
				}
			}
			
			stack.setTagCompound(new NBTTagCompound());			
			stack.getTagCompound().setTag("Items", inventory);
			
			list.add(stack);
		}
	}
}
