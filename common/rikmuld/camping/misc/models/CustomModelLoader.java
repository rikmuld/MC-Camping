package rikmuld.camping.misc.models;

import java.net.URL;

import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

public class CustomModelLoader implements IModelCustomLoader {
    
	int width;
	int height;
    
	@Override
    public String getType()
    {
        return "Techne model";
    }

    private static final String[] types = { "tcn" };
   
    @Override
    public String[] getSuffixes()
    {
        return types;
    }
    
    public IModelCustom loadInstance(int width, int height, String resourceName, URL resource) throws ModelFormatException
    {
    	this.width = width;
    	this.height = height;
    	
    	return this.loadInstance(resourceName, resource);
    }

    @Override
    public IModelCustom loadInstance(String resourceName, URL resource) throws ModelFormatException
    {
        return new CustomModel(resourceName, resource, width, height);
    }
}
