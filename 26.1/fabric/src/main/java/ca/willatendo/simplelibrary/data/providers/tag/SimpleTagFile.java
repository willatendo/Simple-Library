package ca.willatendo.simplelibrary.data.providers.tag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SimpleTagFile(List<SimpleTagEntry> entries, boolean replace) {
    public static final Codec<SimpleTagFile> CODEC = RecordCodecBuilder.create(instance -> instance.group(SimpleTagEntry.CODEC.listOf().fieldOf("values").forGetter(SimpleTagFile::entries), Codec.BOOL.optionalFieldOf("replace", false).forGetter(SimpleTagFile::replace)).apply(instance, SimpleTagFile::new));
}
