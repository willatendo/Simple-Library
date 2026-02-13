package ca.willatendo.simplelibrary;

import ca.willatendo.simplelibrary.core.utils.SimpleCoreUtils;
import ca.willatendo.simplelibrary.server.NeoforgeModInit;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SimpleCoreUtils.ID)
public final class NeoforgeSimpleLibraryMod {
    public NeoforgeSimpleLibraryMod(IEventBus iEventBus) {
        SimpleLibraryMod.modInit(new NeoforgeModInit(SimpleCoreUtils.ID, "1.0.0", iEventBus));
        MobSpawnSettings.SpawnerData.CODEC.codec().listOf();
    }
}