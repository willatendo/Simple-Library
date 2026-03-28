package ca.willatendo.simplelibrary.data.providers.tag;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class SimpleTagBuilder {
    private final List<SimpleTagEntry> entries = new ArrayList<>();
    private boolean replace = false;

    public static SimpleTagBuilder create() {
        return new SimpleTagBuilder();
    }

    public List<SimpleTagEntry> build() {
        return List.copyOf(this.entries);
    }

    public boolean shouldReplace() {
        return this.replace;
    }

    public SimpleTagBuilder setReplace(final boolean replace) {
        this.replace = replace;
        return this;
    }

    public SimpleTagBuilder add(final SimpleTagEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public SimpleTagBuilder addElement(final Identifier id) {
        return this.add(SimpleTagEntry.element(id));
    }

    public SimpleTagBuilder addOptionalElement(final Identifier id) {
        return this.add(SimpleTagEntry.optionalElement(id));
    }

    public SimpleTagBuilder addTag(final Identifier id) {
        return this.add(SimpleTagEntry.tag(id));
    }

    public SimpleTagBuilder addOptionalTag(final Identifier id) {
        return this.add(SimpleTagEntry.optionalTag(id));
    }
}
