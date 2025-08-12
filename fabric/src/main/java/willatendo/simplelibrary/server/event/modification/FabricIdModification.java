package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class FabricIdModification implements IdModification {
    private final String modId;

    public FabricIdModification(String modId) {
        this.modId = modId;
    }

    @Override
    public <T> void updateId(Registry<T> registry, ResourceLocation oldId, ResourceLocation newId) {
        registry.addAlias(oldId, newId);
    }

    @Override
    public ResourceLocation resource(String id) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, id);
    }
}
