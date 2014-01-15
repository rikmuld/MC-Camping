package rikmuld.camping.core.register;

import net.minecraft.util.DamageSource;
import rikmuld.camping.core.lib.DamageSourceInfo;
import rikmuld.camping.misc.damagesources.DamageSourceBarbedWire;
import rikmuld.camping.misc.damagesources.DamageSourceBearTrap;
import rikmuld.camping.misc.damagesources.DamageSourceBleeding;

public class ModDamageSources {

	public static DamageSource barbedWire;
	public static DamageSource bearTrap;
	public static DamageSource bleeding;

	public static void init()
	{
		barbedWire = new DamageSourceBarbedWire(DamageSourceInfo.WIRE);
		bearTrap = new DamageSourceBearTrap(DamageSourceInfo.BEARTRAP);
		bleeding = new DamageSourceBleeding(DamageSourceInfo.BLEEDING);
	}
}
