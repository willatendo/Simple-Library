package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.minecraft.world.level.block.Block;

public final class FabricParticleColorExemptionsRegistry implements ParticleColorExemptionsRegistry {
    @Override
    public void exempt(Block... blocks) {
        for (Block block : blocks) {
            ParticleRenderEvents.ALLOW_BLOCK_DUST_TINT.register((blockState, clientLevel, blockPos) -> !blockState.is(block));
        }
    }
}
