package ca.willatendo.simplelibrary.data.providers;

import ca.willatendo.simplelibrary.data.SoundDefinition;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleSoundDefinitionsProvider implements DataProvider {
    private final Map<String, SoundDefinition> sounds = new LinkedHashMap<>();
    private final PackOutput packOutput;
    private final String modId;

    public SimpleSoundDefinitionsProvider(PackOutput packOutput, String modId) {
        this.packOutput = packOutput;
        this.modId = modId;
    }

    public abstract void registerSounds();

    protected static SoundDefinition definition() {
        return SoundDefinition.definition();
    }

    protected static SoundDefinition.Sound sound(final Identifier name, final SoundDefinition.SoundType type) {
        return SoundDefinition.Sound.sound(name, type);
    }

    protected static SoundDefinition.Sound sound(final Identifier name) {
        return sound(name, SoundDefinition.SoundType.SOUND);
    }

    protected static SoundDefinition.Sound sound(final String name, final SoundDefinition.SoundType type) {
        return sound(Identifier.parse(name), type);
    }

    protected static SoundDefinition.Sound sound(final String name) {
        return sound(Identifier.parse(name));
    }

    protected void add(final Holder<SoundEvent> soundEvent, final SoundDefinition definition) {
        this.add(soundEvent.value(), definition);
    }

    protected void add(final SoundEvent soundEvent, final SoundDefinition definition) {
        this.add(soundEvent.location(), definition);
    }

    protected void add(final Identifier soundEvent, final SoundDefinition definition) {
        this.addSounds(soundEvent.getPath(), definition);
    }

    protected void add(final String soundEvent, final SoundDefinition definition) {
        this.add(Identifier.parse(soundEvent), definition);
    }

    private void addSounds(final String soundEvent, final SoundDefinition definition) {
        if (this.sounds.put(soundEvent, definition) != null) {
            throw new IllegalStateException("Sound event '" + this.modId + ":" + soundEvent + "' already exists");
        }
    }

    public void add(SoundEvent soundEvent, String base, String type, String event, String... sounds) {
        SoundDefinition soundDefinition = SoundDefinition.definition().subtitle("subtitles." + type + "." + base + "." + event);
        for (String sound : sounds) {
            soundDefinition.with(SoundDefinition.Sound.sound(Identifier.fromNamespaceAndPath(this.modId, sound), SoundDefinition.SoundType.SOUND));
        }
        this.add(soundEvent, soundDefinition);
    }

    public void block(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "block", event, sounds);
    }

    public void entity(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "entity", event, sounds);
    }

    public void item(SoundEvent soundEvent, String base, String event, String... sounds) {
        this.add(soundEvent, base, "item", event, sounds);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.sounds.clear();
        this.registerSounds();
        this.validate();
        if (!this.sounds.isEmpty()) {
            return this.save(cache, this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modId).resolve("sounds.json"));
        }

        return CompletableFuture.allOf();
    }

    private void validate() {
        final List<String> notValid = this.sounds.entrySet().stream().filter(it -> !this.validate(it.getKey(), it.getValue())).map(Map.Entry::getKey).map(it -> this.modId + ":" + it).toList();
        if (!notValid.isEmpty()) {
            throw new IllegalStateException("Found invalid sound events: " + notValid);
        }
    }

    private boolean validate(final String name, final SoundDefinition def) {
        return def.soundList().stream().filter(it -> it.type() == SoundDefinition.SoundType.EVENT).allMatch(it -> {
            final boolean valid = this.sounds.containsKey(name) || BuiltInRegistries.SOUND_EVENT.containsKey(it.name());
            if (!valid) {
                LOGGER.warn("Unable to find event '{}' referenced from '{}'", it.name(), name);
            }
            return valid;
        });
    }

    private CompletableFuture<?> save(final CachedOutput cache, final Path targetFile) {
        return DataProvider.saveStable(cache, this.mapToJson(this.sounds), targetFile);
    }

    private JsonObject mapToJson(final Map<String, SoundDefinition> map) {
        final JsonObject jsonObject = new JsonObject();
        map.forEach((name, soundDefinition) -> jsonObject.add(name, soundDefinition.serialize()));
        return jsonObject;
    }

    @Override
    public String getName() {
        return "SimpleLibrary: Sound Definitions Provider for " + this.modId;
    }
}
