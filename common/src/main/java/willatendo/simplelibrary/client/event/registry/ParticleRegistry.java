package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public interface ParticleRegistry {
    <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> particleType, ParticleProvider<T> particleFactory);
}
