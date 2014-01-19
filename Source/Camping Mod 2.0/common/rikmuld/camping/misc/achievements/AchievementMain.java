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
		PlayerUtil.getPlayerDataTag(player).setBoolean(player.username+"_"+statName, true);
		
		checkForMainAchievements(player);
	}

	private void checkForMainAchievements(EntityPlayer player) 
	{
		NBTTagCompound tag = PlayerUtil.getPlayerDataTag(player);
				
		if(tag.getBoolean(player.username+"_"+ModAchievements.encounterCamper.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.berry1.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.berry2.statName)&&this.statName!=ModAchievements.basic.statName)ModAchievements.basic.addStatToPlayer(player);
		if(tag.getBoolean(player.username+"_"+ModAchievements.hunter.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.camper.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.basic.statName)&&this.statName!=ModAchievements.legend.statName)ModAchievements.legend.addStatToPlayer(player);	
		if(tag.getBoolean(player.username+"_"+ModAchievements.throphy.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.armor.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.trapBait.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.trapLuckey.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.bear.statName)&&this.statName!=ModAchievements.hunter.statName)ModAchievements.hunter.addStatToPlayer(player);
		if(tag.getBoolean(player.username+"_"+ModAchievements.bag.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.tentStore.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.tentSleep.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.tentLight.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.spit.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.effect.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.log.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.pan.statName)&&tag.getBoolean(player.username+"_"+ModAchievements.grill.statName)&&this.statName!=ModAchievements.camper.statName)ModAchievements.camper.addStatToPlayer(player);
	}
}
