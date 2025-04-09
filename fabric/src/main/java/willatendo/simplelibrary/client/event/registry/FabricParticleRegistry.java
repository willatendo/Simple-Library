package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public final class FabricParticleRegistry implements ParticleRegistry {
    @Override
    public <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> particleType, ParticleProvider<T> particleFactory) {
        ParticleFactoryRegistry.getInstance().register(particleType, particleFactory);
    }
}
