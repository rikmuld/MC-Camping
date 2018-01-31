//package com.rikmuld.camping.inventory.objs
//
//import com.rikmuld.camping.Lib._
//import com.rikmuld.camping.inventory.{SlotItem, SlotState}
//import com.rikmuld.camping.tileentity.TileTent
//import com.rikmuld.camping.objs.BlockDefinitions
//import com.rikmuld.camping.registers.Objs
//import com.rikmuld.corerm.gui.GuiHelper
//import com.rikmuld.corerm.gui.container.ContainerSimple
//import com.rikmuld.corerm.gui.gui.GuiContainerSimple
//import com.rikmuld.corerm.utils.MathUtils
//import net.minecraft.client.Minecraft
//import net.minecraft.client.gui.inventory.GuiContainer
//import net.minecraft.client.gui.{FontRenderer, GuiButton, GuiScreen}
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.init.{Blocks, Items}
//import net.minecraft.inventory.IInventory
//import net.minecraft.item.ItemStack
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.text.translation.I18n
//import org.lwjgl.input.Mouse
//import org.lwjgl.opengl.GL11
//
//// make a tabbed GUI :)
//// sleeping tab, lantern tab, chests tab, content tab (just an inventory)
//// this also fixes the tent background lighting issue
//
//class GuiTent(player: EntityPlayer, tile: IInventory) extends GuiScreen {
//  var tent = tile.asInstanceOf[TileTent]
//  var canClick: Array[Boolean] = Array(false, false, false)
//  val bedName = new ItemStack(Items.BED).getDisplayName()
//  val lanternName = new ItemStack(Objs.lantern, 1, BlockDefinitions.Lantern.ON).getDisplayName()
//  val chestName = new ItemStack(Blocks.CHEST).getDisplayName()
//
//  val plural = I18n.translateToLocal("camping.tent.plural")
//  val manage = I18n.translateToLocal("camping.tent.manage")
//  val remove = I18n.translateToLocal("camping.tent.remove")
//  val clear = I18n.translateToLocal("camping.tent.clearall")
//  val space = I18n.translateToLocal("camping.tent.spaceLeft")
//  val sleeping = I18n.translateToLocal("camping.tent.sleeping")
//  val inventory = I18n.translateToLocal("camping.tent.inventory")
//
//  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
//    case 0 => tent.removeAll()
//    case 1 => tent.removeBed()
//    case 2 => tent.removeLantern()
//    case 3 => tent.removeChest()
//  }
//  override def doesGuiPauseGame(): Boolean = false
//  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
//    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
//  }
//
//  def checkMc = if(Option(mc).isEmpty) mc = Minecraft.getMinecraft
//  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
//    if(Option(tent).isEmpty) return
//
//    Mouse.setGrabbed(false)
//
//    checkMc
//    this.drawDefaultBackground()
//    val guiLeft = (width - 255) / 2
//    val guiTop = (height - 160) / 2
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
//    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT))
//    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 255, 160)
//    if (tent.lanterns == 0) drawTexturedModalRect(guiLeft + 32, guiTop + 78, 0, 160, 51, 53)
//    if (tent.chests == 0) drawTexturedModalRect(guiLeft + 102, guiTop + 78, 51, 160, 51, 53)
//    if (tent.beds == 0) drawTexturedModalRect(guiLeft + 172, guiTop + 78, 102, 160, 51, 53)
//    GL11.glPushMatrix()
//    drawCenteredString(fontRenderer, space + ": " + tent.contends + "/" + tent.MAX_COST, (width / 2) - 45, guiTop + 10, 0)
//    drawCenteredString(fontRenderer, bedName + plural + ": " + tent.beds + "/" + tent.MAX_BEDS, (width / 2) - 45, guiTop + 30, 0)
//    drawCenteredString(fontRenderer, lanternName + plural + ": " + tent.lanterns + "/" + tent.MAX_LANTERNS, (width / 2) - 45, guiTop + 40, 0)
//    drawCenteredString(fontRenderer, chestName + plural + ": " + tent.chests + "/" + tent.MAX_CHESTS, (width / 2) - 45, guiTop + 50, 0)
//    GL11.glScalef(0.8F, 0.8F, 0.8F)
//    drawCenteredString(fontRenderer, manage + " " + inventory, ((width / 2) * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
//    drawCenteredString(fontRenderer, manage + " " + lanternName, ((width / 2) * 1.25F).toInt - (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
//    drawCenteredString(fontRenderer, manage + " " + sleeping, ((width / 2) * 1.25F).toInt + (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
//    GL11.glPopMatrix()
//    if (isPointInRegion(172, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.beds > 0)) {
//      if (Mouse.isButtonDown(0) && canClick(0))
//        GuiHelper.openGui(Guis.TENT_SLEEP, mc.player, tent.getPos)
//      if (!Mouse.isButtonDown(0)) canClick(0) = true
//    } else canClick(0) = false
//    if (isPointInRegion(102, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.chests > 0)) {
//      if (Mouse.isButtonDown(0) && canClick(1)) GuiHelper.forceOpenGui(Guis.TENT_CHESTS, mc.player, tent.getPos)
//      if (!Mouse.isButtonDown(0)) canClick(1) = true
//    } else canClick(1) = false
//    if (isPointInRegion(32, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.lanterns > 0)) {
//      if (Mouse.isButtonDown(0) && canClick(2))  GuiHelper.forceOpenGui(Guis.TENT_LANTERNS, mc.player, tent.getPos)
//      if (!Mouse.isButtonDown(0)) canClick(2) = true
//    } else canClick(2) = false
//    super.drawScreen(mouseX, mouseY, partitialTicks)
//  }
//  override def initGui() {
//    super.initGui()
//    val guiTop = (height - 160) / 2
//    buttonList.add(new GuiButton(0, (width / 2) + 4, (guiTop + 10) - 2, 85, 10, clear))
//    buttonList.add(new GuiButton(1, (width / 2) + 4, (guiTop + 30) - 2, 85, 10, remove + " " + bedName))
//    buttonList.add(new GuiButton(2, (width / 2) + 4, (guiTop + 40) - 2, 85, 10, remove + " " + lanternName))
//    buttonList.add(new GuiButton(3, (width / 2) + 4, (guiTop + 50) - 2, 85, 10, remove + " " + chestName))
//  }
//  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
//    val pointXX = pointX - guiLeft
//    val pointYY = pointY - guiTop
//    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
//  }
//  override def updateScreen() {
//    super.updateScreen()
//    checkMc
//    if (!mc.player.isEntityAlive || mc.player.isDead) mc.player.closeScreen()
//  }
//}
//
//class GuiTentSleeping(player: EntityPlayer, tile: IInventory) extends GuiScreen {
//  var tent = tile.asInstanceOf[TileTent]
//  var canClick: Boolean = _
//  val sleep = I18n.translateToLocal("camping.tent.sleep")
//
//  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
//    case 0 => tent.sleep(mc.player)
//    case _ =>
//  }
//  def checkMc = if(Option(mc).isEmpty) mc = Minecraft.getMinecraft
//  override def doesGuiPauseGame(): Boolean = false
//  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) = fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
//  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
//    checkMc
//    drawDefaultBackground()
//    val guiLeft = (width - 97) / 2
//    val guiTop = (height - 30) / 2
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
//    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
//    drawTexturedModalRect(guiLeft, guiTop, 0, 116, 97, 30)
//    if (isPointInRegion(5, 5, 20, 20, mouseX, mouseY, guiLeft, guiTop)) {
//      drawTexturedModalRect(guiLeft + 5, guiTop + 5, 75, 0, 20, 20)
//      if (Mouse.isButtonDown(0) && canClick)
//        GuiHelper.openGui(Guis.TENT, mc.player, tent.getPos)
//      if (!Mouse.isButtonDown(0)) canClick = true
//    } else canClick = false
//    super.drawScreen(mouseX, mouseY, partitialTicks)
//  }
//  override def initGui {
//    super.initGui
//    val guiTop = (height - 30) / 2
//    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, (width / 2) + -20, guiTop + 10, 61, 10, sleep))
//  }
//  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
//    val pointXX = pointX - guiLeft
//    val pointYY = pointY - guiTop
//    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
//  }
//  override def updateScreen {
//    super.updateScreen
//    checkMc
//    if (!mc.player.isEntityAlive || mc.player.isDead) mc.player.closeScreen()
//  }
//}
//
//class GuiTentLanterns(player: EntityPlayer, inv: IInventory) extends GuiContainer(new ContainerTentLanterns(player, inv)) {
//  var tent = inv.asInstanceOf[TileTent]
//  var canClick: Boolean = _
//
//  ySize = 198
//
//  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
//    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
//  }
//
//  override def drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
//    this.drawDefaultBackground()
//    super.drawScreen(mouseX, mouseY, partialTicks)
//    this.renderHoveredToolTip(mouseX, mouseY)
//  }
//
//  protected override def drawGuiContainerBackgroundLayer(partTicks: Float, mouseX: Int, mouseY: Int) {
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
//    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
//    drawTexturedModalRect(guiLeft + 63, guiTop, 0, 0, 50, 107)
//    drawTexturedModalRect(guiLeft, guiTop + 104, 80, 165, xSize, 91)
//    if (isPointInRegion(63 + 15, 8, 20, 20, mouseX, mouseY)) {
//      drawTexturedModalRect(guiLeft + 63 + 15, guiTop + 8, 75, 0, 20, 20)
//      if (Mouse.isButtonDown(0) && canClick)
//        GuiHelper.openGui(Guis.TENT, mc.player, tent.getPos)
//      if (!Mouse.isButtonDown(0)) canClick = true
//    } else canClick = false
//    val scale = MathUtils.getScaledNumber(tent.time, 1500, 22).toInt
//    drawTexturedModalRect(guiLeft + 13 + 63, (guiTop + 83) - scale, 50, 22 - scale, 25, 22)
//  }
//}
//
//class ContainerTentLanterns(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileTent](player) {
//  override def playerInvY: Int =
//    113
//
//  def getID: String =
//    tile.getName
//
//  def addInventorySlots(): Unit =
//    addSlotToContainer(new SlotItem(tile, 0, 80, 88, Items.GLOWSTONE_DUST))
//
//  def initIInventory: TileTent =
//    tile.asInstanceOf[TileTent]
//}
//
//class GuiTentChests(player: EntityPlayer, inv: IInventory) extends GuiContainerSimple(new ContainerTentChests(player, inv)) {
//  ySize =
//    228
//
//  xSize =
//    214
//
//  val tent:TileTent =
//    inv.asInstanceOf[TileTent]
//
//  var slideState: Int = tent.slide
//  var oldSLideState: Int = _
//  var xBegin: Int = -1
//  private var slideBegin: Int = _
//  var mouseMouse: Boolean = _
//
//  var canClick: Boolean =
//    false
//
//  val getTexture: ResourceLocation =
//    new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_2)
//
//  override def drawGUI(mouseX: Int, mouseY: Int): Unit = {
//    super.drawGUI(mouseX, mouseY)
//    drawTexturedModalRect(guiLeft + 39 + slideState, guiTop + 12, 234, if (tent.chests > 2) 12 else 0, 15, 12)
//
//    oldSLideState = slideState
//
//    //add this one as a button
//    if (isPointInRegion(15, 8, 20, 20, mouseX, mouseY)) {
//      drawTexturedModalRect(guiLeft + 15, guiTop + 8, 214, 0, 20, 20)
//      if (Mouse.isButtonDown(0) && canClick)
//        GuiHelper.openGui(Guis.TENT, mc.player, tent.getPos)
//    } else canClick = false
//
//    //do with mouse drag click function
//    if (isPointInRegion(39 + slideState, 12, 15, 12, mouseX, mouseY)) {
//      if (Mouse.isButtonDown(0) && canClick) mouseMouse = true
//    } else canClick = false
//
//    //no longer needed then
//    if (!Mouse.isButtonDown(0)) {
//      canClick = true
//      mouseMouse = false
//    }
//
//    //do with mouse drag click function
//    if (mouseMouse && (tent.chests > 2)) {
//      if (xBegin == -1) {
//        xBegin = mouseX
//        slideBegin = slideState
//      }
//      slideState = (slideBegin + mouseX) - xBegin
//      if (slideState < 0) slideState = 0
//      if (slideState > 144) slideState = 144
//    } else xBegin = -1
//
//    //do with mouse drag click function
//    if (slideState != oldSLideState) tent.setSlideState(slideState)
//  }
//}
//
//class ContainerTentChests(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileTent](player) {
//  override def playerInvY: Int =
//    146
//
//  override def playerInvX: Int =
//    27
//
//  val getID: String =
//    tile.getName
//
//  override def addInventorySlots(): Unit = {
//    val slots: Array[Array[SlotState]] = Array.ofDim[SlotState](25, 6)
//
//    for (column <- 0 until 25; row <- 0 until 6) {
//      val slot = new SlotState(tile, row + (column * 6) + 1, 9 + (column * 18), 34 + (row * 18))
//      slot.disable()
//
//      addSlotToContainer(slot)
//      slots(column)(row) = slot.asInstanceOf[SlotState]
//    }
//
//    getIInventory.setSlots(slots)
//    getIInventory.manageSlots()
//  }
//
//  def initIInventory: TileTent =
//    tile.asInstanceOf[TileTent]
//
//  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
//    mergeItemStack(stack, 0, getIInventory.chests * 5 * 6, false)
//}