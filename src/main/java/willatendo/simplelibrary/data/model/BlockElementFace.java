package willatendo.simplelibrary.data.model;

import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;

public class BlockElementFace {
	public static final int NO_TINT = -1;
	public final Direction cullForDirection;
	public final int tintIndex;
	public final String texture;
	public final BlockFaceUV blockFaceUV;
	@Nullable
	private final ForgeFaceData forgeFaceData;
	protected BlockElement parent;

	public BlockElementFace(@Nullable Direction direction, int tintIndex, String texture, BlockFaceUV blockFaceUV, ForgeFaceData faceData) {
		this.cullForDirection = direction;
		this.tintIndex = tintIndex;
		this.texture = texture;
		this.blockFaceUV = blockFaceUV;
		this.forgeFaceData = faceData;
	}

	public ForgeFaceData getFaceData() {
		if (this.forgeFaceData != null) {
			return this.forgeFaceData;
		} else if (this.parent != null) {
			return this.parent.getFaceData();
		}
		return ForgeFaceData.DEFAULT;
	}

	public static class Deserializer implements JsonDeserializer<BlockElementFace> {
		@Override
		public BlockElementFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Direction direction = this.getCullFacing(jsonObject);
			int tintIndex = this.getTintIndex(jsonObject);
			String texture = this.getTexture(jsonObject);
			BlockFaceUV blockfaceuv = jsonDeserializationContext.deserialize(jsonObject, BlockFaceUV.class);
			var faceData = ForgeFaceData.read(jsonObject.get("forge_data"), null);
			return new BlockElementFace(direction, tintIndex, texture, blockfaceuv, faceData);
		}

		protected int getTintIndex(JsonObject jsonObject) {
			return GsonHelper.getAsInt(jsonObject, "tintindex", -1);
		}

		private String getTexture(JsonObject jsonObject) {
			return GsonHelper.getAsString(jsonObject, "texture");
		}

		@Nullable
		private Direction getCullFacing(JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "cullface", "");
			return Direction.byName(s);
		}
	}
}
