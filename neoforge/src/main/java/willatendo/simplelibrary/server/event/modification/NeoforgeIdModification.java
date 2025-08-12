package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public final class NeoforgeIdModification implements IdModification {
    private final String modId;
    private final FMLCommonSetupEvent event;

    public NeoforgeIdModification(String modId, FMLCommonSetupEvent event) {
        this.modId = modId;
        this.event = event;
    }

    @Override
    public <T> void updateId(Registry<T> registry, ResourceLocation oldId, ResourceLocation newId) {
        this.event.enqueueWork(() -> registry.addAlias(oldId, newId));
    }

    @Override
    public ResourceLocation resource(String id) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, id);
    }
}
