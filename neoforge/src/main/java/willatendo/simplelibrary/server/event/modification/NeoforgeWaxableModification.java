package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;
import willatendo.simplelibrary.data.DataMapEntry;

import java.util.Map;

public final class NeoforgeWaxableModification implements WaxableModification, DataMapEntry<Block, Waxable> {
    private final Map<Block, Block> waxable = Maps.newHashMap();

    @Override
    public void add(Block in, Block out) {
        this.waxable.put(in, out);
    }

    @Override
    public DataMapType<Block, Waxable> getDataMapType() {
        return NeoForgeDataMaps.WAXABLES;
    }

    @Override
    public void addAll(DataMapProvider.Builder<Waxable, Block> builder) {
        this.waxable.forEach((in, out) -> builder.add(in.builtInRegistryHolder(), new Waxable(out), false));
    }

    public Map<Block, Block> getWaxable() {
        return this.waxable;
    }
}
