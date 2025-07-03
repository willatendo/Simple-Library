package willatendo.simplelibrary.data;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.Optional;

public final class DataHelper {
    public static TextureSlot create(String name) {
        return TextureSlot.create(name);
    }

    public static TextureSlot create(String name, TextureSlot parent) {
        return TextureSlot.create(name, parent);
    }

    private static ExtendedModelTemplateBuilder create(ResourceLocation location, Optional<String> suffix, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(location), suffix, textureSlots).extend();
    }

    public static ExtendedModelTemplateBuilder create(ResourceLocation location, TextureSlot... textureSlots) {
        return DataHelper.create(location, Optional.empty(), textureSlots);
    }

    public static ExtendedModelTemplateBuilder create(ResourceLocation location, String suffix, TextureSlot... textureSlots) {
        return DataHelper.create(location, Optional.of(suffix), textureSlots);
    }

    public static ExtendedModelTemplateBuilder createBlockMC(String location, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.withDefaultNamespace("block/" + location), textureSlots);
    }

    public static ExtendedModelTemplateBuilder createBlockMC(String location, String suffix, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.withDefaultNamespace("block/" + location), suffix, textureSlots);
    }

    public static ExtendedModelTemplateBuilder createItemMC(String location, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.withDefaultNamespace("item/" + location), textureSlots);
    }

    public static ExtendedModelTemplateBuilder createItemMC(String location, String suffix, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.withDefaultNamespace("item/" + location), suffix, textureSlots);
    }

    public static ExtendedModelTemplateBuilder createBlock(String modId, String location, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.fromNamespaceAndPath(modId, "block/" + location), textureSlots);
    }

    public static ExtendedModelTemplateBuilder createBlock(String modId, String location, String suffix, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.fromNamespaceAndPath(modId, "block/" + location), suffix, textureSlots);
    }

    public static ExtendedModelTemplateBuilder createItem(String modId, String location, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.fromNamespaceAndPath(modId, "item/" + location), textureSlots);
    }

    public static ExtendedModelTemplateBuilder createItem(String modId, String location, String suffix, TextureSlot... textureSlots) {
        return DataHelper.create(ResourceLocation.fromNamespaceAndPath(modId, "item/" + location), suffix, textureSlots);
    }
}
