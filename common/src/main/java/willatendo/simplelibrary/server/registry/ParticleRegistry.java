package willatendo.simplelibrary.server.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public final class ParticleRegistry extends SimpleRegistry<ParticleType<?>> {
    ParticleRegistry(String modId) {
        super(Registries.PARTICLE_TYPE, modId);
    }

    public SimpleHolder<SimpleParticleType> registerSimple(String id, boolean overrideLimiter) {
        return this.register(id, () -> new SimpleParticleType(overrideLimiter));
    }
}
