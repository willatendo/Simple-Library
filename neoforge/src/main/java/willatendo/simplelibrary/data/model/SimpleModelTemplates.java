package willatendo.simplelibrary.data.model;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.resources.ResourceLocation;

public final class SimpleModelTemplates {
    public static ModelTemplate cutout(ModelTemplate modelTemplate) {
        return SimpleModelTemplates.renderType(modelTemplate, ResourceLocation.withDefaultNamespace("cutout"));
    }

    public static ModelTemplate translucent(ModelTemplate modelTemplate) {
        return SimpleModelTemplates.renderType(modelTemplate, ResourceLocation.withDefaultNamespace("translucent"));
    }

    public static ModelTemplate renderType(ModelTemplate modelTemplate, ResourceLocation renderType) {
        return modelTemplate.extend().renderType(renderType).build();
    }
}
