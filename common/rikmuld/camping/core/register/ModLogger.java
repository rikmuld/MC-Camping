package rikmuld.camping.core.register;

import java.nio.charset.Charset;
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
			if(message[i] instanceof String) ModLogger.logDebug((String) message[i]);
			else if(message[i] instanceof Integer) ModLogger.logDebug((int) message[i]);
			else if(message[i] instanceof Boolean) ModLogger.logDebug((boolean) message[i]);
			else if(message[i] instanceof Float) ModLogger.logDebug((float) message[i]);
			else if(message[i] instanceof Double) ModLogger.logDebug((double) message[i]);
			else if(message[i] instanceof Charset) ModLogger.logDebug((char) message[i]);
			else if(message[i] instanceof Long) ModLogger.logDebug((long) message[i]);
			else if(message[i]==null) ModLogger.logDebug("NULL");
			else ModLogger.logDebug(message[i].toString());
		}
	}

	private static void logDebug(String... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, message[i]);
		}
	}

	private static void logDebug(int... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}

	private static void logDebug(float... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}

	private static void logDebug(double... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}

	private static void logDebug(boolean... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}

	private static void logDebug(char... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}

	private static void logDebug(long... message)
	{
		for(int i = 0; i<message.length; i++)
		{
			ModLogger.log(Level.INFO, ""+message[i]);
		}
	}
}