package com.rikmuld.camping.common.obj.item

import java.util.List
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.core.ObjRegistry
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.ModInfo
import com.rikmuld.camping.core.ObjInfo

class ItemMain(infoClass: Class[ObjInfo]) extends Item {
  val info = infoClass.newInstance
  val metadata = info.NAME_META

  var iconBuffer: Array[IIcon] = null

  setHasSubtypes(metadata != null)
  ObjRegistry.register(this, info.NAME)
  setUnlocalizedName(info.NAME)
  setCreativeTab(Objs.tab)

  override def getIconFromDamage(meta: Int): IIcon = if (metadata != null) iconBuffer(meta) else itemIcon
  override def getUnlocalizedName(stack: ItemStack): String = if (metadata == null) getUnlocalizedName else metadata(stack.getItemDamage)
  @SideOnly(Side.CLIENT)
  override def getSubItems(item: Item, tab: CreativeTabs, list: List[_]) = for (meta <- 0 to (if (metadata != null) metadata.length - 1 else 0)) list.asInstanceOf[List[ItemStack]].add(new ItemStack(item, 1, meta))
  override def registerIcons(register: IIconRegister) {
    if (metadata == null) itemIcon = register.registerIcon(ModInfo.MOD_ID + ":" + getUnlocalizedName().substring(5))
    else {
      iconBuffer = new Array[IIcon](metadata.length)
      for (x <- 0 to metadata.length - 1) iconBuffer(x) = register.registerIcon(ModInfo.MOD_ID + ":" + metadata(x).toString)
    }
  }
}