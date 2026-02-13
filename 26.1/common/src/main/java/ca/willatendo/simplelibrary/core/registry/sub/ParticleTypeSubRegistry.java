package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.platform.SimpleLibraryPlatformHelper;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ParticleTypeSubRegistry extends SimpleRegistry<ParticleType<?>> {
    private ParticleTypeSubRegistry(String modId) {
        super(Registries.PARTICLE_TYPE, modId);
    }

    public SimpleHolder<SimpleParticleType> registerSimple(String name) {
        return this.registerSimple(name, false);
    }

    public SimpleHolder<SimpleParticleType> registerSimple(String name, boolean overrideLimiter) {
        return this.register(name, () -> SimpleLibraryPlatformHelper.INSTANCE.createSimpleParticleType(overrideLimiter));
    }
}
