package willatendo.simplelibrary.client.event.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.ResourceLocation;

public final class FabricSpecialRendererRegistry implements SpecialRendererRegistry {
    @Override
    public void register(ResourceLocation id, MapCodec<? extends SpecialModelRenderer.Unbaked> specialModelRenderer) {
        SpecialModelRenderers.ID_MAPPER.put(id, specialModelRenderer);
    }
}
