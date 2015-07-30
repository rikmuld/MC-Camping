package com.rikmuld.camping.objs.block

import com.rikmuld.corerm.objs.RMBlockContainer
import com.rikmuld.corerm.objs.WithInstable
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.CampingMod._
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.init.Blocks
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random
import java.util.ArrayList
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import com.rikmuld.camping.objs.Objs
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.corerm.objs.ObjInfo
import net.minecraft.init.Items
import net.minecraft.util.BlockPos
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import com.rikmuld.corerm.misc.WorldBlock._
import net.minecraft.util.EnumParticleTypes
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.Lib.NBTInfo
import net.minecraft.nbt.NBTTagCompound

object Campfire {
  def particleAnimation(bd:BlockData, color:Int, random:Random){
    val motionY = (random.nextFloat() / 40F) + 0.025F
    val particleX = ((bd.x + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
    val particleY = bd.y + 0.1F + (random.nextInt(15) / 100F)
    val particleZ = ((bd.z + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
    proxy.spawnFlame(bd.world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, color)
    bd.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D)
  }
}

class Campfire(modId:String, info: ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable {
  setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  override def getRenderType = 3
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileCampfire()
  override def getDrops(world: IBlockAccess, pos:BlockPos, state:IBlockState, fortune: Int): ArrayList[ItemStack] = {
    val stacks = new ArrayList[ItemStack]()
    stacks.add(new ItemStack(Items.stick, new Random().nextInt(4) + 1, 0))
    stacks.add(new ItemStack(Objs.campfireCook, 1, 0))
    stacks
  }
  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    val bd = (world, pos)
    if ((player.getCurrentEquippedItem != null) && (player.getCurrentEquippedItem.getItem == Objs.knife)) {
      if (!player.inventory.addItemStackToInventory(new ItemStack(Objs.campfire))) world.dropItemInWorld(new ItemStack(Objs.campfire), bd.x, bd.y, bd.z, new Random())
      breakBlock(world, pos, state)
      bd.toAir
      player.getCurrentEquippedItem.addDamage(player, 1)
    } else if(!world.isRemote && player.getCurrentEquippedItem != null && player.getCurrentEquippedItem.getItem == Items.dye && bd.tile.asInstanceOf[TileCampfire].addDye(player.getCurrentEquippedItem)) {
      player.getCurrentEquippedItem.stackSize-=1
      var data = player.getEntityData
      if(!data.hasKey(NBTInfo.ACHIEVEMENTS))data.setTag(NBTInfo.ACHIEVEMENTS, new NBTTagCompound())
      data = data.getTag(NBTInfo.ACHIEVEMENTS).asInstanceOf[NBTTagCompound]
      var dye = if(data.hasKey("dye_burn"))data.getInteger("dye_burn") else 0
      if(dye == 4)player.triggerAchievement(Objs.achMadCamper)
      else data.setInteger("dye_burn", dye + 1)
    }
    true
  }
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(world: World, pos:BlockPos, state:IBlockState, random: Random) {
    for (i <- 0 until 3) Campfire.particleAnimation((world, pos), (world, pos).tile.asInstanceOf[TileCampfire].color, random)
  }
}
  
class CampfireCook(modId:String, info:ObjInfo) extends RMBlockContainer(modId, info) with WithModel with WithInstable {
  setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
    val bd = (world, pos)
    bd.tile.asInstanceOf[TileCampfireCook].lastPlayer = player
    super.onBlockActivated(world, pos, state, player, side, xHit, yHit, zHit)
  }
  override def getRenderType = 3
  override def createNewTileEntity(world: World, meta: Int): RMTile = new TileCampfireCook()
  override def getLightValue(world: IBlockAccess, pos:BlockPos): Int = {
    var fuel = 0
    Option(world.getTileEntity(pos)).map { tile => fuel = tile.asInstanceOf[TileCampfireCook].fuel }
    if(fuel > 0) 15 else 0
  }
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(world: World, pos:BlockPos, state:IBlockState, random: Random) {
    if ((world, pos).tile.asInstanceOf[TileCampfireCook].fuel > 0) {
      for (i <- 0 until 3) Campfire.particleAnimation((world, pos), 16, random)
    }
  }
}