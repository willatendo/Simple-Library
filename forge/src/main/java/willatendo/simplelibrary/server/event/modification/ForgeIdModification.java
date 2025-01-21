package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.function.Supplier;

public final class ForgeIdModification implements IdModification {
    private final String modId;
    private final MissingMappingsEvent event;

    public ForgeIdModification(String modId, MissingMappingsEvent event) {
        this.modId = modId;
        this.event = event;
    }

    @Override
    public <T> void updateId(Registry<T> registry, ResourceLocation oldId, Supplier<T> remap) {
        this.event.getAllMappings(registry.key()).forEach(blockMapping -> {
            if (blockMapping.getKey() == oldId) {
                blockMapping.remap(remap.get());
            }
        });
    }

    @Override
    public ResourceLocation resource(String id) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, id);
    }
}
