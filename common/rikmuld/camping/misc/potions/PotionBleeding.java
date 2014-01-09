package rikmuld.camping.misc.potions;

import rikmuld.camping.core.lib.PotionInfo;
import rikmuld.camping.core.register.ModDamageSources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;

public class PotionBleeding extends Potion{

	public PotionBleeding(String name) 
	{
		super(PotionInfo.id(name), false, 9643043);
		this.setPotionName(PotionInfo.name(name));
		setIconIndex(4, 0);
	}
	
	public void performEffect(EntityLivingBase entity, int amplifier)
    {
		entity.attackEntityFrom(ModDamageSources.bleeding, (entity.getMaxHealth()/20)*(amplifier+1));
    }
	
	public boolean isReady(int par1, int par2)
    {
		 int k = 60 >> par2;
         return k > 0 ? par1 % k == 0 : true;
    }
}
