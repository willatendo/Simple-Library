package willatendo.simplelibrary.client.event.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ForgeSpecialRendererRegistry implements SpecialRendererRegistry {
    private final FMLClientSetupEvent event;

    public ForgeSpecialRendererRegistry(FMLClientSetupEvent event) {
        this.event = event;
    }

    @Override
    public void register(ResourceLocation id, MapCodec<? extends SpecialModelRenderer.Unbaked> specialModelRenderer) {
        this.event.enqueueWork(() -> SpecialModelRenderers.ID_MAPPER.put(id, specialModelRenderer));
    }
}
