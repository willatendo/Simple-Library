package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleBannerPatternTagsProvider extends SimpleTagsProvider<BannerPattern> {
	public SimpleBannerPatternTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.BANNER_PATTERN, provider, modId, existingFileHelper);
	}
}
