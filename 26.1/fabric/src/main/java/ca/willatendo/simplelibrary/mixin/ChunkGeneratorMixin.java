package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.ChunkGeneratorExtension;
import ca.willatendo.simplelibrary.server.utils.Lazy;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin implements ChunkGeneratorExtension {
    @Shadow
    @Final
    protected BiomeSource biomeSource;
    @Shadow
    @Final
    private Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep;
    @Shadow
    @Final
    private Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter;

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;featuresPerStep:Ljava/util/function/Supplier;", opcode = Opcodes.PUTFIELD), method = "<init>(Lnet/minecraft/world/level/biome/BiomeSource;Ljava/util/function/Function;)V")
    private void init(ChunkGenerator chunkGenerator, Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep) {
        chunkGenerator.featuresPerStep = Lazy.of(() -> FeatureSorter.buildFeaturesPerStep(List.copyOf(biomeSource.possibleBiomes()), biomeHolder -> generationSettingsGetter.apply(biomeHolder).features(), true));
    }


    @Override
    public void refreshFeaturesPerStep() {
        ((Lazy) this.featuresPerStep).invalidate();
    }
}
