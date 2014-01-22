package rikmuld.camping.misc.damagesources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;

public class DamageSourceBearTrap extends DamageSource {

	public DamageSourceBearTrap(String name)
	{
		super(name);
	}

	@Override
	public ChatMessageComponent getDeathMessage(EntityLivingBase entity)
	{
		ChatMessageComponent message = new ChatMessageComponent();
		if(entity instanceof EntityPlayer)
		{
			message.addText(((EntityPlayer)entity).username + " died by getting stuk in a bear trap");
		}
		return message;
	}
}
