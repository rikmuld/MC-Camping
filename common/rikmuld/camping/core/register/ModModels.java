package rikmuld.camping.core.register;

import java.net.MalformedURLException;
import java.net.URL;

import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.ModelInfo;
import rikmuld.camping.misc.models.CustomModel;
import rikmuld.camping.misc.models.CustomModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.techne.TechneModel;
import net.minecraftforge.client.model.techne.TechneModelLoader;

public class ModModels {

	public static TechneModel campfire;
	public static TechneModel log;
	public static CustomModel tent;

	static TechneModelLoader loader = new TechneModelLoader();
	static CustomModelLoader customLoader = new CustomModelLoader();

	public static void init()
	{
		try
		{
			campfire = (TechneModel) loader.loadInstance("campfireDeco", ModModels.class.getResource(ModelInfo.CAMPFIRE));
			log = (TechneModel) loader.loadInstance("log", ModModels.class.getResource(ModelInfo.LOG));
			
			customLoader.setTextureSize(128, 64);
			
			tent = (CustomModel) customLoader.loadInstance("tent", ModModels.class.getResource(ModelInfo.TENT));
		}
		
		catch(ModelFormatException e)
		{
			e.printStackTrace();
		}
	}
}
