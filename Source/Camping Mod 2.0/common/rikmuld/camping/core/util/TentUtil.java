package rikmuld.camping.core.util;

import rikmuld.camping.core.lib.TextureInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class TentUtil {

	public static ResourceLocation getTentTexture(int color)
	{
		switch(color)
		{
			case 0: return new ResourceLocation(TextureInfo.MODEL_TENT_BLACK);
			case 1: return new ResourceLocation(TextureInfo.MODEL_TENT_RED);
			case 2: return new ResourceLocation(TextureInfo.MODEL_TENT_GREEN);
			case 3: return new ResourceLocation(TextureInfo.MODEL_TENT_BROWN);
			case 4: return new ResourceLocation(TextureInfo.MODEL_TENT_BLUE);
			case 5: return new ResourceLocation(TextureInfo.MODEL_TENT_PURPLE);
			case 6: return new ResourceLocation(TextureInfo.MODEL_TENT_CYAN);
			case 7: return new ResourceLocation(TextureInfo.MODEL_TENT_LIGHTGRAY);
			case 8: return new ResourceLocation(TextureInfo.MODEL_TENT_GRAY);
			case 9: return new ResourceLocation(TextureInfo.MODEL_TENT_PINK);
			case 10: return new ResourceLocation(TextureInfo.MODEL_TENT_LIME);
			case 11: return new ResourceLocation(TextureInfo.MODEL_TENT_YELLOW);
			case 12: return new ResourceLocation(TextureInfo.MODEL_TENT_LIGHTBLUE);
			case 13: return new ResourceLocation(TextureInfo.MODEL_TENT_MAGENTA);
			case 14: return new ResourceLocation(TextureInfo.MODEL_TENT_ORANGE);
			case 15: return new ResourceLocation(TextureInfo.MODEL_TENT_WHITE);
			default: return new ResourceLocation(TextureInfo.MODEL_TENT_WHITE);
		}
	}
}
