package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public interface StructurePoolModification {
    ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty"));

    Registry<StructureTemplatePool> getTemplatePoolRegistry();

    Registry<StructureProcessorList> getProcessorListRegistry();

    default ResourceLocation getPlainsPoolLocation() {
        return ResourceLocation.withDefaultNamespace("village/plains/houses");
    }

    default ResourceLocation getDesertPoolLocation() {
        return ResourceLocation.withDefaultNamespace("village/desert/houses");
    }

    default ResourceLocation getSavannaPoolLocation() {
        return ResourceLocation.withDefaultNamespace("village/savanna/houses");
    }

    default ResourceLocation getSnowyPoolLocation() {
        return ResourceLocation.withDefaultNamespace("village/snowy/houses");
    }

    default ResourceLocation getTaigaPoolLocation() {
        return ResourceLocation.withDefaultNamespace("village/taiga/houses");
    }

    default void add(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        Holder.Reference<StructureProcessorList> empty = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        StructureTemplatePool structureTemplatePool = templatePoolRegistry.get(poolRL);
        if (structureTemplatePool == null) {
            return;
        }

        SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, empty).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            structureTemplatePool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = Lists.newArrayList(structureTemplatePool.rawTemplates.iterator());
        listOfPieceEntries.add(new Pair<>(piece, weight));
        structureTemplatePool.rawTemplates = listOfPieceEntries;
    }
}
