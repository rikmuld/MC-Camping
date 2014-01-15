package rikmuld.camping.misc.version;

import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModLogger;

public class VersionData  implements Runnable {

	private static VersionData instance = new VersionData();
	
	Document doc;
	
	boolean checked = false;
	public static boolean doneChecking = false;
	
	int check = 0;

	public static String NEW_VERSION = "Not Found";

	public void GetXmlFile()
	{
		try
		{
			URL url = new URL("http://rikmuld.com/assets/files/version.xml");
			
			URLConnection connection = url.openConnection();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

			doc = builder.parse(connection.getInputStream());
		}
		catch(Exception e)
		{
			ModLogger.log(Level.WARNING, "Whooops, something whent wrong while cheking the version! "+Integer.toString(2-check)+((2-check==1)? " attempt":" attempts")+" left!");
		}
	}

	public void CheckVersion()
	{
		GetXmlFile();
		if(doc!=null)
		{			
			NodeList Version = doc.getElementsByTagName("Version");

			Node NewestVersion = Version.item(0);

			String NewVersion = NewestVersion.getTextContent();
			
			if(!NewVersion.equals(ModInfo.MOD_VERSION))
			{
				this.NEW_VERSION = NewVersion;
			}
			
			checked = true;
		}
	}

	@Override
	public void run()
	{		
		while(checked==false)
		{
			CheckVersion();
			check++;
			if(checked==false)
			{
				try
				{
					Thread.sleep(5000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			if(check>=3)
			{
				break;
			}
		}
		
		this.doneChecking = true;
	}

	public void execute()
	{
		if(ConfigInfoBoolean.value(ConfigInfo.ENABLE_VERSION))new Thread(instance).start();
	}
}
