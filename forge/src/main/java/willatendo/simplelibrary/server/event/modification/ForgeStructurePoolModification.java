package willatendo.simplelibrary.server.event.modification;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

public final class ForgeStructurePoolModification implements StructurePoolModification {
    private final MinecraftServer minecraftServer;

    public ForgeStructurePoolModification(ServerAboutToStartEvent event) {
        this.minecraftServer = event.getServer();
    }

    @Override
    public Registry<StructureTemplatePool> getTemplatePoolRegistry() {
        return this.minecraftServer.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL);
    }

    @Override
    public Registry<StructureProcessorList> getProcessorListRegistry() {
        return this.minecraftServer.registryAccess().lookupOrThrow(Registries.PROCESSOR_LIST);
    }
}