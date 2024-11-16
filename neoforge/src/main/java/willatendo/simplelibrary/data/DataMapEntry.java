package willatendo.simplelibrary.data;

import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public interface DataMapEntry<A, B> {
    DataMapType<A, B> getDataMapType();

    void addAll(DataMapProvider.Builder<B, A> builder);
}
