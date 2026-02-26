package ca.willatendo.simplelibrary.mixin.client;

import ca.willatendo.simplelibrary.server.event.RegisterTextureAtlasesEvent;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.resources.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {
    @Shadow
    @Final
    private Map<Identifier, AtlasManager.AtlasEntry> atlasByTexture;
    @Shadow
    @Final
    private Map<Identifier, AtlasManager.AtlasEntry> atlasById;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(TextureManager textureManager, int maxMipmapLevels, CallbackInfo ci) {
        List<AtlasManager.AtlasConfig> moddedAtlases = Lists.newArrayList();
        RegisterTextureAtlasesEvent.EVENT.invoker().register(moddedAtlases::add);
        for (AtlasManager.AtlasConfig atlasConfig : moddedAtlases) {
            TextureAtlas textureAtlas = new TextureAtlas(atlasConfig.textureId());
            textureManager.register(atlasConfig.textureId(), textureAtlas);
            AtlasManager.AtlasEntry atlasEntry = new AtlasManager.AtlasEntry(textureAtlas, atlasConfig);
            this.atlasByTexture.put(atlasConfig.textureId(), atlasEntry);
            this.atlasById.put(atlasConfig.definitionLocation(), atlasEntry);
        }
    }
}
