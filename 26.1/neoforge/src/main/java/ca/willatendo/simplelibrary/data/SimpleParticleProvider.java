package ca.willatendo.simplelibrary.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleParticleProvider implements DataProvider {
    private final Map<Identifier, ParticleInfo> particles = new HashMap<>();
    private final PackOutput packOutput;
    private final String modId;

    public SimpleParticleProvider(PackOutput packOutput, String modId) {
        this.packOutput = packOutput;
        this.modId = modId;
    }

    protected abstract void getAll();

    protected void add(ParticleType<?> particleType, Identifier... textures) {
        this.particles.put(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType), new ParticleInfo(textures));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.getAll();
        List<CompletableFuture<?>> completableFutures = new ArrayList<>();
        this.particles.forEach((particle, particleInfo) -> completableFutures.add(DataProvider.saveStable(cachedOutput, ParticleInfo.CODEC, particleInfo, this.packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles").json(particle))));
        return CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Particles: " + this.modId;
    }

    record ParticleInfo(List<Identifier> textures) {
        static final Codec<ParticleInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(ParticleInfo::textures)).apply(instance, ParticleInfo::new));

        public ParticleInfo(Identifier[] textures) {
            this(List.of(textures));
        }
    }
}
