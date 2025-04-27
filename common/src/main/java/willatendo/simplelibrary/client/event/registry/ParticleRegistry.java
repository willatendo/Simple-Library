package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public interface ParticleRegistry {
    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ParticleProvider<T> particleFactory);

    <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite);

    <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleMetaFactory);
}
