package rikmuld.camping.core.register;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoBoolean;
import rikmuld.camping.core.lib.EntityInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.entity.EntityBear;
import rikmuld.camping.entity.EntityCamper;
import rikmuld.camping.entity.EntityDeer;
import rikmuld.camping.entity.EntityHare;
import rikmuld.camping.misc.creativeTab.TabMain;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModEntitys {

	public static int startEntityId = 300;

	public static BiomeGenBase[] forests = BiomeDictionary.getBiomesForType(Type.FOREST);
	public static BiomeGenBase[] plains = BiomeDictionary.getBiomesForType(Type.PLAINS);

	public static int getUniqueEntityId()
	{
		while(EntityList.getStringFromID(startEntityId) != null)
		{
			startEntityId++;
		}
		return startEntityId;
	}

	public static void init()
	{
		if(ConfigInfoBoolean.value(ConfigInfo.USE_BEARS))
		{
			registerEntity(EntityBear.class, "Grizzly Bear", EntityInfo.BEAR, true, 0x583B2D, 0xE2B572);

			for(BiomeGenBase biome: forests)
			{
				EntityRegistry.addSpawn(EntityBear.class, 3, 2, 4, EnumCreatureType.creature, biome);
			}
			EntityRegistry.addSpawn(EntityBear.class, 3, 2, 4, EnumCreatureType.creature, BiomeGenBase.river);
		}
		if(ConfigInfoBoolean.value(ConfigInfo.USE_DEERS))
		{
			registerEntity(EntityDeer.class, "Deer", EntityInfo.DEER, true, 0xb57d41, 0xe8cfa0);
			for(BiomeGenBase biome: forests)
			{
				EntityRegistry.addSpawn(EntityDeer.class, 4, 3, 4, EnumCreatureType.creature, biome);
			}
			for(BiomeGenBase biome: plains)
			{
				EntityRegistry.addSpawn(EntityDeer.class, 6, 3, 4, EnumCreatureType.creature, biome);
			}
		}
		if(ConfigInfoBoolean.value(ConfigInfo.USE_HARES))
		{
			registerEntity(EntityHare.class, "Hare", EntityInfo.HARE, true, 0xcccccc, 0xe882a0);
			for(BiomeGenBase biome: forests)
			{
				EntityRegistry.addSpawn(EntityHare.class, 4, 3, 4, EnumCreatureType.creature, biome);
			}
			for(BiomeGenBase biome: plains)
			{
				EntityRegistry.addSpawn(EntityHare.class, 6, 3, 4, EnumCreatureType.creature, biome);
			}
		}
		if(ConfigInfoBoolean.value(ConfigInfo.USE_CAMPERS))
		{
			registerEntity(EntityCamper.class, "Camper", EntityInfo.CAMPER, true, 0x747B51, 0x70471B);
		}
		registerEntity(EntityDeer.class, "Log Seat", EntityInfo.LOG, false, 0, 0);
	}

	public static void registerEntity(Class<? extends Entity> entity, String name, int id, boolean hasEgg, int primeColor, int secColor)
	{
		EntityRegistry.registerModEntity(entity, name, id, CampingMod.instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + ModInfo.MOD_ID + "." + name + ".name", name);
		if(hasEgg)
		{
			registerEntityEgg(entity, primeColor, secColor);
		}
	}

	public static void registerEntityEgg(Class<? extends Entity> entity, int primaryColor, int secondaryColor)
	{
		int id = getUniqueEntityId();
		EntityList.IDtoClassMapping.put(id, entity);
		EntityList.entityEggs.put(id, new EntityEggInfo(id, primaryColor, secondaryColor));

		TabMain.eggIds.add(id);
	}
}
