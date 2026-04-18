package ca.willatendo.simplelibrary.server.toposort;

import java.util.Set;

public final class CyclePresentException extends IllegalArgumentException {
    private final Set<Set<?>> cycles;

    CyclePresentException(Set<Set<?>> cycles) {
        super("Cycles present in graph: " + cycles);
        this.cycles = cycles;
    }

    public <T> Set<Set<T>> getCycles() {
        return (Set<Set<T>>) (Set<?>) cycles;
    }
}
