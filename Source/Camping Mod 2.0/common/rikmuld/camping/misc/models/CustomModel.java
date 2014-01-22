package rikmuld.camping.misc.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cpw.mods.fml.common.FMLLog;

public class CustomModel extends ModelBase implements IModelCustom {

	public int textureHeight = 64;
	public int textureWidth = 32;

	public static final List<String> cubeTypes = Arrays.asList("d9e621f7-957f-4b77-b1ae-20dcd0da7751", "de81aa14-bd60-4228-8d8d-5238bcd3caaa");

	private String fileName;
	private Map<String, byte[]> zipContents = new HashMap<String, byte[]>();

	private Map<String, ModelRenderer> parts = new LinkedHashMap<String, ModelRenderer>();
	private String texture = null;

	public CustomModel(String fileName, URL resource, int width, int height) throws ModelFormatException
	{
		this.fileName = fileName;

		textureHeight = height;
		textureWidth = width;

		loadTechneModel(resource);
	}

	@Override
	public String getType()
	{
		return "tcn";
	}

	private void loadTechneModel(URL fileURL) throws ModelFormatException
	{
		try
		{
			ZipInputStream zipInput = new ZipInputStream(fileURL.openStream());

			ZipEntry entry;
			while((entry = zipInput.getNextEntry()) != null)
			{
				byte[] data = new byte[(int)entry.getSize()];
				// For some reason, using read(byte[]) makes reading stall upon
				// reaching a 0x1E byte
				int i = 0;
				while((zipInput.available() > 0) && (i < data.length))
				{
					data[i++] = (byte)zipInput.read();
				}
				zipContents.put(entry.getName(), data);
			}

			byte[] modelXml = zipContents.get("model.xml");
			if(modelXml == null) throw new ModelFormatException("Model " + fileName + " contains no model.xml file");

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(modelXml));

			NodeList nodeListTechne = document.getElementsByTagName("Techne");
			if(nodeListTechne.getLength() < 1) throw new ModelFormatException("Model " + fileName + " contains no Techne tag");

			NodeList nodeListModel = document.getElementsByTagName("Model");
			if(nodeListModel.getLength() < 1) throw new ModelFormatException("Model " + fileName + " contains no Model tag");

			NamedNodeMap modelAttributes = nodeListModel.item(0).getAttributes();
			if(modelAttributes == null) throw new ModelFormatException("Model " + fileName + " contains a Model tag with no attributes");

			Node modelTexture = modelAttributes.getNamedItem("texture");
			if(modelTexture != null)
			{
				texture = modelTexture.getTextContent();
			}

			NodeList shapes = document.getElementsByTagName("Shape");
			for(int i = 0; i < shapes.getLength(); i++)
			{
				Node shape = shapes.item(i);
				NamedNodeMap shapeAttributes = shape.getAttributes();
				if(shapeAttributes == null) throw new ModelFormatException("Shape #" + (i + 1) + " in " + fileName + " has no attributes");

				Node name = shapeAttributes.getNamedItem("name");
				String shapeName = null;
				if(name != null)
				{
					shapeName = name.getNodeValue();
				}
				if(shapeName == null)
				{
					shapeName = "Shape #" + (i + 1);
				}

				String shapeType = null;
				Node type = shapeAttributes.getNamedItem("type");
				if(type != null)
				{
					shapeType = type.getNodeValue();
				}
				if((shapeType != null) && !cubeTypes.contains(shapeType))
				{
					FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " is not a cube, ignoring");
					continue;
				}

				try
				{
					boolean mirrored = false;
					String[] offset = new String[3];
					String[] position = new String[3];
					String[] rotation = new String[3];
					String[] size = new String[3];
					String[] textureOffset = new String[2];

					NodeList shapeChildren = shape.getChildNodes();
					for(int j = 0; j < shapeChildren.getLength(); j++)
					{
						Node shapeChild = shapeChildren.item(j);

						String shapeChildName = shapeChild.getNodeName();
						String shapeChildValue = shapeChild.getTextContent();
						if(shapeChildValue != null)
						{
							shapeChildValue = shapeChildValue.trim();

							if(shapeChildName.equals("IsMirrored"))
							{
								mirrored = !shapeChildValue.equals("False");
							}
							else if(shapeChildName.equals("Offset"))
							{
								offset = shapeChildValue.split(",");
							}
							else if(shapeChildName.equals("Position"))
							{
								position = shapeChildValue.split(",");
							}
							else if(shapeChildName.equals("Rotation"))
							{
								rotation = shapeChildValue.split(",");
							}
							else if(shapeChildName.equals("Size"))
							{
								size = shapeChildValue.split(",");
							}
							else if(shapeChildName.equals("TextureOffset"))
							{
								textureOffset = shapeChildValue.split(",");
							}
						}
					}

					// That's what the ModelBase subclassing is needed for
					ModelRenderer cube = new ModelRenderer(this, Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));
					cube.textureHeight = textureHeight;
					cube.textureWidth = textureWidth;

					cube.mirror = mirrored;
					cube.addBox(Float.parseFloat(offset[0]), Float.parseFloat(offset[1]), Float.parseFloat(offset[2]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
					cube.setRotationPoint(Float.parseFloat(position[0]), Float.parseFloat(position[1]) - 23.4F, Float.parseFloat(position[2]));

					cube.rotateAngleX = (float)Math.toRadians(Float.parseFloat(rotation[0]));
					cube.rotateAngleY = (float)Math.toRadians(Float.parseFloat(rotation[1]));
					cube.rotateAngleZ = (float)Math.toRadians(Float.parseFloat(rotation[2]));

					parts.put(shapeName, cube);
				}
				catch(NumberFormatException e)
				{
					FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " contains malformed integers within its data, ignoring");
					e.printStackTrace();
				}
			}
		}
		catch(ZipException e)
		{
			throw new ModelFormatException("Model " + fileName + " is not a valid zip file");
		}
		catch(IOException e)
		{
			throw new ModelFormatException("Model " + fileName + " could not be read", e);
		}
		catch(ParserConfigurationException e)
		{
			// hush
		}
		catch(SAXException e)
		{
			throw new ModelFormatException("Model " + fileName + " contains invalid XML", e);
		}
	}

	@Override
	public void renderAll()
	{
		for(ModelRenderer part: parts.values())
		{
			part.renderWithRotation(1.0F);
		}
	}

	@Override
	public void renderAllExcept(String... excludedGroupNames)
	{
		int i = 0;
		for(ModelRenderer part: parts.values())
		{
			String name = (String)parts.keySet().toArray()[i];
			boolean skipPart = false;

			for(String excludedGroupName: excludedGroupNames)
			{
				if(excludedGroupName.equalsIgnoreCase(name))
				{
					skipPart = true;
				}
			}
			if(!skipPart)
			{
				part.renderWithRotation(1.0f);
			}
			i++;
		}
	}

	@Override
	public void renderOnly(String... groupNames)
	{
		int i = 0;
		for(ModelRenderer part: parts.values())
		{
			String name = (String)parts.keySet().toArray()[i];

			for(String groupName: groupNames)
			{
				if(groupName.equalsIgnoreCase(name))
				{
					part.renderWithRotation(1.0f);
				}
			}
			i++;
		}
	}

	@Override
	public void renderPart(String partName)
	{
		ModelRenderer part = parts.get(partName);
		if(part != null)
		{
			part.renderWithRotation(1.0F);
		}
	}
}
