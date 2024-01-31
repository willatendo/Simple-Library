package willatendo.simplelibrary.data.tags;

import java.util.concurrent.CompletableFuture;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

public abstract class SimpleEntityTypeTagsProvider extends SimpleIntrinsicHolderTagsProvider<EntityType<?>> {
	public SimpleEntityTypeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.ENTITY_TYPE, provider, (entityType) -> {
			return entityType.builtInRegistryHolder().key();
		}, modId, existingFileHelper);
	}
}
