package ca.willatendo.simplelibrary.server.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.List;
import java.util.Optional;

// Modified from Neoforge
public final class SimpleLibraryExtraCodecs {
    private SimpleLibraryExtraCodecs() {
    }

    public static <A> Codec<List<A>> listWithOptionalElements(Codec<Optional<A>> elementCodec) {
        return SimpleLibraryExtraCodecs.listWithoutEmpty(elementCodec.listOf());
    }

    public static <A> Codec<List<A>> listWithoutEmpty(Codec<List<Optional<A>>> codec) {
        return codec.xmap(list -> list.stream().filter(Optional::isPresent).map(Optional::get).toList(), list -> list.stream().map(Optional::of).toList());
    }

    public static <A> Codec<A> decodeOnly(Decoder<A> decoder) {
        return Codec.of(MapCodec.unitCodec(() -> {
            throw new UnsupportedOperationException("Cannot encode with decode-only codec! Decoder:" + decoder);
        }), decoder, "DecodeOnly[" + decoder + "]");
    }

    public static <T> Codec<T> withAlternative(final Codec<T> codec, final Codec<T> alternative) {
        return new AlternativeCodec<>(codec, alternative);
    }

    private record AlternativeCodec<T>(Codec<T> codec, Codec<T> alternative) implements Codec<T> {
        @Override
        public <T1> DataResult<Pair<T, T1>> decode(final DynamicOps<T1> ops, final T1 input) {
            final DataResult<Pair<T, T1>> result = codec.decode(ops, input);
            if (result.error().isEmpty()) {
                return result;
            } else {
                return alternative.decode(ops, input);
            }
        }

        @Override
        public <T1> DataResult<T1> encode(final T input, final DynamicOps<T1> ops, final T1 prefix) {
            final DataResult<T1> result = codec.encode(input, ops, prefix);
            if (result.error().isEmpty()) {
                return result;
            } else {
                return alternative.encode(input, ops, prefix);
            }
        }

        @Override
        public String toString() {
            return "Alternative[" + codec + ", " + alternative + "]";
        }
    }
}
