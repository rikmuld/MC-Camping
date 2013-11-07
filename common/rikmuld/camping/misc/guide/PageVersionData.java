package rikmuld.camping.misc.guide;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.misc.version.VersionData;

public class PageVersionData extends PageTextData{

	public PageVersionData(Node dataNode)
	{
		super(dataNode);
	}

	@Override
	public void setData()
	{
		super.setData();
		
		this.text += VersionData.NEW_VERSION;
	}
}
