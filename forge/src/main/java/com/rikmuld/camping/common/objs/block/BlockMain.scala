package com.rikmuld.camping.common.obj.block

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraft.world.IWorldAccess
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraft.item.ItemStack
import net.minecraft.init.Blocks
import java.util.Random
import net.minecraft.block.Block
import com.rikmuld.camping.core.ObjRegistry
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.ModInfo
import com.rikmuld.camping.core.ObjInfo
import com.rikmuld.camping.common.objs.tile.TileEntityWithRotation

class BlockMain(infoClass: Class[ObjInfo], material: Material, itemBlock: Class[ItemBlock], useSides: Boolean, icon: Boolean) extends BlockContainer(material) {
  val info = infoClass.newInstance
  val metadata: Array[String] = info.NAME_META

  var iconBuffer: Array[Array[IIcon]] = null

  setBlockName(info.NAME)
  setCreativeTab(Objs.tab)
  if (metadata != null) ObjRegistry.register(this, info.NAME, itemBlock) else ObjRegistry.register(this, info.NAME)

  def this(infoClass: Class[ObjInfo], material: Material) {
    this(infoClass, material, null, false, true)
  }
  override def createNewTileEntity(world: World, meta: Int): TileEntity = null
  override def getIcon(side: Int, meta: Int): IIcon = {
    if (useSides == true && icon) blockIcon = iconBuffer(meta)(side)
    else if (this.metadata != null && icon) blockIcon = iconBuffer(meta)(0)
    blockIcon
  }
  def getSides(meta: Int): Array[String] = {
    val sides: Array[String] = new Array[String](6)
    for (i <- 0 to sides.length - 1) sides(i) = "side"
    sides
  }
  override def registerBlockIcons(register: IIconRegister) {
    if (metadata == null&&icon) {
      if (useSides == false) blockIcon = register.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName.substring(5))
      else {
        iconBuffer = Array.ofDim[IIcon](1, 6)
        for (x <- 0 to 5) iconBuffer(0)(x) = register.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName.substring(5) + "_" + getSides(0)(x))
      }
    } else if(icon){
      if (useSides == false) {
        iconBuffer = Array.ofDim[IIcon](metadata.length, 1)
        for (x <- 0 to metadata.length - 1) iconBuffer(x)(1) = register.registerIcon(ModInfo.MOD_ID + ":" + metadata(x).toString)
      } else {
        iconBuffer = Array.ofDim[IIcon](metadata.length, 6)
        for (x <- 0 to metadata.length - 1; y <- 0 to 5) iconBuffer(x)(y) = register.registerIcon(ModInfo.MOD_ID + ":" + metadata(x).toString + "_" + getSides(x)(y))
      }
    }
  }
}

trait BlockWithModel extends BlockContainer {
  override def getRenderType() = -1
  override def isReplaceable(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = false
  override def isOpaqueCube() = false
  override def renderAsNormalBlock() = false
}

trait BlockWithRotation extends BlockContainer {
  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entityLiving: EntityLivingBase, itemStack: ItemStack) {
    var direction = 0
    val facing = MathHelper.floor_double(((entityLiving.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 3
    if (facing == 0) direction = ForgeDirection.NORTH.ordinal() - 2
    else if (facing == 1) direction = ForgeDirection.SOUTH.ordinal() - 2
    else if (facing == 2) direction = ForgeDirection.WEST.ordinal() - 2
    else if (facing == 3) direction = ForgeDirection.EAST.ordinal() - 2
    world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].setRotation(direction)
  }
  def rotate(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) {
    world.getTileEntity(x, y, z).asInstanceOf[TileEntityWithRotation].cycleRotation();
  }
}

trait BlockWithInstability extends BlockContainer {
  override def canPlaceBlockAt(world:World, x:Int, y:Int, z:Int) = ((world.getBlock(x, y, z) == null) || world.getBlock(x, y, z).isReplaceable(world, x, y, z)) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
  def dropIfCantStay(world:World, x:Int, y:Int, z:Int) {
    if((!World.doesBlockHaveSolidTopSurface(world, x, y-1, z))) {
      dropBlockAsItemWithChance(world, x, y, z, 0, 1, 1)
      world.setBlock(x, y, z, Blocks.air)
    }
  }
  override def onBlockAdded(world:World, x:Int, y:Int, z:Int) = if(!world.isRemote)dropIfCantStay(world, x, y, z)
  override def onNeighborBlockChange(world:World, x:Int, y:Int, z:Int, block:Block) = if(!world.isRemote)dropIfCantStay(world, x, y, z)
  override def updateTick(world:World, x:Int, y:Int, z:Int, random:Random) = if(!world.isRemote)dropIfCantStay(world, x, y, z)
}