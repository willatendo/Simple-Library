package willatendo.simplelibrary.client.event.registry;

import net.minecraft.world.level.block.Block;

public interface ParticleColorExemptionsRegistry {
    void exempt(Block... blocks);
}
