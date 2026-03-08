package ca.willatendo.simplelibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.DetectedVersion;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class SimplePackMetadataGenerator implements DataProvider {
    private final Map<String, Supplier<JsonElement>> elements = new HashMap<>();
    private final PackOutput packOutput;
    private final String modId;
    private final Optional<String> packName;

    public SimplePackMetadataGenerator(PackOutput packOutput, String modId, Optional<String> packName) {
        this.packOutput = packOutput;
        this.modId = modId;
        this.packName = packName;
    }

    public <T> SimplePackMetadataGenerator add(MetadataSectionType<T> type, T value) {
        this.elements.put(type.name(), () -> type.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow(IllegalArgumentException::new).getAsJsonObject());
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        JsonObject jsonObject = new JsonObject();
        this.elements.forEach((name, jsonElement) -> jsonObject.add(name, jsonElement.get()));
        return DataProvider.saveStable(cachedOutput, jsonObject, this.packOutput.getOutputFolder().resolve("pack.mcmeta"));
    }

    @Override
    public String getName() {
        String type = "";
        type += this.modId;
        if (this.packName.isPresent()) {
            type += "/" + this.packName.get();
        }
        return "Pack Metadata: " + type;
    }
}
