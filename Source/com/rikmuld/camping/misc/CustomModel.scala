package com.rikmuld.camping.misc

import java.net.URL
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.IModelCustomLoader
import net.minecraftforge.client.model.ModelFormatException
import CustomModelLoader._
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.util.Arrays
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraftforge.client.model.IModelCustom
import net.minecraftforge.client.model.ModelFormatException
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import cpw.mods.fml.common.FMLLog
import CustomModel._
import scala.collection.JavaConversions._
import net.minecraft.util.ResourceLocation
import net.minecraft.client.resources.IResource
import net.minecraft.client.Minecraft
import java.io.InputStream

object CustomModelLoader {
  private val types = Array("tcn")
}

class CustomModelLoader extends IModelCustomLoader {
  var width: Int = _
  var height: Int = _

  override def getSuffixes(): Array[String] = types
  override def getType(): String = "Techne model"
  def loadInstance(width: Int, height: Int, resource: ResourceLocation): IModelCustom = {
    this.width = width
    this.height = height
    this.loadInstance(resource)
  }
  override def loadInstance(resource: ResourceLocation): IModelCustom = new CustomModel(resource, width, height)
}

object CustomModel {
  val cubeTypes = Arrays.asList("d9e621f7-957f-4b77-b1ae-20dcd0da7751", "de81aa14-bd60-4228-8d8d-5238bcd3caaa")
}

class CustomModel(resource: ResourceLocation, var width: Int, var height: Int) extends ModelBase with IModelCustom {
  private var zipContents: Map[String, Array[Byte]] = new HashMap[String, Array[Byte]]()
  private var parts: Map[String, ModelRenderer] = new LinkedHashMap[String, ModelRenderer]()
  private var texture: String = null
  var fileName = resource.toString()

  this.textureWidth = width
  this.textureHeight = height
  val res = Minecraft.getMinecraft().getResourceManager().getResource(resource)
  loadTechneModel(res.getInputStream)

  override def getType(): String = "tcn"
  private def loadTechneModel(stream: InputStream) {
    try {
      val zipInput = new ZipInputStream(stream)
      var entry: ZipEntry = zipInput.getNextEntry
      while (entry != null) {
        val data = Array.ofDim[Byte](entry.getSize.toInt)
        var i = 0
        while (zipInput.available() > 0 && i < data.length) {
          data(i) = zipInput.read().toByte
          i += 1
        }
        zipContents.put(entry.getName, data)
        entry = zipInput.getNextEntry
      }
      val modelXml = zipContents.get("model.xml")
      if (modelXml == null) throw new ModelFormatException("Model " + fileName + " contains no model.xml file")
      val documentBuilderFactory = DocumentBuilderFactory.newInstance()
      val documentBuilder = documentBuilderFactory.newDocumentBuilder()
      val document = documentBuilder.parse(new ByteArrayInputStream(modelXml))
      val nodeListTechne = document.getElementsByTagName("Techne")
      if (nodeListTechne.getLength < 1) throw new ModelFormatException("Model " + fileName + " contains no Techne tag")
      val nodeListModel = document.getElementsByTagName("Model")
      if (nodeListModel.getLength < 1) throw new ModelFormatException("Model " + fileName + " contains no Model tag")
      val modelAttributes = nodeListModel.item(0).getAttributes
      if (modelAttributes == null) throw new ModelFormatException("Model " + fileName + " contains a Model tag with no attributes")
      val modelTexture = modelAttributes.getNamedItem("texture")
      if (modelTexture != null) texture = modelTexture.getTextContent
      val shapes = document.getElementsByTagName("Shape")
      for (i <- 0 until shapes.getLength) {
        val shape = shapes.item(i)
        val shapeAttributes = shape.getAttributes
        if (shapeAttributes == null) throw new ModelFormatException("Shape #" + (i + 1) + " in " + fileName + " has no attributes")
        val name = shapeAttributes.getNamedItem("name")
        var shapeName: String = null
        if (name != null) shapeName = name.getNodeValue
        if (shapeName == null) shapeName = "Shape #" + (i + 1)
        var shapeType: String = null
        val `type` = shapeAttributes.getNamedItem("type")
        if (`type` != null) shapeType = `type`.getNodeValue
        if ((shapeType != null) && !cubeTypes.contains(shapeType)) {
          FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " is not a cube, ignoring")
        }
        try {
          var mirrored = false
          var offset = Array.ofDim[String](3)
          var position = Array.ofDim[String](3)
          var rotation = Array.ofDim[String](3)
          var size = Array.ofDim[String](3)
          var textureOffset = Array.ofDim[String](2)
          val shapeChildren = shape.getChildNodes
          for (j <- 0 until shapeChildren.getLength) {
            val shapeChild = shapeChildren.item(j)
            val shapeChildName = shapeChild.getNodeName
            var shapeChildValue = shapeChild.getTextContent
            if (shapeChildValue != null) {
              shapeChildValue = shapeChildValue.trim()
              if (shapeChildName == "IsMirrored") mirrored = shapeChildValue != "False"
              else if (shapeChildName == "Offset") offset = shapeChildValue.split(",")
              else if (shapeChildName == "Position") position = shapeChildValue.split(",")
              else if (shapeChildName == "Rotation") rotation = shapeChildValue.split(",")
              else if (shapeChildName == "Size") size = shapeChildValue.split(",")
              else if (shapeChildName == "TextureOffset") textureOffset = shapeChildValue.split(",")
            }
          }
          val cube = new ModelRenderer(this, java.lang.Integer.parseInt(textureOffset(0)), java.lang.Integer.parseInt(textureOffset(1)))
          cube.textureHeight = textureHeight
          cube.textureWidth = textureWidth
          cube.mirror = mirrored
          cube.addBox(java.lang.Float.parseFloat(offset(0)), java.lang.Float.parseFloat(offset(1)), java.lang.Float.parseFloat(offset(2)), java.lang.Integer.parseInt(size(0)), java.lang.Integer.parseInt(size(1)), java.lang.Integer.parseInt(size(2)))
          cube.setRotationPoint(java.lang.Float.parseFloat(position(0)), java.lang.Float.parseFloat(position(1)) - 23.4F, java.lang.Float.parseFloat(position(2)))
          cube.rotateAngleX = Math.toRadians(java.lang.Float.parseFloat(rotation(0))).toFloat
          cube.rotateAngleY = Math.toRadians(java.lang.Float.parseFloat(rotation(1))).toFloat
          cube.rotateAngleZ = Math.toRadians(java.lang.Float.parseFloat(rotation(2))).toFloat
          parts.put(shapeName, cube)
        } catch {
          case e: NumberFormatException => {
            FMLLog.warning("Model shape [" + shapeName + "] in " + fileName + " contains malformed integers within its data, ignoring")
            e.printStackTrace()
          }
        }
      }
    } catch {
      case e: ZipException => throw new ModelFormatException("Model " + fileName + " is not a valid zip file")
      case e: IOException => throw new ModelFormatException("Model " + fileName + " could not be read", e)
      case e: ParserConfigurationException =>
      case e: SAXException => throw new ModelFormatException("Model " + fileName + " contains invalid XML", e)
    }
  }
  override def renderAll() {
    for (part <- parts.values) part.renderWithRotation(1.0F)
  }
  override def renderAllExcept(excludedGroupNames: String*) {
    var i = 0
    for (part <- parts.values) {
      val name = parts.keySet().toArray()(i).asInstanceOf[String]
      var skipPart = false
      for (excludedGroupName <- excludedGroupNames if excludedGroupName.equalsIgnoreCase(name)) skipPart = true
      if (!skipPart) part.renderWithRotation(1.0f)
      i += 1
    }
  }
  override def renderOnly(groupNames: String*) {
    var i = 0
    for (part <- parts.values) {
      val name = parts.keySet().toArray()(i).asInstanceOf[String]
      for (groupName <- groupNames if groupName.equalsIgnoreCase(name)) part.renderWithRotation(1.0f)
      i += 1
    }
  }
  override def renderPart(partName: String) {
    val part = parts.get(partName)
    if (part != null) part.renderWithRotation(1.0F)
  }
}