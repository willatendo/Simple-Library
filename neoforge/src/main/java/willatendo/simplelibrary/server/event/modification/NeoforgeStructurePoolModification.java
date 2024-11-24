package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

public final class NeoforgeStructurePoolModification implements StructurePoolModification {
    private final MinecraftServer minecraftServer;

    public NeoforgeStructurePoolModification(ServerAboutToStartEvent event) {
        this.minecraftServer = event.getServer();
    }

    @Override
    public Registry<StructureTemplatePool> getTemplatePoolRegistry() {
        return this.minecraftServer.registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();
    }

    @Override
    public Registry<StructureProcessorList> getProcessorListRegistry() {
        return this.minecraftServer.registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
    }
}
