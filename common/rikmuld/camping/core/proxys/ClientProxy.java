package rikmuld.camping.core.proxys;

import java.net.URISyntaxException;

import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import rikmuld.camping.client.render.block.TileEntityCampfireDecoRenderer;
import rikmuld.camping.client.render.block.TileEntityCampfireCookRenderer;
import rikmuld.camping.client.render.fx.FXColoredFlame;
import rikmuld.camping.client.render.item.RendererCampfireBaseItem;
import rikmuld.camping.client.render.item.RendererCampfireItem;
import rikmuld.camping.core.handler.TickHandler;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.misc.guide.Book;
import rikmuld.camping.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.tileentity.TileEntityCampfireCook;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	public static Book guide;
	
	@Override
	public void registerRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(ModBlocks.campfireDeco.blockID, new RendererCampfireItem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfireDeco.class, new TileEntityCampfireDecoRenderer());
		MinecraftForgeClient.registerItemRenderer(ModBlocks.campfireBase.blockID, new RendererCampfireBaseItem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfireCook.class, new TileEntityCampfireCookRenderer());
	}
	
	@Override
	public void spawnFlame(World world, double x, double y, double z, double motionX, double motionY, double motionZ, int color)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXColoredFlame(world, x, y, z, motionX, motionY, motionZ, color));
	}
	

	public void registerTickHandler()
	{
		TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
	}
	
	public void registerGuide()
	{
		try
		{
			guide = new Book(Book.class.getResource(Book.MAIN_GUIDE_PATH+"book.xml").toURI());
		}
		catch(URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}