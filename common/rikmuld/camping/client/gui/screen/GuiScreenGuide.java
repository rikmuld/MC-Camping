package rikmuld.camping.client.gui.screen;

import java.net.URISyntaxException;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.proxys.ClientProxy;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.misc.guide.Book;
import rikmuld.camping.misc.guide.Page;
import rikmuld.camping.misc.guide.PageCraftData;
import rikmuld.camping.misc.guide.PageImgData;
import rikmuld.camping.misc.guide.PageItemImgData;
import rikmuld.camping.misc.guide.PageTextData;

public class GuiScreenGuide extends GuiScreen{

	int guiLeft;
	int guiTop;
	int guiWidth;
	int guiHeight;
	
	int currPage = 0;
	int maxPages;
	
	Book book;
	Page page;

	private boolean clickReady;
	private int clicker;
	
	RenderItem itemRender;
		
	public GuiScreenGuide()
	{
		this.book = ClientProxy.guide;
		this.maxPages = book.pages.size();
	}
	
	@Override
	public void initGui()
	{		
		this.guiWidth = 180;
		this.guiHeight = 193;
		this.guiLeft = (width-guiWidth)/2;
		this.guiTop = (int)((height-guiHeight)/3.8F);
		
		itemRender = new RenderItem();
	}	
	
    public void drawScreen(int mouseX, int mouseY, float par3)
    {
        super.drawScreen(mouseX, mouseY, par3);
        
        this.page = book.pages.get(currPage);
        
        if(clicker<20&&clickReady==false)clicker++;
		if(clicker>=20)
		{
			clicker = 0;
			clickReady = true;
		}
		
		this.drawBackground(mouseX, mouseY);
		this.drawCurrPage(mouseX, mouseY);
    }
    
    private void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_GUIDE));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
		
		if(this.isPointInRegion(22, 168, 17, 9, mouseX, mouseY))
		{
			this.drawTexturedModalRect(this.guiLeft+22, this.guiTop+168, 201, 11, 17, 9);
			if(Mouse.isButtonDown(0)&&clickReady&&this.currPage>0)
			{
				this.clickReady = false;
				this.currPage--;
			}
		}
		else this.drawTexturedModalRect(this.guiLeft+22, this.guiTop+168, 181, 11, 17, 9);
		
		if(this.isPointInRegion(this.guiWidth-22-17, 168, 17, 9, mouseX, mouseY))
		{
			this.drawTexturedModalRect(this.guiLeft+this.guiWidth-22-17, this.guiTop+168, 201, 0, 17, 9);
			if(Mouse.isButtonDown(0)&&clickReady&&this.currPage<this.maxPages-1)
			{
				this.clickReady = false;
				this.currPage++;
			}
		}
		else this.drawTexturedModalRect(this.guiLeft+this.guiWidth-22-17, this.guiTop+168, 181, 0, 17, 9);
		
		for(PageImgData img:page.image)
		{
			mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_GUIDE_LOCATION+img.source));
						
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0f, 0f);
			GL11.glVertex2f(guiLeft+img.x, guiTop+img.y);

			GL11.glTexCoord2f(0f, 1f);
			GL11.glVertex2f(guiLeft+img.x, guiTop+img.y+img.height/(1/img.scale));

			GL11.glTexCoord2f(1f, 1f);
			GL11.glVertex2f(guiLeft+img.x+img.width/(1/img.scale), guiTop+img.y+img.height/(1/img.scale));

			GL11.glTexCoord2f(1f, 0f);
			GL11.glVertex2f(guiLeft+img.x+img.width/(1/img.scale), guiTop+img.y);
			GL11.glEnd();
		}
		
		this.drawCenteredString(fontRenderer, String.valueOf(this.currPage+1)+"/"+this.maxPages, this.guiLeft+this.guiWidth/2, this.guiTop+this.guiHeight-20);
    }
    
    private void drawCurrPage(int mouseX, int mouseY)
	{
		for(PageTextData text:page.text)
		{
			GL11.glPushMatrix();
			GL11.glScalef(text.size, text.size, text.size);
			this.fontRenderer.drawSplitString(text.text, (int)((this.guiLeft+text.x)*(1/text.size)), (int)((this.guiTop+text.y)*(1/text.size)), (int)(text.width*(1/text.size)), 0);
			GL11.glPopMatrix();
		}
		
		RenderHelper.enableGUIStandardItemLighting();

		for(PageCraftData craft:page.crafting)
		{
			mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_GUIDE));
			
			this.drawTexturedModalRect(guiLeft+craft.x, guiTop+craft.y, 180, 22, 50, 50);
		
			for(int i = 0; i<3; i++)
			{
				for(int k = 0; k<3; k++)
				{
					if(craft.stacks.get(i*3+k)!=null)itemRender.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, craft.stacks.get(i*3+k), this.guiLeft+craft.x+k*17, this.guiTop+craft.y+i*17);
				}
			}
			
			this.drawCenteredString(fontRenderer, (craft.shapeless? "shapeless":"shaped"), guiLeft+craft.x+25, guiTop+craft.y+53);
		}

		RenderHelper.enableGUIStandardItemLighting();
		for(PageItemImgData item:page.item)itemRender.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, item.stack, this.guiLeft+item.x, this.guiTop+item.y);
        GL11.glDisable(GL11.GL_LIGHTING);
	}

	protected boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY)
    {
        pointX -= guiLeft;
        pointY -= guiTop;
        return pointX >= x - 1 && pointX < x + width + 1 && pointY >= y - 1 && pointY < y + height + 1;
    }
	
	public void drawCenteredString(FontRenderer fontrenderer, String data, int x, int y)
    {
		fontrenderer.drawString(data, x - fontrenderer.getStringWidth(data) / 2, y, 0, false);
    }
}
