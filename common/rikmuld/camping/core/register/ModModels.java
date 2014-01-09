package rikmuld.camping.core.register;

import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.techne.TechneModel;
import net.minecraftforge.client.model.techne.TechneModelLoader;
import rikmuld.camping.core.lib.ModelInfo;
import rikmuld.camping.misc.models.CustomModel;
import rikmuld.camping.misc.models.CustomModelLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModModels {

	public static TechneModel campfire;
	public static TechneModel log;
	public static CustomModel tent;
	public static CustomModel trapOpen;
	public static CustomModel trapClosed;
	public static TechneModel throphyAntler;

	static TechneModelLoader loader;
	static CustomModelLoader customLoader;

	public static void init()
	{
		loader = new TechneModelLoader();
		customLoader = new CustomModelLoader();
		
		try
		{
			campfire = (TechneModel) loader.loadInstance("campfireDeco", ModModels.class.getResource(ModelInfo.CAMPFIRE));
			log = (TechneModel) loader.loadInstance("log", ModModels.class.getResource(ModelInfo.LOG));	
			throphyAntler = (TechneModel) loader.loadInstance("throphy", ModModels.class.getResource(ModelInfo.THROPHY_ANTLER));			
			tent = (CustomModel) customLoader.loadInstance(128, 64, "tent", ModModels.class.getResource(ModelInfo.TENT));
			trapOpen = (CustomModel) customLoader.loadInstance(32, 16, "trapOpen", ModModels.class.getResource(ModelInfo.TRAP_OPEN));
			trapClosed = (CustomModel) customLoader.loadInstance(32, 16, "trapClosed", ModModels.class.getResource(ModelInfo.TRAP_CLOSED));
		}
		
		catch(ModelFormatException e)
		{
			e.printStackTrace();
		}
	}
}
