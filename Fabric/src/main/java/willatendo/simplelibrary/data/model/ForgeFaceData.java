package willatendo.simplelibrary.data.model;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ExtraCodecs;

public record ForgeFaceData(int color, int blockLight, int skyLight, boolean ambientOcclusion) {

	public static final ForgeFaceData DEFAULT = new ForgeFaceData(0xFFFFFFFF, 0, 0, true);

	public static final Codec<Integer> COLOR = new ExtraCodecs.EitherCodec<>(Codec.INT, Codec.STRING).xmap(either -> either.map(Function.identity(), str -> (int) Long.parseLong(str, 16)), color -> Either.right(Integer.toHexString(color)));

	public static final Codec<ForgeFaceData> CODEC = RecordCodecBuilder.create(forgeFaceData -> forgeFaceData.group(COLOR.optionalFieldOf("color", 0xFFFFFFFF).forGetter(ForgeFaceData::color), Codec.intRange(0, 15).optionalFieldOf("block_light", 0).forGetter(ForgeFaceData::blockLight), Codec.intRange(0, 15).optionalFieldOf("sky_light", 0).forGetter(ForgeFaceData::skyLight), Codec.BOOL.optionalFieldOf("ambient_occlusion", true).forGetter(ForgeFaceData::ambientOcclusion)).apply(forgeFaceData, ForgeFaceData::new));

	@Nullable
	public static ForgeFaceData read(@Nullable JsonElement jsonElement, @Nullable ForgeFaceData forgeFaceData) throws JsonParseException {
		if (jsonElement == null) {
			return forgeFaceData;
		}
		return CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(false, JsonParseException::new);
	}
}
