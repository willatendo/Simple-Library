package ca.willatendo.simplelibrary.server.toposort;

import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TopologicalSort {
    public static <T> List<T> topologicalSort(Graph<T> graph, @Nullable Comparator<? super T> comparator) throws IllegalArgumentException {
        Preconditions.checkArgument(graph.isDirected(), "Cannot topologically sort an undirected graph!");
        Preconditions.checkArgument(!graph.allowsSelfLoops(), "Cannot topologically sort a graph with self loops!");

        Queue<T> queue = comparator == null ? new ArrayDeque<>() : new PriorityQueue<>(comparator);
        Map<T, Integer> degrees = new HashMap<>();
        List<T> results = new ArrayList<>();

        for (T node : graph.nodes()) {
            int degree = graph.inDegree(node);
            if (degree == 0) {
                queue.add(node);
            } else {
                degrees.put(node, degree);
            }
        }

        while (!queue.isEmpty()) {
            T current = queue.remove();
            results.add(current);
            for (T successor : graph.successors(current)) {
                int updated = degrees.compute(successor, (node, degree) -> Objects.requireNonNull(degree, () -> "Invalid degree present for " + node) - 1);
                if (updated == 0) {
                    queue.add(successor);
                    degrees.remove(successor);
                }
            }
        }

        if (!degrees.isEmpty()) {
            Set<Set<T>> components = new StronglyConnectedComponentDetector<>(graph).getComponents();
            components.removeIf(set -> set.size() < 2);
            throwCyclePresentException(components);
        }

        return results;
    }

    private static <T> void throwCyclePresentException(Set<Set<T>> components) {
        throw new CyclePresentException((Set<Set<?>>) (Set<?>) components);
    }
}