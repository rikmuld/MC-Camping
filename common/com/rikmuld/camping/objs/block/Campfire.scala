package com.rikmuld.camping.objs.block

import java.util.ArrayList
import java.util.Random
import com.rikmuld.camping.CampingMod.proxy
import com.rikmuld.camping.Lib.NBTInfo
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.camping.objs.tile.TileCampfireWood
import com.rikmuld.corerm.misc.WorldBlock.BlockData
import com.rikmuld.corerm.misc.WorldBlock.IMBlockData
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.RMBoolProp
import com.rikmuld.corerm.objs.RMIntProp
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.corerm.objs.WithInstable
import com.rikmuld.corerm.objs.WithModel
import com.rikmuld.corerm.objs.WithProperties
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side

object Campfire {
  val ON = PropertyBool.create("on");
  val LIGHT = PropertyInteger.create("light", 0, 15);

  def particleAnimation(bd:BlockData, color:Int, random:Random, flame:Boolean){
    val motionY = (random.nextFloat() / 40F) + 0.025F
    val particleX = ((bd.x + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
    val particleY = bd.y + 0.1F + (random.nextInt(15) / 100F)
    val particleZ = ((bd.z + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
    if(flame)proxy.spawnFlame(bd.world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, color)
    bd.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D)
  }
}

class Campfire(modId:String, info: ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable {
  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB = new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)
  override def createTileEntity(world: World, meta: IBlockState): RMTile = new TileCampfire()
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, stack:ItemStack, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    val bd = (world, pos)
    if(!world.isRemote && stack != null && stack.getItem == Items.DYE && bd.tile.asInstanceOf[TileCampfire].addDye(stack)) {
      stack.stackSize-=1
      var data = player.getEntityData
      if(!data.hasKey(NBTInfo.ACHIEVEMENTS))data.setTag(NBTInfo.ACHIEVEMENTS, new NBTTagCompound())
      data = data.getTag(NBTInfo.ACHIEVEMENTS).asInstanceOf[NBTTagCompound]
      var dye = if(data.hasKey("dye_burn"))data.getInteger("dye_burn") else 0
      if(dye == 4)player.addStat(Objs.achMadCamper)
      else data.setInteger("dye_burn", dye + 1)
    }
    true
  }
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state:IBlockState, world: World, pos:BlockPos, random: Random) {
    for (i <- 0 until 3) Campfire.particleAnimation((world, pos), (world, pos).tile.asInstanceOf[TileCampfire].color, random, true)
  }
}
  
class CampfireCook(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable with WithProperties {
  setDefaultState(getStateFromMeta(0))
  
  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB = new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)
  def isOn(world:IBlockAccess, pos:BlockPos):Boolean = {
    val state = world.getBlockState(pos)
    if(state.getBlock == this) state.getValue(Campfire.ON).asInstanceOf[Boolean]
    else false
  }
  def setOn(world:World, pos:BlockPos, on:Boolean) = (world, pos).setState((world, pos).state.withProperty(Campfire.ON, on.asInstanceOf[java.lang.Boolean]))
  override def getProps = Array(new RMBoolProp(Campfire.ON, 0))
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, stack:ItemStack, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    val bd = (world, pos)
    bd.tile.asInstanceOf[TileCampfireCook].lastPlayer = player
    super.onBlockActivated(world, pos, state, player, hand, stack, side, xHit, yHit, zHit)
  }
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileCampfireCook()
  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int = if(isOn(world, pos)) 15 else 0
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state:IBlockState, world: World, pos:BlockPos, random: Random) {
    if ((world, pos).tile.asInstanceOf[TileCampfireCook].fuel > 0) {
      for (i <- 0 until 3) Campfire.particleAnimation((world, pos), 16, random, true)
    }
  }
}

class CampfireWood(modId:String, info:ObjInfo) extends Campfire(modId, info) with WithProperties {
  setDefaultState(this.getStateFromMeta(0).withProperty(Campfire.LIGHT, 0.asInstanceOf[java.lang.Integer]))
  
  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB = new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)
  def getLight(world:IBlockAccess, pos:BlockPos):Int = {
    val state = world.getBlockState(pos)
    if(state.getBlock == this){
      world.getBlockState(pos).getValue(Campfire.LIGHT).asInstanceOf[Integer]
    } else 0
  }
  def setLight(world:World, pos:BlockPos, light:Integer) = (world, pos).setState((world, pos).state.withProperty(Campfire.LIGHT, light))
  override def getProps = Array(new RMIntProp(Campfire.LIGHT, 4, 0))
  override def getDrops(world: IBlockAccess, pos:BlockPos, state:IBlockState, fortune: Int): ArrayList[ItemStack] = {
    val rand = new Random
    val stacks = new ArrayList[ItemStack]()
    
    stacks.add(new ItemStack(Items.STICK, rand.nextInt(3)+1))
    stacks
  }
  
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state:IBlockState, world: World, pos:BlockPos, random: Random) {
    val tile = world.getTileEntity(pos).asInstanceOf[TileCampfireWood]
    if(tile.isOn())super.randomDisplayTick(state, world, pos, random)
    else {
      val lid = tile.getLid
      if(lid > 0)for (i <- 0 until (lid/5).toInt + 1) Campfire.particleAnimation((world, pos), (world, pos).tile.asInstanceOf[TileCampfire].color, random, false)
    }
  }
  
  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int = getLight(world, pos)
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, stack:ItemStack, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    if(!world.isRemote){
      val bd = (world, pos)
      
      if(Option(stack).isDefined && stack.getItem == Items.STICK){
        val tile = bd.tile.asInstanceOf[TileCampfireWood]
        tile.tryLid()
      }
    }
    
    if(!world.isRemote && (world, pos).tile.asInstanceOf[TileCampfireWood].isOn())super.onBlockActivated(world, pos, state, player, hand, stack, side, xHit, yHit, zHit)
    else true
  }
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileCampfireWood()
}