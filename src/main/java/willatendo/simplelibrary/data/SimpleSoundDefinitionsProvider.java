package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import willatendo.simplelibrary.data.sounds.SoundDefinition;

public abstract class SimpleSoundDefinitionsProvider implements DataProvider {
	private final FabricDataOutput output;
	private final String modId;

	private final Map<String, SoundDefinition> sounds = new LinkedHashMap<>();

	public SimpleSoundDefinitionsProvider(FabricDataOutput fabricDataOutput) {
		this.output = fabricDataOutput;
		this.modId = fabricDataOutput.getModId();
	}

	public abstract void registerSounds();

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		this.sounds.clear();
		this.registerSounds();
		if (!this.sounds.isEmpty()) {
			return this.save(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modId).resolve("sounds.json"));
		}

		return CompletableFuture.allOf();
	}

	protected static SoundDefinition definition() {
		return SoundDefinition.definition();
	}

	protected static SoundDefinition.Sound sound(ResourceLocation resourceLocation, SoundDefinition.SoundType soundType) {
		return SoundDefinition.Sound.sound(resourceLocation, soundType);
	}

	protected static SoundDefinition.Sound sound(ResourceLocation resourceLocation) {
		return sound(resourceLocation, SoundDefinition.SoundType.SOUND);
	}

	protected static SoundDefinition.Sound sound(String name, SoundDefinition.SoundType soundType) {
		return sound(new ResourceLocation(name), soundType);
	}

	protected static SoundDefinition.Sound sound(String name) {
		return sound(new ResourceLocation(name));
	}

	protected void add(Supplier<SoundEvent> soundEvent, SoundDefinition soundDefinitions) {
		this.add(soundEvent.get(), soundDefinitions);
	}

	protected void add(SoundEvent soundEvent, SoundDefinition soundDefinitions) {
		this.add(soundEvent.getLocation(), soundDefinitions);
	}

	protected void add(ResourceLocation resourceLocation, SoundDefinition soundDefinition) {
		this.addSounds(resourceLocation.getPath(), soundDefinition);
	}

	protected void add(String name, SoundDefinition soundDefinition) {
		this.add(new ResourceLocation(name), soundDefinition);
	}

	private void addSounds(String name, SoundDefinition soundDefinition) {
		if (this.sounds.put(name, soundDefinition) != null) {
			throw new IllegalStateException("Sound event '" + this.modId + ":" + name + "' already exists");
		}
	}

	private CompletableFuture<?> save(final CachedOutput cache, final Path targetFile) {
		return DataProvider.saveStable(cache, this.mapToJson(this.sounds), targetFile);
	}

	private JsonObject mapToJson(final Map<String, SoundDefinition> map) {
		JsonObject jsonObject = new JsonObject();
		map.forEach((name, soundDefinition) -> jsonObject.add(name, soundDefinition.serialize()));
		return jsonObject;
	}

	@Override
	public String getName() {
		return this.modId + ": Item Models";
	}
}
