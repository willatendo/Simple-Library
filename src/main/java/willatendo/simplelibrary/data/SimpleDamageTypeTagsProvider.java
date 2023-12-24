package willatendo.simplelibrary.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public abstract class SimpleDamageTypeTagsProvider extends SimpleTagsProvider<DamageType> {
	public SimpleDamageTypeTagsProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, Registries.DAMAGE_TYPE, provider, modId, existingFileHelper);
	}
}
