package rikmuld.camping.misc.achievements;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import rikmuld.camping.core.lib.AchievementInfo;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.util.PlayerUtil;

public class AchievementMain extends Achievement{
	
	public AchievementMain(String name, int x, int y, ItemStack picture, Achievement dependency, boolean isSpecial) 
	{
		super(AchievementInfo.id(name), name, x, y, picture, dependency);
		
		if(dependency==null)this.setIndependent();
		if(isSpecial)this.setSpecial();
		
		ModAchievements.register(this, AchievementInfo.name(name), AchievementInfo.description(name));
	}

	public AchievementMain(String name, int x, int y, Block picture, Achievement dependency, boolean isSpecial) 
	{
		this(name, x, y, new ItemStack(picture), dependency, isSpecial);
	}
	
	public AchievementMain(String name, int x, int y, Item picture, Achievement dependency, boolean isSpecial) 
	{
		this(name, x, y, new ItemStack(picture), dependency, isSpecial);
	}
	
	public void addStatToPlayer(EntityPlayer player)
	{
		player.addStat(this, 1);
		PlayerUtil.getPlayerDataTag(player).setBoolean(player.username+"_"+this.getName(), true);
		
		checkForMainAchievements(player);
	}

	private void checkForMainAchievements(EntityPlayer player) 
	{
		NBTTagCompound tag = PlayerUtil.getPlayerDataTag(player);
				
		if(tag.getBoolean(player.username+"_"+ModAchievements.encounterCamper.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.berry1.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.berry2.getName())&&this.getName()!=ModAchievements.basic.getName())ModAchievements.basic.addStatToPlayer(player);
		if(tag.getBoolean(player.username+"_"+ModAchievements.hunter.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.camper.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.basic.getName())&&this.getName()!=ModAchievements.legend.getName())ModAchievements.legend.addStatToPlayer(player);	
		if(tag.getBoolean(player.username+"_"+ModAchievements.throphy.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.armor.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.trapBait.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.trapLuckey.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.bear.getName())&&this.getName()!=ModAchievements.hunter.getName())ModAchievements.hunter.addStatToPlayer(player);
		if(tag.getBoolean(player.username+"_"+ModAchievements.bag.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.tentStore.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.tentSleep.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.tentLight.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.spit.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.effect.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.log.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.pan.getName())&&tag.getBoolean(player.username+"_"+ModAchievements.grill.getName())&&this.getName()!=ModAchievements.camper.getName())ModAchievements.camper.addStatToPlayer(player);
	}
}
