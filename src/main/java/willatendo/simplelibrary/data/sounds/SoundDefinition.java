package willatendo.simplelibrary.data.sounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.resources.ResourceLocation;

public final class SoundDefinition {
	private final List<Sound> sounds = new ArrayList<>();
	private boolean replace = false;
	private String subtitle = null;

	private SoundDefinition() {
	}

	public static SoundDefinition definition() {
		return new SoundDefinition();
	}

	public SoundDefinition replace(boolean replace) {
		this.replace = replace;
		return this;
	}

	public SoundDefinition subtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public SoundDefinition with(Sound sound) {
		this.sounds.add(sound);
		return this;
	}

	public SoundDefinition with(Sound... sounds) {
		this.sounds.addAll(Arrays.asList(sounds));
		return this;
	}

	public List<Sound> soundList() {
		return this.sounds;
	}

	public JsonObject serialize() {
		if (this.sounds.isEmpty()) {
			throw new IllegalStateException("Unable to serialize a sound definition that has no sounds!");
		}

		final JsonObject object = new JsonObject();
		if (this.replace)
			object.addProperty("replace", true);
		if (this.subtitle != null)
			object.addProperty("subtitle", this.subtitle);
		final JsonArray sounds = new JsonArray();
		this.sounds.stream().map(Sound::serialize).forEach(sounds::add);
		object.add("sounds", sounds);
		return object;
	}

	public static final class Sound {
		private static final SoundType DEFAULT_TYPE = SoundType.SOUND;
		private static final float DEFAULT_VOLUME = 1.0F;
		private static final float DEFAULT_PITCH = 1.0F;
		private static final int DEFAULT_WEIGHT = 1;
		private static final boolean DEFAULT_STREAM = false;
		private static final int DEFAULT_ATTENUATION_DISTANCE = 16;
		private static final boolean DEFAULT_PRELOAD = false;

		private final ResourceLocation resourceLocation;
		private final SoundType soundType;

		private float volume = DEFAULT_VOLUME;
		private float pitch = DEFAULT_PITCH;
		private int weight = DEFAULT_WEIGHT;
		private boolean stream = DEFAULT_STREAM;
		private int attenuationDistance = DEFAULT_ATTENUATION_DISTANCE;
		private boolean preload = DEFAULT_PRELOAD;

		private Sound(ResourceLocation resourceLocation, SoundType soundType) {
			this.resourceLocation = resourceLocation;
			this.soundType = soundType;
		}

		public static Sound sound(ResourceLocation resourceLocation, SoundType soundType) {
			return new Sound(resourceLocation, soundType);
		}

		public Sound volume(double volume) {
			return this.volume((float) volume);
		}

		public Sound volume(float volume) {
			if (volume <= 0.0F) {
				throw new IllegalArgumentException("Volume must be positive for sound " + this.resourceLocation + ", but instead got " + volume);
			}
			this.volume = volume;
			return this;
		}

		public Sound pitch(double pitch) {
			return this.pitch((float) pitch);
		}

		public Sound pitch(float pitch) {
			if (pitch <= 0.0F) {
				throw new IllegalArgumentException("Pitch must be positive for sound " + this.resourceLocation + ", but instead got " + pitch);
			}
			this.pitch = pitch;
			return this;
		}

		public Sound weight(int weight) {
			if (weight <= 0) {
				throw new IllegalArgumentException("Weight has to be a positive number in sound " + this.resourceLocation + ", but instead got " + weight);
			}
			this.weight = weight;
			return this;
		}

		public Sound stream() {
			return this.stream(true);
		}

		public Sound stream(boolean stream) {
			this.stream = stream;
			return this;
		}

		public Sound attenuationDistance(int attenuationDistance) {
			this.attenuationDistance = attenuationDistance;
			return this;
		}

		public Sound preload() {
			return this.preload(true);
		}

		public Sound preload(final boolean preload) {
			this.preload = preload;
			return this;
		}

		public ResourceLocation name() {
			return this.resourceLocation;
		}

		public SoundType type() {
			return this.soundType;
		}

		public JsonElement serialize() {
			if (this.canBeInShortForm()) {
				return new JsonPrimitive(this.stripMcPrefix(this.resourceLocation));
			}

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("name", this.stripMcPrefix(this.resourceLocation));
			if (this.soundType != DEFAULT_TYPE)
				jsonObject.addProperty("type", this.soundType.jsonString);
			if (this.volume != DEFAULT_VOLUME)
				jsonObject.addProperty("volume", this.volume);
			if (this.pitch != DEFAULT_PITCH)
				jsonObject.addProperty("pitch", this.pitch);
			if (this.weight != DEFAULT_WEIGHT)
				jsonObject.addProperty("weight", this.weight);
			if (this.stream != DEFAULT_STREAM)
				jsonObject.addProperty("stream", this.stream);
			if (this.preload != DEFAULT_PRELOAD)
				jsonObject.addProperty("preload", this.preload);
			if (this.attenuationDistance != DEFAULT_ATTENUATION_DISTANCE)
				jsonObject.addProperty("attenuation_distance", this.attenuationDistance);
			return jsonObject;
		}

		private boolean canBeInShortForm() {
			return this.soundType == DEFAULT_TYPE && this.volume == DEFAULT_VOLUME && this.pitch == DEFAULT_PITCH && this.weight == DEFAULT_WEIGHT && this.stream == DEFAULT_STREAM && this.attenuationDistance == DEFAULT_ATTENUATION_DISTANCE && this.preload == DEFAULT_PRELOAD;
		}

		private String stripMcPrefix(final ResourceLocation name) {
			return "minecraft".equals(name.getNamespace()) ? name.getPath() : name.toString();
		}
	}

	public enum SoundType {
		SOUND("sound"),
		EVENT("event");

		private final String jsonString;

		private SoundType(String jsonString) {
			this.jsonString = jsonString;
		}
	}
}
