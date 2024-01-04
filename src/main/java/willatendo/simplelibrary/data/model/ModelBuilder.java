package willatendo.simplelibrary.data.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import com.mojang.serialization.JsonOps;

import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import willatendo.simplelibrary.data.ModelProvider;
import willatendo.simplelibrary.data.util.ExistingFileHelper;
import willatendo.simplelibrary.data.util.TransformationHelper;

public class ModelBuilder<T extends ModelBuilder<T>> extends ModelFile {
	protected ModelFile parent;
	protected final Map<String, String> textures = new LinkedHashMap<>();
	protected final TransformsBuilder transformsBuilder = new TransformsBuilder();
	protected final ExistingFileHelper existingFileHelper;

	protected String renderType = null;
	protected boolean ambientOcclusion = true;
	protected GuiLight guiLight = null;

	protected final List<ElementBuilder> elementsBuilders = new ArrayList<>();

	private final RootTransformsBuilder rootTransforms = new RootTransformsBuilder();

	protected ModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation);
		this.existingFileHelper = existingFileHelper;
	}

	private T self() {
		return (T) this;
	}

	@Override
	protected boolean exists() {
		return true;
	}

	public T parent(ModelFile parent) {
		Preconditions.checkNotNull(parent, "Parent must not be null");
		parent.assertExistence();
		this.parent = parent;
		return self();
	}

	public T texture(String key, String texture) {
		Preconditions.checkNotNull(key, "Key must not be null");
		Preconditions.checkNotNull(texture, "Texture must not be null");
		if (texture.charAt(0) == '#') {
			this.textures.put(key, texture);
			return self();
		} else {
			ResourceLocation asLoc;
			if (texture.contains(":")) {
				asLoc = new ResourceLocation(texture);
			} else {
				asLoc = new ResourceLocation(this.getResourceLocation().getNamespace(), texture);
			}
			return this.texture(key, asLoc);
		}
	}

	public T texture(String key, ResourceLocation texture) {
		Preconditions.checkNotNull(key, "Key must not be null");
		Preconditions.checkNotNull(texture, "Texture must not be null");
		Preconditions.checkArgument(existingFileHelper.exists(texture, ModelProvider.TEXTURE), "Texture %s does not exist in any known resource pack", texture);
		this.textures.put(key, texture.toString());
		return self();
	}

	public T renderType(String renderType) {
		Preconditions.checkNotNull(renderType, "Render type must not be null");
		return renderType(new ResourceLocation(renderType));
	}

	public T renderType(ResourceLocation renderType) {
		Preconditions.checkNotNull(renderType, "Render type must not be null");
		this.renderType = renderType.toString();
		return self();
	}

	public TransformsBuilder transforms() {
		return this.transformsBuilder;
	}

	public T ao(boolean ao) {
		this.ambientOcclusion = ao;
		return self();
	}

	public T guiLight(GuiLight light) {
		this.guiLight = light;
		return self();
	}

	public ElementBuilder element() {
		ElementBuilder elementBuilder = new ElementBuilder();
		this.elementsBuilders.add(elementBuilder);
		return elementBuilder;
	}

	public ElementBuilder element(int index) {
		Preconditions.checkElementIndex(index, this.elementsBuilders.size(), "Element index");
		return this.elementsBuilders.get(index);
	}

	public int getElementCount() {
		return this.elementsBuilders.size();
	}

	public RootTransformsBuilder rootTransforms() {
		return this.rootTransforms;
	}

	@VisibleForTesting
	public JsonObject toJson() {
		JsonObject root = new JsonObject();

		if (this.parent != null) {
			root.addProperty("parent", this.parent.getResourceLocation().toString());
		}

		if (!this.ambientOcclusion) {
			root.addProperty("ambientocclusion", this.ambientOcclusion);
		}

		if (this.guiLight != null) {
			root.addProperty("gui_light", this.guiLight.name);
		}

		if (this.renderType != null) {
			root.addProperty("render_type", this.renderType);
		}

		Map<ItemDisplayContext, ItemTransform> transforms = this.transformsBuilder.build();
		if (!transforms.isEmpty()) {
			JsonObject display = new JsonObject();
			for (Entry<ItemDisplayContext, ItemTransform> e : transforms.entrySet()) {
				JsonObject transform = new JsonObject();
				ItemTransform vec = e.getValue();
				if (vec.equals(ItemTransform.NO_TRANSFORM))
					continue;
				if (!vec.translation.equals(ItemTransform.Deserializer.DEFAULT_TRANSLATION)) {
					transform.add("translation", serializeVector3f(e.getValue().translation));
				}
				if (!vec.rotation.equals(ItemTransform.Deserializer.DEFAULT_ROTATION)) {
					transform.add("rotation", serializeVector3f(vec.rotation));
				}
				if (!vec.scale.equals(ItemTransform.Deserializer.DEFAULT_SCALE)) {
					transform.add("scale", serializeVector3f(e.getValue().scale));
				}
				display.add(e.getKey().getSerializedName(), transform);
			}
			root.add("display", display);
		}

		if (!this.textures.isEmpty()) {
			JsonObject textures = new JsonObject();
			for (Entry<String, String> e : this.textures.entrySet()) {
				textures.addProperty(e.getKey(), serializeLocOrKey(e.getValue()));
			}
			root.add("textures", textures);
		}

		if (!this.elementsBuilders.isEmpty()) {
			JsonArray elements = new JsonArray();
			this.elementsBuilders.stream().map(ElementBuilder::build).forEach(part -> {
				JsonObject partObj = new JsonObject();
				partObj.add("from", serializeVector3f(part.from));
				partObj.add("to", serializeVector3f(part.to));

				if (part.rotation != null) {
					JsonObject rotation = new JsonObject();
					rotation.add("origin", serializeVector3f(part.rotation.origin()));
					rotation.addProperty("axis", part.rotation.axis().getSerializedName());
					rotation.addProperty("angle", part.rotation.angle());
					if (part.rotation.rescale()) {
						rotation.addProperty("rescale", part.rotation.rescale());
					}
					partObj.add("rotation", rotation);
				}

				if (!part.shade) {
					partObj.addProperty("shade", part.shade);
				}

				JsonObject faces = new JsonObject();
				for (Direction dir : Direction.values()) {
					BlockElementFace face = part.faces.get(dir);
					if (face == null)
						continue;

					JsonObject faceObj = new JsonObject();
					faceObj.addProperty("texture", serializeLocOrKey(face.texture));
					if (!Arrays.equals(face.blockFaceUV.uvs, part.uvsByFace(dir))) {
						faceObj.add("uv", new Gson().toJsonTree(face.blockFaceUV.uvs));
					}
					if (face.cullForDirection != null) {
						faceObj.addProperty("cullface", face.cullForDirection.getSerializedName());
					}
					if (face.blockFaceUV.rotation != 0) {
						faceObj.addProperty("rotation", face.blockFaceUV.rotation);
					}
					if (face.tintIndex != -1) {
						faceObj.addProperty("tintindex", face.tintIndex);
					}
					if (!face.getFaceData().equals(ForgeFaceData.DEFAULT)) {
						faceObj.add("forge_data", ForgeFaceData.CODEC.encodeStart(JsonOps.INSTANCE, face.getFaceData()).result().get());
					}
					faces.add(dir.getSerializedName(), faceObj);
				}
				if (!part.faces.isEmpty()) {
					partObj.add("faces", faces);
				}
				elements.add(partObj);
			});
			root.add("elements", elements);
		}

		JsonObject transform = rootTransforms.toJson();
		if (transform.size() > 0) {
			root.add("transform", transform);
		}

		return root;
	}

	private String serializeLocOrKey(String tex) {
		if (tex.charAt(0) == '#') {
			return tex;
		}
		return new ResourceLocation(tex).toString();
	}

	private JsonArray serializeVector3f(Vector3f vec) {
		JsonArray ret = new JsonArray();
		ret.add(serializeFloat(vec.x()));
		ret.add(serializeFloat(vec.y()));
		ret.add(serializeFloat(vec.z()));
		return ret;
	}

	private Number serializeFloat(float f) {
		if ((int) f == f) {
			return (int) f;
		}
		return f;
	}

	public class ElementBuilder {
		private Vector3f from = new Vector3f();
		private Vector3f to = new Vector3f(16, 16, 16);
		private final Map<Direction, FaceBuilder> faces = new LinkedHashMap<>();
		private RotationBuilder rotation;
		private boolean shade = true;
		private int color = 0xFFFFFFFF;
		private int blockLight = 0, skyLight = 0;
		private boolean hasAmbientOcclusion = true;

		private void validateCoordinate(float coord, char name) {
			Preconditions.checkArgument(!(coord < -16.0F) && !(coord > 32.0F), "Position " + name + " out of range, must be within [-16, 32]. Found: %d", coord);
		}

		private void validatePosition(Vector3f pos) {
			this.validateCoordinate(pos.x(), 'x');
			this.validateCoordinate(pos.y(), 'y');
			this.validateCoordinate(pos.z(), 'z');
		}

		public ElementBuilder from(float x, float y, float z) {
			this.from = new Vector3f(x, y, z);
			this.validatePosition(this.from);
			return this;
		}

		public ElementBuilder to(float x, float y, float z) {
			this.to = new Vector3f(x, y, z);
			this.validatePosition(this.to);
			return this;
		}

		public FaceBuilder face(Direction direction) {
			Preconditions.checkNotNull(direction, "Direction must not be null");
			return faces.computeIfAbsent(direction, FaceBuilder::new);
		}

		public RotationBuilder rotation() {
			if (this.rotation == null) {
				this.rotation = new RotationBuilder();
			}
			return this.rotation;
		}

		public ElementBuilder shade(boolean shade) {
			this.shade = shade;
			return this;
		}

		public ElementBuilder allFaces(BiConsumer<Direction, FaceBuilder> action) {
			Arrays.stream(Direction.values()).forEach(d -> action.accept(d, face(d)));
			return this;
		}

		public ElementBuilder faces(BiConsumer<Direction, FaceBuilder> action) {
			faces.entrySet().stream().forEach(e -> action.accept(e.getKey(), e.getValue()));
			return this;
		}

		public ElementBuilder textureAll(String texture) {
			return allFaces(addTexture(texture));
		}

		public ElementBuilder texture(String texture) {
			return faces(addTexture(texture));
		}

		public ElementBuilder cube(String texture) {
			return allFaces(addTexture(texture).andThen((dir, f) -> f.cullface(dir)));
		}

		public ElementBuilder emissivity(int blockLight, int skyLight) {
			this.blockLight = blockLight;
			this.skyLight = skyLight;
			return this;
		}

		public ElementBuilder color(int color) {
			this.color = color;
			return this;
		}

		public ElementBuilder ao(boolean ao) {
			this.hasAmbientOcclusion = ao;
			return this;
		}

		private BiConsumer<Direction, FaceBuilder> addTexture(String texture) {
			return ($, f) -> f.texture(texture);
		}

		BlockElement build() {
			Map<Direction, BlockElementFace> faces = this.faces.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().build(), (k1, k2) -> {
				throw new IllegalArgumentException();
			}, LinkedHashMap::new));
			return new BlockElement(from, to, faces, rotation == null ? null : rotation.build(), shade, new ForgeFaceData(this.color, this.blockLight, this.skyLight, this.hasAmbientOcclusion));
		}

		public T end() {
			return self();
		}

		public class FaceBuilder {
			private Direction cullface;
			private int tintindex = -1;
			private String texture = MissingTextureAtlasSprite.getLocation().toString();
			private float[] uvs;
			private FaceRotation rotation = FaceRotation.ZERO;
			private int color = 0xFFFFFFFF;
			private int blockLight = 0, skyLight = 0;
			private boolean hasAmbientOcclusion = true;

			private FaceBuilder(Direction direction) {
			}

			public FaceBuilder cullface(@Nullable Direction direction) {
				this.cullface = direction;
				return this;
			}

			public FaceBuilder tintindex(int index) {
				this.tintindex = index;
				return this;
			}

			public FaceBuilder texture(String texture) {
				Preconditions.checkNotNull(texture, "Texture must not be null");
				this.texture = texture;
				return this;
			}

			public FaceBuilder uvs(float u1, float v1, float u2, float v2) {
				this.uvs = new float[] { u1, v1, u2, v2 };
				return this;
			}

			public FaceBuilder rotation(FaceRotation faceRotation) {
				Preconditions.checkNotNull(faceRotation, "Rotation must not be null");
				this.rotation = faceRotation;
				return this;
			}

			public FaceBuilder emissivity(int blockLight, int skyLight) {
				this.blockLight = blockLight;
				this.skyLight = skyLight;
				return this;
			}

			public FaceBuilder color(int color) {
				this.color = color;
				return this;
			}

			public FaceBuilder ao(boolean ao) {
				this.hasAmbientOcclusion = ao;
				return this;
			}

			private BlockElementFace build() {
				if (this.texture == null) {
					throw new IllegalStateException("A model face must have a texture");
				}
				return new BlockElementFace(cullface, tintindex, texture, new BlockFaceUV(uvs, rotation.rotation), new ForgeFaceData(this.color, this.blockLight, this.skyLight, this.hasAmbientOcclusion));
			}

			public ElementBuilder end() {
				return ElementBuilder.this;
			}
		}

		public class RotationBuilder {
			private Vector3f origin;
			private Direction.Axis axis;
			private float angle;
			private boolean rescale;

			public RotationBuilder origin(float x, float y, float z) {
				this.origin = new Vector3f(x, y, z);
				return this;
			}

			public RotationBuilder axis(Direction.Axis axis) {
				Preconditions.checkNotNull(axis, "Axis must not be null");
				this.axis = axis;
				return this;
			}

			public RotationBuilder angle(float angle) {
				Preconditions.checkArgument(angle == 0.0F || Mth.abs(angle) == 22.5F || Mth.abs(angle) == 45.0F, "Invalid rotation %f found, only -45/-22.5/0/22.5/45 allowed", angle);
				this.angle = angle;
				return this;
			}

			public RotationBuilder rescale(boolean rescale) {
				this.rescale = rescale;
				return this;
			}

			BlockElementRotation build() {
				return new BlockElementRotation(this.origin, this.axis, this.angle, this.rescale);
			}

			public ElementBuilder end() {
				return ElementBuilder.this;
			}
		}
	}

	public enum FaceRotation {
		ZERO(0),
		CLOCKWISE_90(90),
		UPSIDE_DOWN(180),
		COUNTERCLOCKWISE_90(270),;

		private final int rotation;

		private FaceRotation(int rotation) {
			this.rotation = rotation;
		}

		public int getRotation() {
			return this.rotation;
		}
	}

	public class TransformsBuilder {
		private final Map<ItemDisplayContext, TransformVecBuilder> transforms = new LinkedHashMap<>();

		public TransformVecBuilder transform(ItemDisplayContext type) {
			Preconditions.checkNotNull(type, "Perspective cannot be null");
			return transforms.computeIfAbsent(type, TransformVecBuilder::new);
		}

		Map<ItemDisplayContext, ItemTransform> build() {
			return this.transforms.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().build(), (k1, k2) -> {
				throw new IllegalArgumentException();
			}, LinkedHashMap::new));
		}

		public T end() {
			return self();
		}

		public class TransformVecBuilder {
			private Vector3f rotation = new Vector3f(ItemTransform.Deserializer.DEFAULT_ROTATION);
			private Vector3f translation = new Vector3f(ItemTransform.Deserializer.DEFAULT_TRANSLATION);
			private Vector3f scale = new Vector3f(ItemTransform.Deserializer.DEFAULT_SCALE);

			protected TransformVecBuilder(ItemDisplayContext itemDisplayContext) {
			}

			public TransformVecBuilder rotation(float x, float y, float z) {
				this.rotation = new Vector3f(x, y, z);
				return this;
			}

			public TransformVecBuilder leftRotation(float x, float y, float z) {
				return this.rotation(x, y, z);
			}

			public TransformVecBuilder translation(float x, float y, float z) {
				this.translation = new Vector3f(x, y, z);
				return this;
			}

			public TransformVecBuilder scale(float sc) {
				return this.scale(sc, sc, sc);
			}

			public TransformVecBuilder scale(float x, float y, float z) {
				this.scale = new Vector3f(x, y, z);
				return this;
			}

			ItemTransform build() {
				return new ItemTransform(this.rotation, this.translation, this.scale);
			}

			public TransformsBuilder end() {
				return TransformsBuilder.this;
			}
		}
	}

	public class RootTransformsBuilder {
		private static final Vector3f ONE = new Vector3f(1, 1, 1);

		private Vector3f translation = new Vector3f();
		private Quaternionf leftRotation = new Quaternionf();
		private Quaternionf rightRotation = new Quaternionf();
		private Vector3f scale = ONE;

		private @Nullable TransformationHelper.TransformOrigin transformOrigin;
		private @Nullable Vector3f originVec;

		RootTransformsBuilder() {
		}

		public RootTransformsBuilder translation(Vector3f translation) {
			this.translation = Preconditions.checkNotNull(translation, "Translation must not be null");
			return this;
		}

		public RootTransformsBuilder translation(float x, float y, float z) {
			return translation(new Vector3f(x, y, z));
		}

		public RootTransformsBuilder rotation(Quaternionf quaternionf) {
			this.leftRotation = Preconditions.checkNotNull(quaternionf, "Rotation must not be null");
			return this;
		}

		public RootTransformsBuilder rotation(float x, float y, float z, boolean isDegrees) {
			return this.rotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
		}

		public RootTransformsBuilder leftRotation(Quaternionf quaternionf) {
			return this.rotation(quaternionf);
		}

		public RootTransformsBuilder leftRotation(float x, float y, float z, boolean isDegrees) {
			return this.leftRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
		}

		public RootTransformsBuilder rightRotation(Quaternionf quaternionf) {
			this.rightRotation = Preconditions.checkNotNull(quaternionf, "Rotation must not be null");
			return this;
		}

		public RootTransformsBuilder rightRotation(float x, float y, float z, boolean isDegrees) {
			return this.rightRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
		}

		public RootTransformsBuilder postRotation(Quaternionf quaternionf) {
			return this.rightRotation(quaternionf);
		}

		public RootTransformsBuilder postRotation(float x, float y, float z, boolean isDegrees) {
			return this.postRotation(TransformationHelper.quatFromXYZ(x, y, z, isDegrees));
		}

		public RootTransformsBuilder scale(float scale) {
			return this.scale(new Vector3f(scale, scale, scale));
		}

		public RootTransformsBuilder scale(float xScale, float yScale, float zScale) {
			return this.scale(new Vector3f(xScale, yScale, zScale));
		}

		public RootTransformsBuilder scale(Vector3f vector3f) {
			this.scale = Preconditions.checkNotNull(vector3f, "Scale must not be null");
			return this;
		}

		public RootTransformsBuilder transform(Transformation transformation) {
			Preconditions.checkNotNull(transformation, "Transformation must not be null");
			this.translation = transformation.getTranslation();
			this.leftRotation = transformation.getLeftRotation();
			this.rightRotation = transformation.getRightRotation();
			this.scale = transformation.getScale();
			return this;
		}

		public RootTransformsBuilder origin(Vector3f vector3f) {
			this.originVec = Preconditions.checkNotNull(vector3f, "Origin must not be null");
			this.transformOrigin = null;
			return this;
		}

		public RootTransformsBuilder origin(TransformationHelper.TransformOrigin transformOrigin) {
			this.transformOrigin = Preconditions.checkNotNull(transformOrigin, "Origin must not be null");
			this.originVec = null;
			return this;
		}

		public ModelBuilder<T> end() {
			return ModelBuilder.this;
		}

		public JsonObject toJson() {
			JsonObject jsonObject = new JsonObject();

			if (!translation.equals(0, 0, 0)) {
				jsonObject.add("translation", writeVec3(translation));
			}

			if (!scale.equals(ONE)) {
				jsonObject.add("scale", writeVec3(scale));
			}

			if (!leftRotation.equals(0, 0, 0, 1)) {
				jsonObject.add("rotation", writeQuaternion(leftRotation));
			}

			if (!rightRotation.equals(0, 0, 0, 1)) {
				jsonObject.add("post_rotation", writeQuaternion(rightRotation));
			}

			if (this.transformOrigin != null) {
				jsonObject.addProperty("origin", transformOrigin.getSerializedName());
			} else if (originVec != null && !originVec.equals(0, 0, 0)) {
				jsonObject.add("origin", writeVec3(originVec));
			}

			return jsonObject;
		}

		private static JsonArray writeVec3(Vector3f vector3f) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(vector3f.x());
			jsonArray.add(vector3f.y());
			jsonArray.add(vector3f.z());
			return jsonArray;
		}

		private static JsonArray writeQuaternion(Quaternionf quaternionf) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(quaternionf.x());
			jsonArray.add(quaternionf.y());
			jsonArray.add(quaternionf.z());
			jsonArray.add(quaternionf.w());
			return jsonArray;
		}
	}
}