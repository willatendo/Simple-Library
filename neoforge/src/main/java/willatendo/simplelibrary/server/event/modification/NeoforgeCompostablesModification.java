package willatendo.simplelibrary.server.event.modification;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import willatendo.simplelibrary.data.DataMapEntry;

import java.util.Map;

public final class NeoforgeCompostablesModification implements CompostablesModification, DataMapEntry<Item, Compostable> {
    private final Map<ItemLike, Float> compostables = Maps.newHashMap();

    @Override
    public void add(ItemLike itemLike, float chance) {
        this.compostables.put(itemLike, chance);
    }

    @Override
    public DataMapType<Item, Compostable> getDataMapType() {
        return NeoForgeDataMaps.COMPOSTABLES;
    }

    @Override
    public void addAll(DataMapProvider.Builder<Compostable, Item> builder) {
        this.compostables.forEach((itemLike, chance) -> builder.add(itemLike.asItem().builtInRegistryHolder(), new Compostable(chance), false));
    }
}
