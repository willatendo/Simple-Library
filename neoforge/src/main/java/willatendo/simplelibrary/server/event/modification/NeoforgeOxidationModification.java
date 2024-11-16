package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import willatendo.simplelibrary.data.DataMapEntry;

import java.util.Map;

public final class NeoforgeOxidationModification implements OxidationModification, DataMapEntry<Block, Oxidizable> {
    private final Map<Block, Block> oxidation = Maps.newHashMap();

    @Override
    public void add(Block in, Block out) {
        this.oxidation.put(in, out);
    }

    @Override
    public DataMapType<Block, Oxidizable> getDataMapType() {
        return NeoForgeDataMaps.OXIDIZABLES;
    }

    @Override
    public void addAll(DataMapProvider.Builder<Oxidizable, Block> builder) {
        this.oxidation.forEach((in, out) -> builder.add(in.builtInRegistryHolder(), new Oxidizable(out), false));
    }

    public Map<Block, Block> getOxidation() {
        return this.oxidation;
    }
}
