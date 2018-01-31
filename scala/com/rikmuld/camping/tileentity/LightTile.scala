//package com.rikmuld.camping.tileentity
//
//import java.util.ArrayList
//
//import com.rikmuld.camping.Utils._
//import com.rikmuld.camping.misc.WorldBlock._
//import com.rikmuld.corerm.tileentity.TileEntitySimple
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.util.ITickable
//import net.minecraft.util.math.AxisAlignedBB
//
//import scala.collection.JavaConversions._
//
//class TileLight extends TileEntitySimple with ITickable {
//  var tick: Int = _
//
//  override def update {
//    if (!world.isRemote) {
//      tick += 1
//      if (tick > 10) {
//        var flag = true
//        val players = world.getEntitiesWithinAABB(classOf[EntityPlayer], new AxisAlignedBB(pos.getX - 2, pos.getY - 2, pos.getZ - 2, pos.getX + 2, pos.getY + 2, pos.getZ+ 2)).asInstanceOf[ArrayList[EntityPlayer]]
//        for (player <- players if player.hasLantarn()) flag = false
//        if (flag) (world, pos).toAir
//        else tick = 0
//      }
//    }
//  }
//}