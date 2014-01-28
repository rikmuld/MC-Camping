package rikmuld.camping.core.util;

import java.util.ArrayList;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.core.lib.KeyInfo;
import rikmuld.camping.misc.key.IKeyListner;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class KeyUtil {

	public static ArrayList<KeyBinding> keyBindingsList;
	public static ArrayList<Boolean> isRepeatingList;
	public static ArrayList<IKeyListner> keyListners = new ArrayList<IKeyListner>();

	public static void addIsRepeating(boolean value)
	{
		if(isRepeatingList == null)
		{
			isRepeatingList = new ArrayList<Boolean>();
		}
		isRepeatingList.add(value);
	}

	public static void addKeyBinding(String name, int value)
	{
		if(keyBindingsList == null)
		{
			keyBindingsList = new ArrayList<KeyBinding>();
		}
		keyBindingsList.add(new KeyBinding(name, value));

		addIsRepeating(false);
	}

	public static void fireKey(boolean down, String key, EntityPlayer player)
	{
		for(IKeyListner listner: keyListners)
		{
			if(down)
			{
				listner.keyDown(key, player);
			}
			else
			{
				listner.keyUp(key, player);
			}
		}
	}

	public static boolean[] gatherIsRepeating()
	{
		boolean[] isRepeating = new boolean[isRepeatingList.size()];
		for(int x = 0; x < isRepeating.length; x++)
		{
			isRepeating[x] = isRepeatingList.get(x).booleanValue();
		}

		return isRepeating;
	}

	public static KeyBinding[] gatherKeyBindings()
	{
		return keyBindingsList.toArray(new KeyBinding[keyBindingsList.size()]);
	}

	public static void putKeyBindings()
	{
		KeyInfo.putAll();
		for(int i = 0; i < KeyInfo.keys.size(); i++)
		{
			addKeyBinding(((String)KeyInfo.keys.keySet().toArray()[i]), ((Integer)KeyInfo.keys.values().toArray()[i]));
			LanguageRegistry.instance().addStringLocalization(((String)KeyInfo.names.keySet().toArray()[i]), "en_US", ((String)KeyInfo.names.values().toArray()[i]));
		}
	}

	public static void registerKeyListner(IKeyListner listner)
	{
		keyListners.add(listner);
	}
}
