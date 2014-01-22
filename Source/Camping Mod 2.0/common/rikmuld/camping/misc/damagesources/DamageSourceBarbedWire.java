package rikmuld.camping.misc.damagesources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;

public class DamageSourceBarbedWire extends DamageSource {

	public DamageSourceBarbedWire(String name)
	{
		super(name);
	}

	@Override
	public ChatMessageComponent getDeathMessage(EntityLivingBase entity)
	{
		ChatMessageComponent message = new ChatMessageComponent();
		message.addText(((EntityPlayer)entity).username + " was stung by barbed wire");
		return message;
	}
}
