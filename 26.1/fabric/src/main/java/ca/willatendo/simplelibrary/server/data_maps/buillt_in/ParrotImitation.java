package ca.willatendo.simplelibrary.server.data_maps.buillt_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public record ParrotImitation(SoundEvent sound) {
    public static final Codec<ParrotImitation> SOUND_CODEC = BuiltInRegistries.SOUND_EVENT.byNameCodec().xmap(ParrotImitation::new, ParrotImitation::sound);
    public static final Codec<ParrotImitation> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound").forGetter(ParrotImitation::sound)).apply(instance, ParrotImitation::new)), SOUND_CODEC);
}
