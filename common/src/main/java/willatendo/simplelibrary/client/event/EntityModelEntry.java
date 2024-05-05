package willatendo.simplelibrary.client.event;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record EntityModelEntry<T extends Entity>(EntityType<T> entityType, EntityRendererProvider<T> entityRendererProvider) {
}
