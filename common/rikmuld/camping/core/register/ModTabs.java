package rikmuld.camping.core.register;

import rikmuld.camping.misc.creativeTab.TabMain;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModTabs {

	public static TabMain campingTab = new TabMain("campingTab");

	public static void init()
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup.campingTab", "en_US", "The Camping Mod 2.0");
	}
}
