package rikmuld.camping.misc.damagesources;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;

public class DamageSourceBleeding extends DamageSource{

	public DamageSourceBleeding(String name) 
	{
		super(name);
	}
	
	@Override
    public ChatMessageComponent getDeathMessage(EntityLivingBase entity)
    {
        ChatMessageComponent message = new ChatMessageComponent();
    	
        Random random = new Random();
        int num = random.nextInt(5);
        
        if(entity instanceof EntityPlayer) 
        {
        	if(num==0)message.addText(((EntityPlayer)entity).username + " bled away");   
        	if(num==1)message.addText(((EntityPlayer)entity).username + " has run out of blood");        
        	if(num==2)message.addText(((EntityPlayer)entity).username + " bled out");   
        	if(num==3)message.addText(((EntityPlayer)entity).username + " bled to death");      
        	if(num==4)message.addText(((EntityPlayer)entity).username + " fizzled");             	
        }
        return message;
    }
}
