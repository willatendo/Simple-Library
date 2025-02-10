package willatendo.simplelibrary.client.event.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

public final class NeoforgeSpecialRendererRegistry implements SpecialRendererRegistry {
    private final RegisterSpecialModelRendererEvent event;

    public NeoforgeSpecialRendererRegistry(RegisterSpecialModelRendererEvent event) {
        this.event = event;
    }

    @Override
    public void register(ResourceLocation id, MapCodec<? extends SpecialModelRenderer.Unbaked> specialModelRenderer) {
        this.event.register(id, specialModelRenderer);
    }
}
