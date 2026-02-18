package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.server.data_maps.SimpleLibraryDataMaps;
import ca.willatendo.simplelibrary.server.data_maps.buillt_in.BiomeVillagerType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerType.class)
public class VillagerTypeMixin {
    @Inject(at = @At("HEAD"), method = "byBiome", cancellable = true)
    private static void byBiome(Holder<Biome> holder, CallbackInfoReturnable<ResourceKey<VillagerType>> cir) {
        BiomeVillagerType fromDataMap = (BiomeVillagerType) holder.getData(SimpleLibraryDataMaps.VILLAGER_TYPES);
        if (fromDataMap != null) {
            cir.setReturnValue(fromDataMap.type());
        }
    }
}
