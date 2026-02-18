package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AcceptableVillagerDistance(float distance) {
    public static final Codec<AcceptableVillagerDistance> DISTANCE_CODEC = Codec.FLOAT.xmap(AcceptableVillagerDistance::new, AcceptableVillagerDistance::distance);
    public static final Codec<AcceptableVillagerDistance> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("acceptable_villager_distance").forGetter(AcceptableVillagerDistance::distance)).apply(instance, AcceptableVillagerDistance::new)), DISTANCE_CODEC);
}
