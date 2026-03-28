package ca.willatendo.simplelibrary.data.providers.tag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleTagEntry {
    private static final Codec<SimpleTagEntry> FULL_CODEC = RecordCodecBuilder.create((i) -> i.group(ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("id").forGetter(SimpleTagEntry::elementOrTag), Codec.BOOL.optionalFieldOf("required", true).forGetter((e) -> e.required)).apply(i, SimpleTagEntry::new));
    public static final Codec<SimpleTagEntry> CODEC;
    private final Identifier id;
    private final boolean tag;
    private final boolean required;

    private SimpleTagEntry(Identifier id, boolean tag, boolean required) {
        this.id = id;
        this.tag = tag;
        this.required = required;
    }

    private SimpleTagEntry(ExtraCodecs.TagOrElementLocation elementOrTag, boolean required) {
        this.id = elementOrTag.id();
        this.tag = elementOrTag.tag();
        this.required = required;
    }

    private ExtraCodecs.TagOrElementLocation elementOrTag() {
        return new ExtraCodecs.TagOrElementLocation(this.id, this.tag);
    }

    SimpleTagEntry withRequired(boolean required) {
        return new SimpleTagEntry(this.id, this.tag, required);
    }

    public static SimpleTagEntry element(Identifier id) {
        return new SimpleTagEntry(id, false, true);
    }

    public static SimpleTagEntry optionalElement(Identifier id) {
        return new SimpleTagEntry(id, false, false);
    }

    public static SimpleTagEntry tag(Identifier id) {
        return new SimpleTagEntry(id, true, true);
    }

    public static SimpleTagEntry optionalTag(Identifier id) {
        return new SimpleTagEntry(id, true, false);
    }

    public <T> boolean build(SimpleTagEntry.Lookup<T> lookup, Consumer<T> output) {
        if (this.tag) {
            Collection<T> result = lookup.tag(this.id);
            if (result == null) {
                return !this.required;
            }

            result.forEach(output);
        } else {
            T result = lookup.element(this.id, this.required);
            if (result == null) {
                return !this.required;
            }

            output.accept(result);
        }

        return true;
    }

    public void visitRequiredDependencies(Consumer<Identifier> output) {
        if (this.tag && this.required) {
            output.accept(this.id);
        }

    }

    public void visitOptionalDependencies(Consumer<Identifier> output) {
        if (this.tag && !this.required) {
            output.accept(this.id);
        }

    }

    public boolean verifyIfPresent(Predicate<Identifier> elementCheck, Predicate<Identifier> tagCheck) {
        return !this.required || (this.tag ? tagCheck : elementCheck).test(this.id);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.tag) {
            result.append('#');
        }

        result.append(this.id);
        if (!this.required) {
            result.append('?');
        }

        return result.toString();
    }

    public Identifier getId() {
        return this.id;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isTag() {
        return this.tag;
    }

    static {
        CODEC = Codec.either(ExtraCodecs.TAG_OR_ELEMENT_ID, FULL_CODEC).xmap((e) -> (SimpleTagEntry) e.map((l) -> new SimpleTagEntry(l, true), (r) -> r), (entry) -> entry.required ? Either.left(entry.elementOrTag()) : Either.right(entry));
    }

    public interface Lookup<T> {
        T element(Identifier var1, boolean var2);

        Collection<T> tag(Identifier var1);
    }
}
