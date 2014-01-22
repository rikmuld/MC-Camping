package rikmuld.camping.misc.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import rikmuld.camping.core.lib.PotionInfo;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModDamageSources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PotionBleeding extends Potion {

	public PotionBleeding(String name)
	{
		super(PotionInfo.id(name), false, 9643043);
		setPotionName(PotionInfo.name(name));
		setIconIndex(0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(TextureInfo.SPRITE_POTION));
		return true;
	}

	@Override
	public boolean isReady(int par1, int par2)
	{
		int k = 60 >> par2;
		return k > 0? (par1 % k) == 0:true;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		entity.attackEntityFrom(ModDamageSources.bleeding, (entity.getMaxHealth() / 20) * (amplifier + 1));
	}
}
