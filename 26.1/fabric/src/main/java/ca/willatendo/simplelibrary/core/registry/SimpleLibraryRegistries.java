package ca.willatendo.simplelibrary.core.registry;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import ca.willatendo.simplelibrary.server.biome_modifier.BiomeModifier;
import ca.willatendo.simplelibrary.server.conditions.ICondition;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class SimpleLibraryRegistries {
    public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS = SimpleLibraryRegistries.neoforgeKey("biome_modifier");
    public static final ResourceKey<Registry<MapCodec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = SimpleLibraryRegistries.neoforgeKey("biome_modifier_serializers");
    public static final ResourceKey<Registry<MapCodec<? extends ICondition>>> CONDITION_CODECS = SimpleLibraryRegistries.neoforgeKey("condition_codecs");

    private static <T> ResourceKey<Registry<T>> neoforgeKey(String name) {
        return ResourceKey.createRegistryKey(CoreUtils.resource("neoforge", name));
    }
}
