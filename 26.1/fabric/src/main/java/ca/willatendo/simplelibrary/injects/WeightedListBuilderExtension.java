package ca.willatendo.simplelibrary.injects;

import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;

import java.util.Collection;
import java.util.function.Predicate;

public interface WeightedListBuilderExtension<E> {
    default WeightedList.Builder<E> addAll(WeightedList<E> values) {
        return this.addAll(values.unwrap());
    }

    default WeightedList.Builder<E> addAll(Collection<Weighted<E>> values) {
        return (WeightedList.Builder<E>) this;
    }

    default WeightedList.Builder<E> removeIf(Predicate<Weighted<E>> filter) {
        return (WeightedList.Builder<E>) this;
    }
}
