package ca.willatendo.simplelibrary.server.biome_modifier;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public final class NoneBiomeModifier implements BiomeModifier {
    public static final NoneBiomeModifier INSTANCE = new NoneBiomeModifier();

    private NoneBiomeModifier() {
    }

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return SimpleLibraryBiomeModifierSerializers.NONE_BIOME_MODIFIER_TYPE.get();
    }
}
