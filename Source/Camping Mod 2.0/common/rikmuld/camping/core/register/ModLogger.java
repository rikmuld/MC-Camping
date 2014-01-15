package rikmuld.camping.core.register;

import java.util.logging.Level;
import java.util.logging.Logger;

import rikmuld.camping.core.lib.ModInfo;
import cpw.mods.fml.common.FMLLog;

public class ModLogger {

	private static Logger logger = Logger.getLogger(ModInfo.MOD_ID);

	public static void init()
	{
		logger.setParent(FMLLog.getLogger());
	}

	public static void log(Level logLevel, String message)
	{
		logger.log(logLevel, message);
	}

	public static void logMulti(Object... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			if(message[i]==null) ModLogger.log(Level.INFO, "NULL");
			else ModLogger.log(Level.INFO, message[i].toString());
		}
	}
}