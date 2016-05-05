package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.objs.tile.TileEntityTent
import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.inventory.SlotState
import com.rikmuld.corerm.inventory.SlotItemsOnly
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import net.minecraft.inventory.Slot
import com.rikmuld.camping.objs.tile.TileEntityTent
import com.rikmuld.corerm.inventory.RMContainerTile
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.client.gui.GuiButton
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.camping.objs.tile.TileEntityTent
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import com.rikmuld.corerm.RMMod
import net.minecraft.client.gui.inventory.GuiContainer
import com.rikmuld.camping.objs.misc.OpenGui
import org.lwjgl.input.Mouse
import com.rikmuld.camping.objs.tile.TileEntityTent
import com.rikmuld.camping.objs.Objs
import net.minecraft.entity.Entity
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import com.rikmuld.camping.objs.BlockDefinitions
import net.minecraft.util.StatCollector

class GuiTent(player: EntityPlayer, tile: IInventory) extends GuiScreen {
  var tent = tile.asInstanceOf[TileTent]
  var canClick: Array[Boolean] = Array(false, false, false)
  val bedName = new ItemStack(Items.bed).getDisplayName()
  val lanternName = new ItemStack(Objs.lantern, 1, BlockDefinitions.Lantern.ON).getDisplayName()
  val chestName = new ItemStack(Blocks.chest).getDisplayName()

  val plural = StatCollector.translateToLocal("camping.tent.plural") 
  val manage = StatCollector.translateToLocal("camping.tent.manage") 
  val remove = StatCollector.translateToLocal("camping.tent.remove") 
  val clear = StatCollector.translateToLocal("camping.tent.clearall") 
  val space = StatCollector.translateToLocal("camping.tent.spaceLeft")  
  val sleeping = StatCollector.translateToLocal("camping.tent.sleeping")  
  val inventory = StatCollector.translateToLocal("camping.tent.inventory")  
  
  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
    case 0 => tent.removeAll()
    case 1 => tent.removeBed()
    case 2 => tent.removeLantern()
    case 3 => tent.removeChest()
  }
  override def doesGuiPauseGame(): Boolean = false
  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  }
  def checkMc = if(Option(mc).isEmpty) mc = Minecraft.getMinecraft
  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
    checkMc
    drawDefaultBackground
    val guiLeft = (width - 255) / 2
    val guiTop = (height - 160) / 2
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 255, 160)
    if (tent.lanterns == 0) drawTexturedModalRect(guiLeft + 32, guiTop + 78, 0, 160, 51, 53)
    if (tent.chests == 0) drawTexturedModalRect(guiLeft + 102, guiTop + 78, 51, 160, 51, 53)
    if (tent.beds == 0) drawTexturedModalRect(guiLeft + 172, guiTop + 78, 102, 160, 51, 53)
    GL11.glPushMatrix()
    drawCenteredString(fontRendererObj, space + ": " + tent.contends + "/" + tent.MAX_COST, (width / 2) - 45, guiTop + 10, 0)
    drawCenteredString(fontRendererObj, bedName + plural + ": " + tent.beds + "/" + tent.MAX_BEDS, (width / 2) - 45, guiTop + 30, 0)
    drawCenteredString(fontRendererObj, lanternName + plural + ": " + tent.lanterns + "/" + tent.MAX_LANTERNS, (width / 2) - 45, guiTop + 40, 0)
    drawCenteredString(fontRendererObj, chestName + plural + ": " + tent.chests + "/" + tent.MAX_CHESTS, (width / 2) - 45, guiTop + 50, 0)
    GL11.glScalef(0.8F, 0.8F, 0.8F)
    drawCenteredString(fontRendererObj, manage + " " + inventory, ((width / 2) * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    drawCenteredString(fontRendererObj, manage + " " + lanternName, ((width / 2) * 1.25F).toInt - (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    drawCenteredString(fontRendererObj, manage + " " + sleeping, ((width / 2) * 1.25F).toInt + (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    GL11.glPopMatrix()
    if (isPointInRegion(172, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.beds > 0)) {
      if (Mouse.isButtonDown(0) && canClick(0)) mc.thePlayer.openGui(RMMod, Objs.guiTentSleep, tent.getWorld, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ)
      if (!Mouse.isButtonDown(0)) canClick(0) = true
    } else canClick(0) = false
    if (isPointInRegion(102, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.chests > 0)) {
      if (Mouse.isButtonDown(0) && canClick(1)) PacketSender.toServer(new OpenGui(Objs.guiTentChests, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ))
      if (!Mouse.isButtonDown(0)) canClick(1) = true
    } else canClick(1) = false
    if (isPointInRegion(32, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.lanterns > 0)) {
      if (Mouse.isButtonDown(0) && canClick(2)) PacketSender.toServer(new OpenGui(Objs.guiTentLantern, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ))
      if (!Mouse.isButtonDown(0)) canClick(2) = true
    } else canClick(2) = false
    super.drawScreen(mouseX, mouseY, partitialTicks)
  }
  override def initGui() {
    super.initGui()
    val guiTop = (height - 160) / 2
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, (width / 2) + 4, (guiTop + 10) - 2, 85, 10, clear))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(1, (width / 2) + 4, (guiTop + 30) - 2, 85, 10, remove + " " + bedName))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(2, (width / 2) + 4, (guiTop + 40) - 2, 85, 10, remove + " " + lanternName))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(3, (width / 2) + 4, (guiTop + 50) - 2, 85, 10, remove + " " + chestName))
  }
  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
    val pointXX = pointX - guiLeft
    val pointYY = pointY - guiTop
    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
  }
  override def updateScreen() {
    super.updateScreen()
    checkMc
    if (!mc.thePlayer.isEntityAlive || mc.thePlayer.isDead) mc.thePlayer.closeScreen()
  }
}

class GuiTentSleeping(player: EntityPlayer, tile: IInventory) extends GuiScreen {
  var tent = tile.asInstanceOf[TileTent]
  var canClick: Boolean = _
  val sleep = StatCollector.translateToLocal("camping.tent.sleep")  

  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
    case 0 => tent.sleep(mc.thePlayer)
    case _ => 
  }
  def checkMc = if(Option(mc).isEmpty) mc = Minecraft.getMinecraft
  override def doesGuiPauseGame(): Boolean = false
  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) = fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
    checkMc
    drawDefaultBackground()
    val guiLeft = (width - 97) / 2
    val guiTop = (height - 30) / 2
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
    drawTexturedModalRect(guiLeft, guiTop, 0, 116, 97, 30)
    if (isPointInRegion(5, 5, 20, 20, mouseX, mouseY, guiLeft, guiTop)) {
      drawTexturedModalRect(guiLeft + 5, guiTop + 5, 75, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick) mc.thePlayer.openGui(RMMod, Objs.guiTent, tent.getWorld, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ)
      if (!Mouse.isButtonDown(0)) canClick = true
    } else canClick = false
    super.drawScreen(mouseX, mouseY, partitialTicks)
  }
  override def initGui {
    super.initGui
    val guiTop = (height - 30) / 2
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, (width / 2) + -20, guiTop + 10, 61, 10, sleep))
  }
  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
    val pointXX = pointX - guiLeft
    val pointYY = pointY - guiTop
    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
  }
  override def updateScreen {
    super.updateScreen
    checkMc
    if (!mc.thePlayer.isEntityAlive || mc.thePlayer.isDead) mc.thePlayer.closeScreen()
  }
}

class GuiTentLanterns(player: EntityPlayer, inv: IInventory) extends GuiContainer(new ContainerTentLanterns(player, inv)) {
  var tent = inv.asInstanceOf[TileTent]
  var canClick: Boolean = _

  ySize = 198

  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  }
  protected override def drawGuiContainerBackgroundLayer(partTicks: Float, mouseX: Int, mouseY: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
    drawTexturedModalRect(guiLeft + 63, guiTop, 0, 0, 50, 107)
    drawTexturedModalRect(guiLeft, guiTop + 104, 80, 165, xSize, 91)
    if (isPointInRegion(63 + 15, 8, 20, 20, mouseX, mouseY)) {
      drawTexturedModalRect(guiLeft + 63 + 15, guiTop + 8, 75, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick) mc.thePlayer.openGui(RMMod, Objs.guiTent, tent.getWorld, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ)
      if (!Mouse.isButtonDown(0)) canClick = true
    } else canClick = false
    val scale = tent.time.getScaledNumber(1500, 22).toInt
    drawTexturedModalRect(guiLeft + 13 + 63, (guiTop + 83) - scale, 50, 22 - scale, 25, 22)
  }
}

class ContainerTentLanterns(player: EntityPlayer, tile: IInventory) extends RMContainerTile(player, tile) {
  var tent = tile.asInstanceOf[TileTent]

  addSlotToContainer(new SlotItemsOnly(tile, 0, 80, 88, Items.glowstone_dust))
  this.addSlots(player.inventory, 9, 3, 9, 8, 113)
  this.addSlots(player.inventory, 0, 1, 9, 8, 171)

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (i < tent.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tent.getSizeInventory, inventorySlots.size, true)) return null
      } else if (itemstack1.getItem() == Items.glowstone_dust) {
        if (!mergeItemStack(itemstack1, 0, tent.getSizeInventory, true)) return null
      }
      if (itemstack1.stackSize == 0) slot.putStack(null)
      else slot.onSlotChanged()
    }
    itemstack
  }
}

class GuiTentChests(player: EntityPlayer, inv: IInventory) extends GuiContainer(new ContainerTentChests(player, inv)) {
  var tent = inv.asInstanceOf[TileTent]
  var slideState: Int = tent.slide
  var oldSLideState: Int = _
  var xBegin: Int = -1
  private var slideBegin: Int = _
  var mouseMouse: Boolean = _
  var canClick: Array[Boolean] = Array(false, false)

  ySize = 228
  xSize = 214

  protected override def drawGuiContainerBackgroundLayer(partTicks: Float, mouseX: Int, mouseY: Int) {
    oldSLideState = slideState
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_2))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    if (isPointInRegion(15, 8, 20, 20, mouseX, mouseY)) {
      drawTexturedModalRect(guiLeft + 15, guiTop + 8, 214, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick(0)) mc.thePlayer.openGui(RMMod, Objs.guiTent, tent.getWorld, tent.getPos.getX, tent.getPos.getY, tent.getPos.getZ)
      if (!Mouse.isButtonDown(0)) canClick(0) = true
    } else canClick(0) = false
    if (isPointInRegion(39 + slideState, 12, 15, 12, mouseX, mouseY)) {
      if (Mouse.isButtonDown(0) && canClick(1)) mouseMouse = true
      if (!Mouse.isButtonDown(0)) canClick(1) = true
    } else canClick(1) = false
    if (mouseMouse && (tent.chests > 2)) {
      if (xBegin == -1) {
        xBegin = mouseX
        slideBegin = slideState
      }
      slideState = (slideBegin + mouseX) - xBegin
      if (slideState < 0) slideState = 0
      if (slideState > 144) slideState = 144
    } else xBegin = -1
    if (!Mouse.isButtonDown(0)) mouseMouse = false
    drawTexturedModalRect(guiLeft + 39 + slideState, guiTop + 12, 234, if (tent.chests > 2) 12 else 0, 15, 12)
    if (slideState != oldSLideState) tent.setSlideState(slideState)
  }
}

class ContainerTentChests(player: EntityPlayer, tile: IInventory) extends RMContainerTile(player, tile) {
  var tent = tile.asInstanceOf[TileTent]
  var slots: Array[Array[SlotState]] = Array.ofDim[SlotState](25, 6)

  for (collom <- 0 until 25; row <- 0 until 6) {
    val slot = new SlotState(tile, row + (collom * 6) + 1, 9 + (collom * 18), 34 + (row * 18))
    addSlotToContainer(slot)
    slot.disable
    slots(collom)(row) = slot
  }
  this.addSlots(player.inventory, 9, 3, 9, 27, 146)
  this.addSlots(player.inventory, 0, 1, 9, 27, 204)
  tent.setSlots(slots)
  tent.manageSlots()

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (i < tent.getSizeInventory - 1) {
        if (!mergeItemStack(itemstack1, tent.getSizeInventory - 1, inventorySlots.size, true)) return null
      } else {
        if (!mergeItemStack(itemstack1, 0, tent.chests * 5 * 6, false)) return null
      }
      if (itemstack1.stackSize == 0) slot.putStack(null)
      else slot.onSlotChanged()
    }
    itemstack
  }
}