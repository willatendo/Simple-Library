package willatendo.simplelibrary.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
import willatendo.simplelibrary.server.item.DyeColoursHelper;

@Mixin(DyeColor.class)
public class DyeColourMixin implements DyeColoursHelper {
	private TagKey<Item> tag;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addTags(String n, int t, int id, String name, int colour, MapColor mapColour, int fireworkColour, int textColour, CallbackInfo callbackInfo) {
		this.tag = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "dyes/" + name));
	}

	@Override
	public TagKey<Item> getTag() {
		return this.tag;
	}
}
