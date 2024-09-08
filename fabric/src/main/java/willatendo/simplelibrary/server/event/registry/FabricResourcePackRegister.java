package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.util.Optional;

public final class FabricResourcePackRegister implements ResourcePackRegister {
    @Override
    public void register(String modId, String resourcePackName) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        ResourceManagerHelper.registerBuiltinResourcePack(SimpleUtils.resource(modId, resourcePackName), modContainer.get(), SimpleUtils.translation(modId, "resourcePack", resourcePackName + ".name"), ResourcePackActivationType.NORMAL);
    }
}
