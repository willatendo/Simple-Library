package willatendo.simplelibrary.server.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.world.flag.FeatureFlagSet;

public record FeatureFlagsMetadataSection(FeatureFlagSet featureFlagSet) {
	private static Codec<FeatureFlagsMetadataSection> getCodec(Codec<FeatureFlagSet> featureFlagSetCodec) {
		return RecordCodecBuilder.create(instance -> instance.group(featureFlagSetCodec.fieldOf("enabled").forGetter(FeatureFlagsMetadataSection::featureFlagSet)).apply(instance, FeatureFlagsMetadataSection::new));
	}

	public static MetadataSectionType<FeatureFlagsMetadataSection> getType(Codec<FeatureFlagSet> featureFlagSetCodec) {
		return MetadataSectionType.fromCodec("features", FeatureFlagsMetadataSection.getCodec(featureFlagSetCodec));
	}
}
