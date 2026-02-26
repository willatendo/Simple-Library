package ca.willatendo.simplelibrary.data;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SimpleAtlasProvider implements DataProvider {
    private final Map<Identifier, List<SpriteSource>> atlases = new HashMap<>();
    private final PackOutput.PathProvider pathProvider;
    private final String modId;

    public SimpleAtlasProvider(PackOutput packOutput, String modId) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
        this.modId = modId;
    }

    protected abstract void getAll();

    protected void add(Identifier identifier, List<SpriteSource> spriteSources) {
        this.atlases.put(identifier, spriteSources);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.getAll();
        List<CompletableFuture<?>> completableFutures = new ArrayList<>();
        this.atlases.forEach((identifier, spriteSources) -> completableFutures.add(DataProvider.saveStable(cachedOutput, SpriteSources.FILE_CODEC, spriteSources, this.pathProvider.json(identifier))));
        return CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Atlas Provider: " + this.modId;
    }
}
