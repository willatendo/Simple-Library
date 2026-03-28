package ca.willatendo.simplelibrary.data.providers.tag;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagAppender;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public interface SimpleTagAppender<E, T> extends FabricTagAppender<E, T> {
    SimpleTagAppender<E, T> add(E element);

    default SimpleTagAppender<E, T> add(final E... elements) {
        return this.addAll(Arrays.stream(elements));
    }

    default SimpleTagAppender<E, T> addAll(final Collection<E> elements) {
        elements.forEach(this::add);
        return this;
    }

    default SimpleTagAppender<E, T> addAll(final Stream<E> elements) {
        elements.forEach(this::add);
        return this;
    }

    SimpleTagAppender<E, T> addOptional(E element);

    SimpleTagAppender<E, T> addTag(TagKey<T> tag);

    SimpleTagAppender<E, T> addOptionalTag(TagKey<T> tag);

    static <T> SimpleTagAppender<ResourceKey<T>, T> forBuilder(final SimpleTagBuilder simpleTagBuilder) {
        return new SimpleTagAppender<>() {
            @Override
            public SimpleTagAppender<ResourceKey<T>, T> add(final ResourceKey<T> element) {
                simpleTagBuilder.addElement(element.identifier());
                return this;
            }

            @Override
            public SimpleTagAppender<ResourceKey<T>, T> addOptional(final ResourceKey<T> element) {
                simpleTagBuilder.addOptionalElement(element.identifier());
                return this;
            }

            @Override
            public SimpleTagAppender<ResourceKey<T>, T> addTag(final TagKey<T> tag) {
                simpleTagBuilder.addTag(tag.location());
                return this;
            }

            @Override
            public SimpleTagAppender<ResourceKey<T>, T> addOptionalTag(final TagKey<T> tag) {
                simpleTagBuilder.addOptionalTag(tag.location());
                return this;
            }
        };
    }

    default <U> SimpleTagAppender<U, T> map(final Function<U, E> converter) {
        return new SimpleTagAppender<>() {
            {
                Objects.requireNonNull(SimpleTagAppender.this);
            }

            @Override
            public SimpleTagAppender<U, T> add(final U element) {
                SimpleTagAppender.this.add(converter.apply(element));
                return this;
            }

            @Override
            public SimpleTagAppender<U, T> addOptional(final U element) {
                SimpleTagAppender.this.add(converter.apply(element));
                return this;
            }

            @Override
            public SimpleTagAppender<U, T> addTag(final TagKey<T> tag) {
                SimpleTagAppender.this.addTag(tag);
                return this;
            }

            @Override
            public SimpleTagAppender<U, T> addOptionalTag(final TagKey<T> tag) {
                SimpleTagAppender.this.addOptionalTag(tag);
                return this;
            }
        };
    }
}
