package ca.willatendo.simplelibrary.core.registry;

import ca.willatendo.simplelibrary.server.biome_modifier.BiomeModifier;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import ca.willatendo.simplelibrary.server.utils.ServerUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public final class SimpleLibraryBuiltInRegistries {
    public static final Registry<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = ServerUtils.createRegistry(SimpleLibraryRegistries.BIOME_MODIFIER_SERIALIZERS, SimpleRegistryBuilder.of());
    public static final Registry<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = ServerUtils.createRegistry(SimpleLibraryRegistries.CONDITION_CODECS, SimpleRegistryBuilder.of());

    public static void init() {
    }
}
