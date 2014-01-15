package rikmuld.camping.core.register;

import rikmuld.camping.core.lib.PotionInfo;
import rikmuld.camping.misc.potions.PotionBleeding;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ModPotions {

	public static Potion bleeding;
	
	public static void init()
	{
		PotionInfo.putAll();
		
		bleeding = new PotionBleeding(PotionInfo.BLEEDING);
	}
}
