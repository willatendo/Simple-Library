package willatendo.simplelibrary.data.model;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;

public class BlockElement {
	public final Vector3f from;
	public final Vector3f to;
	public final Map<Direction, BlockElementFace> faces;
	public final BlockElementRotation rotation;
	public final boolean shade;
	private ForgeFaceData forgeFaceData;

	public BlockElement(Vector3f from, Vector3f to, Map<Direction, BlockElementFace> faces, BlockElementRotation blockElementRotation, boolean shade) {
		this(from, to, faces, blockElementRotation, shade, ForgeFaceData.DEFAULT);
	}

	public BlockElement(Vector3f from, Vector3f to, Map<Direction, BlockElementFace> faces, BlockElementRotation blockElementRotation, boolean shade, ForgeFaceData forgeFaceData) {
		this.from = from;
		this.to = to;
		this.faces = faces;
		this.rotation = blockElementRotation;
		this.shade = shade;
		this.fillUvs();
		this.setForgeFaceData(forgeFaceData);
		this.faces.values().forEach(face -> face.parent = this);
	}

	private void fillUvs() {
		for (Map.Entry<Direction, BlockElementFace> entry : this.faces.entrySet()) {
			float[] afloat = this.uvsByFace(entry.getKey());
			(entry.getValue()).blockFaceUV.setMissingUv(afloat);
		}

	}

	public float[] uvsByFace(Direction direction) {
		switch (direction) {
		case DOWN:
			return new float[] { this.from.x(), 16.0F - this.to.z(), this.to.x(), 16.0F - this.from.z() };
		case UP:
			return new float[] { this.from.x(), this.from.z(), this.to.x(), this.to.z() };
		case NORTH:
		default:
			return new float[] { 16.0F - this.to.x(), 16.0F - this.to.y(), 16.0F - this.from.x(), 16.0F - this.from.y() };
		case SOUTH:
			return new float[] { this.from.x(), 16.0F - this.to.y(), this.to.x(), 16.0F - this.from.y() };
		case WEST:
			return new float[] { this.from.z(), 16.0F - this.to.y(), this.to.z(), 16.0F - this.from.y() };
		case EAST:
			return new float[] { 16.0F - this.to.z(), 16.0F - this.to.y(), 16.0F - this.from.z(), 16.0F - this.from.y() };
		}
	}

	public static class Deserializer implements JsonDeserializer<BlockElement> {
		@Override
		public BlockElement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Vector3f vector3f = this.getFrom(jsonObject);
			Vector3f vector3f1 = this.getTo(jsonObject);
			BlockElementRotation blockElementRotation = this.getRotation(jsonObject);
			Map<Direction, BlockElementFace> map = this.getFaces(jsonDeserializationContext, jsonObject);
			if (jsonObject.has("shade") && !GsonHelper.isBooleanValue(jsonObject, "shade")) {
				throw new JsonParseException("Expected shade to be a Boolean");
			} else {
				boolean flag = GsonHelper.getAsBoolean(jsonObject, "shade", true);
				var faceData = ForgeFaceData.read(jsonObject.get("forge_data"), ForgeFaceData.DEFAULT);
				return new BlockElement(vector3f, vector3f1, map, blockElementRotation, flag, faceData);
			}
		}

		@Nullable
		private BlockElementRotation getRotation(JsonObject jsonObject) {
			BlockElementRotation blockElementRotation = null;
			if (jsonObject.has("rotation")) {
				JsonObject rotation = GsonHelper.getAsJsonObject(jsonObject, "rotation");
				Vector3f vector3f = this.getVector3f(rotation, "origin");
				vector3f.mul(0.0625F);
				Direction.Axis direction$axis = this.getAxis(rotation);
				float f = this.getAngle(rotation);
				boolean flag = GsonHelper.getAsBoolean(rotation, "rescale", false);
				blockElementRotation = new BlockElementRotation(vector3f, direction$axis, f, flag);
			}

			return blockElementRotation;
		}

		private float getAngle(JsonObject jsonObject) {
			float angle = GsonHelper.getAsFloat(jsonObject, "angle");
			if (angle != 0.0F && Mth.abs(angle) != 22.5F && Mth.abs(angle) != 45.0F) {
				throw new JsonParseException("Invalid rotation " + angle + " found, only -45/-22.5/0/22.5/45 allowed");
			} else {
				return angle;
			}
		}

		private Direction.Axis getAxis(JsonObject jsonObject) {
			String axis = GsonHelper.getAsString(jsonObject, "axis");
			Direction.Axis axisFromString = Direction.Axis.byName(axis.toLowerCase(Locale.ROOT));
			if (axisFromString == null) {
				throw new JsonParseException("Invalid rotation axis: " + axis);
			} else {
				return axisFromString;
			}
		}

		private Map<Direction, BlockElementFace> getFaces(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
			Map<Direction, BlockElementFace> map = this.filterNullFromFaces(jsonDeserializationContext, jsonObject);
			if (map.isEmpty()) {
				throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
			} else {
				return map;
			}
		}

		private Map<Direction, BlockElementFace> filterNullFromFaces(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
			Map<Direction, BlockElementFace> map = Maps.newEnumMap(Direction.class);
			JsonObject faces = GsonHelper.getAsJsonObject(jsonObject, "faces");

			for (Map.Entry<String, JsonElement> entry : faces.entrySet()) {
				Direction direction = this.getFacing(entry.getKey());
				map.put(direction, jsonDeserializationContext.deserialize(entry.getValue(), BlockElementFace.class));
			}

			return map;
		}

		private Direction getFacing(String facing) {
			Direction direction = Direction.byName(facing);
			if (direction == null) {
				throw new JsonParseException("Unknown facing: " + facing);
			} else {
				return direction;
			}
		}

		private Vector3f getTo(JsonObject jsonObject) {
			Vector3f vector3f = this.getVector3f(jsonObject, "to");
			if (!(vector3f.x() < -16.0F) && !(vector3f.y() < -16.0F) && !(vector3f.z() < -16.0F) && !(vector3f.x() > 32.0F) && !(vector3f.y() > 32.0F) && !(vector3f.z() > 32.0F)) {
				return vector3f;
			} else {
				throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
			}
		}

		private Vector3f getFrom(JsonObject jsonObject) {
			Vector3f vector3f = this.getVector3f(jsonObject, "from");
			if (!(vector3f.x() < -16.0F) && !(vector3f.y() < -16.0F) && !(vector3f.z() < -16.0F) && !(vector3f.x() > 32.0F) && !(vector3f.y() > 32.0F) && !(vector3f.z() > 32.0F)) {
				return vector3f;
			} else {
				throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
			}
		}

		private Vector3f getVector3f(JsonObject jsonObject, String vec) {
			JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, vec);
			if (jsonArray.size() != 3) {
				throw new JsonParseException("Expected 3 " + vec + " values, found: " + jsonArray.size());
			} else {
				float[] afloat = new float[3];

				for (int i = 0; i < afloat.length; ++i) {
					afloat[i] = GsonHelper.convertToFloat(jsonArray.get(i), vec + "[" + i + "]");
				}

				return new Vector3f(afloat[0], afloat[1], afloat[2]);
			}
		}
	}

	public ForgeFaceData getFaceData() {
		return this.forgeFaceData;
	}

	public void setForgeFaceData(ForgeFaceData forgeFaceData) {
		this.forgeFaceData = Objects.requireNonNull(forgeFaceData);
	}
}
