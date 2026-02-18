package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record MonsterRoomMob(int weight) {
    public static final Codec<MonsterRoomMob> WEIGHT_CODEC = ExtraCodecs.NON_NEGATIVE_INT.xmap(MonsterRoomMob::new, MonsterRoomMob::weight);
    public static final Codec<MonsterRoomMob> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(MonsterRoomMob::weight)).apply(instance, MonsterRoomMob::new)), WEIGHT_CODEC);
}
