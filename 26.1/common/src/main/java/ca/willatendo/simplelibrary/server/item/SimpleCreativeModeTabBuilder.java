package ca.willatendo.simplelibrary.server.item;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SimpleCreativeModeTabBuilder {
    private final List<Identifier> before = new ArrayList<>();
    private final List<Identifier> after = new ArrayList<>();

    public void withAfter(Identifier... tabs) {
        this.after.addAll(Arrays.asList(tabs));
    }

    public void withBefore(Identifier... tabs) {
        this.before.addAll(Arrays.asList(tabs));
    }

    public boolean hasBefore() {
        return !this.before.isEmpty();
    }

    public boolean hasAfter() {
        return !this.after.isEmpty();
    }

    public Identifier[] getBefore() {
        return this.after.toArray(Identifier[]::new);
    }

    public Identifier[] getAfter() {
        return this.after.toArray(Identifier[]::new);
    }
}
