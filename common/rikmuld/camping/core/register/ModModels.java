package rikmuld.camping.core.register;

import java.net.MalformedURLException;
import java.net.URL;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.ModelInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.techne.TechneModel;
import net.minecraftforge.client.model.techne.TechneModelLoader;

public class ModModels {

	public static TechneModel campfire;
	public static TechneModel log;

	static TechneModelLoader loader = new TechneModelLoader();

	public static void init()
	{
		try
		{
			campfire = (TechneModel) loader.loadInstance("campfireDeco", ModModels.class.getResource(ModelInfo.CAMPFIRE_DECO));
			log = (TechneModel) loader.loadInstance("log", ModModels.class.getResource(ModelInfo.LOG));
		}
		catch(ModelFormatException e)
		{
			e.printStackTrace();
		}
	}
}
