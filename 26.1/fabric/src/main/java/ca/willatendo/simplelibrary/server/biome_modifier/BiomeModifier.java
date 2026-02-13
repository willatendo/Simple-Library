package ca.willatendo.simplelibrary.server.biome_modifier;

import ca.willatendo.simplelibrary.core.registry.SimpleLibraryBuiltInRegistries;
import ca.willatendo.simplelibrary.core.registry.SimpleLibraryRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

// Modified from Neoforge
public interface BiomeModifier {
    Codec<BiomeModifier> DIRECT_CODEC = SimpleLibraryBuiltInRegistries.BIOME_MODIFIER_SERIALIZERS.byNameCodec().dispatch(BiomeModifier::codec, Function.identity());
    Codec<Holder<BiomeModifier>> REFERENCE_CODEC = RegistryFileCodec.create(SimpleLibraryRegistries.BIOME_MODIFIERS, DIRECT_CODEC);
    Codec<HolderSet<BiomeModifier>> LIST_CODEC = RegistryCodecs.homogeneousList(SimpleLibraryRegistries.BIOME_MODIFIERS, DIRECT_CODEC);

    void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder);

    MapCodec<? extends BiomeModifier> codec();

    enum Phase {
        BEFORE_EVERYTHING,
        ADD,
        REMOVE,
        MODIFY,
        AFTER_EVERYTHING
    }
}
