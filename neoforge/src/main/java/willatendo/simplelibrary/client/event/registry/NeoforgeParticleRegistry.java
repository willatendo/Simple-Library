package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public final class NeoforgeParticleRegistry implements ParticleRegistry {
    private final RegisterParticleProvidersEvent event;

    public NeoforgeParticleRegistry(RegisterParticleProvidersEvent event) {
        this.event = event;
    }

    @Override
    public <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
        this.event.registerSpecial(particleType, particleFactory);
    }
}
