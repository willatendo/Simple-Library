package willatendo.simplelibrary.server.event.registry;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.nio.file.Path;
import java.util.Optional;

public final class NeoforgeResourcePackRegister implements ResourcePackRegister {
    private final AddPackFindersEvent event;

    public NeoforgeResourcePackRegister(AddPackFindersEvent event) {
        this.event = event;
    }

    @Override
    public void register(String modId, String resourcePackName) {
        if (this.event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(modId).getFile().findResource("resourcepacks/" + resourcePackName);
            this.event.addRepositorySource(consumer -> {
                Pack pack = Pack.readMetaAndCreate(new PackLocationInfo(SimpleUtils.resource(modId, "resourcepacks." + resourcePackName).toString(), SimpleUtils.translation(modId, "resourcePack", resourcePackName + ".name"), PackSource.BUILT_IN, Optional.empty()), new PathPackResources.PathResourcesSupplier(resourcePath), PackType.CLIENT_RESOURCES, new PackSelectionConfig(false, Pack.Position.TOP, false));
                if (pack != null) {
                    consumer.accept(pack);
                }
            });
        }
    }
}
