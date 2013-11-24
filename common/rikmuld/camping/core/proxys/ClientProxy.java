package rikmuld.camping.core.proxys;

import java.net.URISyntaxException;

import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import rikmuld.camping.client.render.block.TileEntityBerryRenderer;
import rikmuld.camping.client.render.block.TileEntityCampfireCookRenderer;
import rikmuld.camping.client.render.block.TileEntityCampfireDecoRenderer;
import rikmuld.camping.client.render.block.TileEntityLogRenderer;
import rikmuld.camping.client.render.block.TileEntitySleepingBagRenderer;
import rikmuld.camping.client.render.block.TileEntityTentRenderer;
import rikmuld.camping.client.render.fx.FXColoredFlame;
import rikmuld.camping.client.render.item.RendererBerryItem;
import rikmuld.camping.client.render.item.RendererCampfireBaseItem;
import rikmuld.camping.client.render.item.RendererCampfireItem;
import rikmuld.camping.client.render.item.RendererLogItem;
import rikmuld.camping.client.render.item.RendererTentItem;
import rikmuld.camping.core.handler.TickHandler;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.entity.tileentity.TileEntityBerry;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.entity.tileentity.TileEntityLog;
import rikmuld.camping.entity.tileentity.TileEntitySleepingBag;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.misc.guide.Book;
import rikmuld.camping.misc.version.VersionData;
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
		MinecraftForgeClient.registerItemRenderer(ModBlocks.log.blockID, new RendererLogItem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLog.class, new TileEntityLogRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySleepingBag.class, new TileEntitySleepingBagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBerry.class, new TileEntityBerryRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTent.class, new TileEntityTentRenderer());
		MinecraftForgeClient.registerItemRenderer(ModBlocks.tent.blockID, new RendererTentItem());
		MinecraftForgeClient.registerItemRenderer(ModBlocks.leaves.blockID, new RendererBerryItem());
	}
	
	@Override
	public void spawnFlame(World world, double x, double y, double z, double motionX, double motionY, double motionZ, int color)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXColoredFlame(world, x, y, z, motionX, motionY, motionZ, color));
	}
	
	@Override
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
	
	@Override
	public void checkVersion()
	{
		VersionData data = new VersionData();
		data.execute();
	}
}