package willatendo.simplelibrary.server.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class ParticleTypeRegistry extends SimpleRegistry<ParticleType<?>> {
    ParticleTypeRegistry(String modId) {
        super(Registries.PARTICLE_TYPE, modId);
    }

    public SimpleHolder<SimpleParticleType> registerSimple(String id, boolean overrideLimiter) {
        return this.register(id, () -> SimpleUtils.createParticleType(overrideLimiter));
    }
}
