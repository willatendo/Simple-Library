package willatendo.simplelibrary.client.event;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ParticleEntry<T extends ParticleOptions>(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleSheetProvider) {
}
