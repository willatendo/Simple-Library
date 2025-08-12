package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class NeoforgeParticleColorExemptionsRegistry implements ParticleColorExemptionsRegistry {
    private final RegisterClientExtensionsEvent event;

    public NeoforgeParticleColorExemptionsRegistry(RegisterClientExtensionsEvent event) {
        this.event = event;
    }

    @Override
    public void exempt(Block... blocks) {
        this.event.registerBlock(new IClientBlockExtensions() {
            @Override
            public boolean areBreakingParticlesTinted(BlockState blockState, ClientLevel clientLevel, BlockPos blockPos) {
                return false;
            }
        }, blocks);
    }
}
