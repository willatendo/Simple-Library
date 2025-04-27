package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

public final class ForgeParticleRegistry implements ParticleRegistry {
    private final RegisterParticleProvidersEvent event;

    public ForgeParticleRegistry(RegisterParticleProvidersEvent event) {
        this.event = event;
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
        this.event.registerSpecial(particleType, particleFactory);
    }

    @Override
    public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
        this.event.registerSprite(particleType, sprite);
    }

    @Override
    public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleMetaFactory) {
        this.event.registerSpriteSet(particleType, particleMetaFactory);
    }
}
