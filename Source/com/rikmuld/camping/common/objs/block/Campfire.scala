package com.rikmuld.camping.common.objs.block

import com.rikmuld.camping.core.ObjInfo
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.ObjRegistry
import com.rikmuld.camping.CampingMod
import net.minecraft.util.IIcon
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.init.Blocks
import cpw.mods.fml.relauncher.SideOnly
import java.util.Random
import java.util.ArrayList
import net.minecraft.block.Block
import com.rikmuld.camping.core.GuiInfo
import net.minecraft.init.Items
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire
import net.minecraft.world.IBlockAccess
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook

class Campfire(infoClass: Class[_]) extends BlockMain(infoClass, Material.fire, false, false) with BlockWithModel with BlockWithInstability {
  setHardness(3.0F)
  setLightLevel(1.0F)
  setStepSound(Block.soundTypeStone)
  setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    world.dropBlockItems(x, y, z, new Random())
    super.breakBlock(world, x, y, z, block, meta)
  }
  override def createTileEntity(world: World, meta: Int): TileEntity = new TileEntityCampfire()
  override def getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): ArrayList[ItemStack] = {
    val stacks = new ArrayList[ItemStack]()
    stacks.add(new ItemStack(Items.stick, world.rand.nextInt(4) + 1, 0))
    //stacks.add(new ItemStack(Objs.campfireCook, 1, 0))
    stacks
  }
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.planks.getIcon(0, 0)
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if ((player.getCurrentEquippedItem != null) && (player.getCurrentEquippedItem.getItem == Objs.knife)) {
      if (!player.inventory.addItemStackToInventory(new ItemStack(Objs.campfire))) {
        world.dropItemInWorld(new ItemStack(Objs.campfire, 1, 0), x, y, z, new Random())
      }
      breakBlock(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z))
      world.setBlock(x, y, z, Blocks.air)
      player.getCurrentEquippedItem.addDamage(player, 1)
    }
    if (!world.isRemote) {
      if ((player.getCurrentEquippedItem == null) ||
        !(player.getCurrentEquippedItem.getItem == Objs.knife)) {
        player.openGui(CampingMod, GuiInfo.GUI_CAMPFIRE, world, x, y, z)
      }
    }
    true
  }
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(world: World, x: Int, y: Int, z: Int, random: Random) {
    for (i <- 0 until 3) {
      val motionY = (random.nextFloat() / 40F) + 0.025F
      val particleX = ((x + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
      val particleY = y + 0.1F + (random.nextInt(15) / 100F)
      val particleZ = ((z + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
      CampingMod.proxy.spawnFlame(world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, world.getTileEntity(x, y, z).asInstanceOf[TileEntityCampfire].color)
      world.spawnParticle("smoke", particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D)
    }
  }
}

class CampfireCook(infoClass: Class[_]) extends BlockMain(infoClass, Material.fire, false, false) with BlockWithModel with BlockWithInstability {
  setHardness(2.0F)
  setStepSound(Block.soundTypeStone)
  setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
    world.dropBlockItems(x, y, z, new Random())
    super.breakBlock(world, x, y, z, block, meta)
  }
  override def createTileEntity(world: World, meta: Int): TileEntity = new TileEntityCampfireCook()
  override def getIcon(side: Int, metadata: Int): IIcon = Blocks.stone.getIcon(0, 0)
  override def getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityCampfireCook]
    if (tile.fuel > 0) 15 else 0
  }
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (!world.isRemote) player.openGui(CampingMod, GuiInfo.GUI_CAMPFIRE_COOK, world, x, y, z)
    true
  }
  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(world: World, x: Int, y: Int, z: Int, random: Random) {
    val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityCampfireCook]
    if (tile.fuel > 0) {
      for (i <- 0 until 3) {
        val motionY = (random.nextFloat() / 40F) + 0.025F
        val particleX = ((x + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
        val particleY = y + 0.1F + (random.nextInt(15) / 100F)
        val particleZ = ((z + 0.5F) - 0.15F) + (random.nextInt(30) / 100F)
        CampingMod.proxy.spawnFlame(world, particleX, particleY, particleZ, 0.0F, motionY, 0.0F, 16)
        world.spawnParticle("smoke", particleX, particleY, particleZ, 0.0D, 0.05D, 0.0D)
      }
    }
  }
}