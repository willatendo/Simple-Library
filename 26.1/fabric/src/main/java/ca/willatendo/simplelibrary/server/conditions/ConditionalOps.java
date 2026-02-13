package ca.willatendo.simplelibrary.server.conditions;

import ca.willatendo.simplelibrary.server.utils.SimpleLibraryExtraCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Optional;

// Modified from Neoforge
public class ConditionalOps<T> extends RegistryOps<T> {
    private final ICondition.IContext context;

    public ConditionalOps(RegistryOps<T> ops, ICondition.IContext context) {
        super(ops, ops.lookupProvider);
        this.context = context;
    }

    public static MapCodec<ICondition.IContext> retrieveContext() {
        return ExtraCodecs.retrieveContext(ops -> {
            if (!(ops instanceof ConditionalOps<?> conditionalOps)) {
                return DataResult.success(ICondition.IContext.EMPTY);
            }

            return DataResult.success(conditionalOps.context);
        });
    }

    public static final String DEFAULT_CONDITIONS_KEY = "neoforge:conditions";
    public static final String CONDITIONAL_VALUE_KEY = "neoforge:value";

    public static <T> Codec<Optional<T>> createConditionalCodec(final Codec<T> ownerCodec) {
        return createConditionalCodec(ownerCodec, DEFAULT_CONDITIONS_KEY);
    }

    public static <T> Codec<Optional<T>> createConditionalCodec(final Codec<T> ownerCodec, String conditionalsKey) {
        return createConditionalCodecWithConditions(ownerCodec, conditionalsKey).xmap(r -> r.map(WithConditions::carrier), r -> r.map(i -> new WithConditions<>(List.of(), i)));
    }

    public static <T> Codec<List<T>> decodeListWithElementConditions(final Codec<T> ownerCodec) {
        return Codec.of(ownerCodec.listOf(), SimpleLibraryExtraCodecs.listWithOptionalElements(createConditionalCodec(ownerCodec)));
    }

    public static <T> Codec<Optional<WithConditions<T>>> createConditionalCodecWithConditions(final Codec<T> ownerCodec) {
        return createConditionalCodecWithConditions(ownerCodec, DEFAULT_CONDITIONS_KEY);
    }

    public static <T> Codec<Optional<WithConditions<T>>> createConditionalCodecWithConditions(final Codec<T> ownerCodec, String conditionalsKey) {
        return Codec.of(new ConditionalEncoder<>(conditionalsKey, ICondition.LIST_CODEC, ownerCodec), new ConditionalDecoder<>(conditionalsKey, ICondition.LIST_CODEC, retrieveContext().codec(), ownerCodec));
    }

    private static final class ConditionalEncoder<A> implements Encoder<Optional<WithConditions<A>>> {
        private final String conditionalsPropertyKey;
        public final Codec<List<ICondition>> conditionsCodec;
        private final Encoder<A> innerCodec;

        private ConditionalEncoder(String conditionalsPropertyKey, Codec<List<ICondition>> conditionsCodec, Encoder<A> innerCodec) {
            this.conditionalsPropertyKey = conditionalsPropertyKey;
            this.conditionsCodec = conditionsCodec;
            this.innerCodec = innerCodec;
        }

        @Override
        public <T> DataResult<T> encode(Optional<WithConditions<A>> input, DynamicOps<T> ops, T prefix) {
            if (ops.compressMaps()) {
                return DataResult.error(() -> "Cannot use ConditionalCodec with compressing DynamicOps");
            }

            if (input.isEmpty()) {
                return DataResult.error(() -> "Cannot encode empty Optional with a ConditionalEncoder. We don't know what to encode to!");
            }

            WithConditions<A> withConditions = input.get();

            if (withConditions.conditions().isEmpty()) {
                return this.innerCodec.encode(withConditions.carrier(), ops, prefix);
            }

            RecordBuilder<T> recordBuilder = ops.mapBuilder();
            recordBuilder.add(this.conditionalsPropertyKey, this.conditionsCodec.encodeStart(ops, withConditions.conditions()));

            DataResult<T> encodedInner = this.innerCodec.encodeStart(ops, withConditions.carrier());

            return encodedInner.flatMap(inner -> ops.getMap(inner).map(innerMap -> {
                if (innerMap.get(conditionalsPropertyKey) != null || innerMap.get(CONDITIONAL_VALUE_KEY) != null) {
                    return DataResult.<T>error(() -> "Cannot wrap a value that already uses the condition or value key with a ConditionalCodec.");
                }
                innerMap.entries().forEach(pair -> recordBuilder.add(pair.getFirst(), pair.getSecond()));
                return recordBuilder.build(prefix);
            }).result().orElseGet(() -> {
                recordBuilder.add(CONDITIONAL_VALUE_KEY, inner);
                return recordBuilder.build(prefix);
            }));
        }

        @Override
        public String toString() {
            return "Conditional[" + innerCodec + "]";
        }
    }

    private static final class ConditionalDecoder<A> implements Decoder<Optional<WithConditions<A>>> {
        private final String conditionalsPropertyKey;
        public final Codec<List<ICondition>> conditionsCodec;
        private final Codec<ICondition.IContext> contextCodec;
        private final Decoder<A> innerCodec;

        private ConditionalDecoder(String conditionalsPropertyKey, Codec<List<ICondition>> conditionsCodec, Codec<ICondition.IContext> contextCodec, Decoder<A> innerCodec) {
            this.conditionalsPropertyKey = conditionalsPropertyKey;
            this.conditionsCodec = conditionsCodec;
            this.contextCodec = contextCodec;
            this.innerCodec = innerCodec;
        }

        @Override
        public <T> DataResult<Pair<Optional<WithConditions<A>>, T>> decode(DynamicOps<T> ops, T input) {
            if (ops.compressMaps()) {
                return DataResult.error(() -> "Cannot use ConditionalCodec with compressing DynamicOps");
            }

            return ops.getMap(input).map(inputMap -> {
                final T conditionsDataCarrier = inputMap.get(conditionalsPropertyKey);
                if (conditionsDataCarrier == null) {
                    return innerCodec.decode(ops, input).map(result -> result.mapFirst(carrier -> Optional.of(new WithConditions<>(carrier))));
                }

                return conditionsCodec.decode(ops, conditionsDataCarrier).flatMap(conditionsCarrier -> {
                    final List<ICondition> conditions = conditionsCarrier.getFirst();
                    final DataResult<Pair<ICondition.IContext, T>> contextDataResult = contextCodec.decode(ops, ops.emptyMap());

                    return contextDataResult.flatMap(contextCarrier -> {
                        final ICondition.IContext context = contextCarrier.getFirst();

                        final boolean conditionsMatch = conditions.stream().allMatch(c -> c.test(context));
                        if (!conditionsMatch) {
                            return DataResult.success(Pair.of(Optional.empty(), input));
                        }

                        DataResult<Pair<A, T>> innerDecodeResult;

                        T valueDataCarrier = inputMap.get(CONDITIONAL_VALUE_KEY);
                        if (valueDataCarrier != null) {
                            innerDecodeResult = innerCodec.decode(ops, valueDataCarrier);
                        } else {
                            T conditionalsKey = ops.createString(conditionalsPropertyKey);
                            var mapForDecoding = ops.createMap(inputMap.entries().filter(pair -> !pair.getFirst().equals(conditionalsKey)));
                            innerDecodeResult = innerCodec.decode(ops, mapForDecoding);
                        }

                        return innerDecodeResult.<Pair<Optional<WithConditions<A>>, T>>map(result -> result.mapFirst(carrier -> Optional.of(new WithConditions<>(conditions, carrier))));
                    });
                });
            }).result().orElseGet(() -> innerCodec.decode(ops, input).map(result -> result.mapFirst(carrier -> Optional.of(new WithConditions<>(carrier)))));
        }
    }
}