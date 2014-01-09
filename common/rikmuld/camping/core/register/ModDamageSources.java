package rikmuld.camping.core.register;

import net.minecraft.util.DamageSource;
import rikmuld.camping.core.lib.DamageSourceInfo;
import rikmuld.camping.misc.damagesources.DamageSourceBearTrap;
import rikmuld.camping.misc.damagesources.DamageSourceBleeding;

public class ModDamageSources {

	public static DamageSource bearTrap;
	public static DamageSource bleeding;

	public static void init()
	{
		bearTrap = new DamageSourceBearTrap(DamageSourceInfo.BEARTRAP);
		bleeding = new DamageSourceBleeding(DamageSourceInfo.BLEEDING);
	}
}
