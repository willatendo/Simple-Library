package willatendo.simplelibrary.server;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagRegister<T> {
	private final ResourceKey<? extends Registry<T>> resourceKey;
	private final String modId;

	public static <T> TagRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		return new TagRegister<>(resourceKey, modId);
	}

	public TagRegister(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		this.resourceKey = resourceKey;
		this.modId = modId;
	}

	public TagKey<T> register(String id) {
		return TagKey.create(this.resourceKey, new ResourceLocation(this.modId, id));
	}
}
