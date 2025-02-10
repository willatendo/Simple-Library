package willatendo.simplelibrary.client.event.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;

public interface SpecialRendererRegistry {
    void register(ResourceLocation id, MapCodec<? extends SpecialModelRenderer.Unbaked> specialModelRenderer);
}
