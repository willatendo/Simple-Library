package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Compostable(float chance, boolean canVillagerCompost) {
    public static final Codec<Compostable> CHANCE_CODEC = Codec.floatRange(0.0F, 1.0F).xmap(Compostable::new, Compostable::chance);
    public static final Codec<Compostable> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(Compostable::chance), Codec.BOOL.optionalFieldOf("can_villager_compost", false).forGetter(Compostable::canVillagerCompost)).apply(instance, Compostable::new)), CHANCE_CODEC);

    public Compostable(float chance) {
        this(chance, false);
    }
}
