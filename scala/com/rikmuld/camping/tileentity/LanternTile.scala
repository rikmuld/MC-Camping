//package com.rikmuld.camping.tileentity
//
//import com.rikmuld.camping.objs.BlockDefinitions
//import com.rikmuld.camping.objs.blocks.Lantern
//import com.rikmuld.corerm.tileentity.TileEntitySimple
//import net.minecraft.nbt.NBTTagCompound
//import net.minecraft.util.ITickable
//import com.rikmuld.camping.misc.WorldBlock._
//
//class TileLantern extends TileEntitySimple with ITickable {
//  var burnTime: Int = _
//  var ticker: Int = _
//
//  def bd: BlockData =
//    (world, pos)
//  override def readFromNBT(tag: NBTTagCompound) {
//    burnTime = tag.getInteger("burnTime")
//    super.readFromNBT(tag)
//  }
//  override def update {
//    if (!world.isRemote) {
//      ticker += 1
//      if (ticker >= 10) {
//        ticker = 0
//        if (burnTime > 0) burnTime -= 1
//      }
//      if (burnTime <= 0 && bd.meta == BlockDefinitions.Lantern.ON) {
//        bd.setMeta(BlockDefinitions.Lantern.OFF)
//        bd.update
//      }
//    }
//  }
//  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound =  {
//    tag.setInteger("burnTime", burnTime)
//    super.writeToNBT(tag)
//  }
//  def isLit = bd.state.getValue(Lantern.LIT).asInstanceOf[Boolean]
//  def isTop = bd.state.getValue(Lantern.TOP).asInstanceOf[Boolean]
//}