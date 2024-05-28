package willatendo.simplelibrary.client.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record SkyRendererEntry(ResourceKey<Level> levelResourceKey, SkyRendererProvider skyRendererProvider) {
}
