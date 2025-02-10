package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class FabricIdModification implements IdModification {
    private final String modId;

    public FabricIdModification(String modId) {
        this.modId = modId;
    }

    @Override
    public <T> void updateId(Registry<T> registry, ResourceLocation oldId, Supplier<T> remap) {
        registry.addAlias(oldId, this.getId(registry, remap.get()));
    }

    @Override
    public ResourceLocation resource(String id) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, id);
    }
}
