package ca.willatendo.simplelibrary.data.model;

import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.resources.Identifier;

public final class SimpleModelTemplates {
    public static ModelTemplate cutout(ModelTemplate modelTemplate) {
        return SimpleModelTemplates.renderType(modelTemplate, CoreUtils.minecraft("cutout"));
    }

    public static ModelTemplate translucent(ModelTemplate modelTemplate) {
        return SimpleModelTemplates.renderType(modelTemplate, CoreUtils.minecraft("translucent"));
    }

    public static ModelTemplate renderType(ModelTemplate modelTemplate, Identifier renderType) {
        return modelTemplate.extend().renderType(renderType).build();
    }
}
