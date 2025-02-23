package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class FabricClientReloadListenerRegister implements ClientReloadListenerRegister {
    private final ResourceManagerHelper resourceManagerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);

    @Override
    public void register(ResourceLocation id, PreparableReloadListener preparableReloadListener) {
        this.resourceManagerHelper.registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor executor, Executor executor2) {
                return preparableReloadListener.reload(preparationBarrier, resourceManager, executor, executor2);
            }
        });
    }
}
