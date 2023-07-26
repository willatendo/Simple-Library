package willatendo.simplelibrary.server.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/*
 * Used to create a {@link TagKey} similar to a {@link DeferredRegister}
 * 
 * @author Willatendo
 */
public class TagRegister<T> {
	private final ResourceKey<? extends Registry<T>> resourceKey;
	private final String modId;

	public TagRegister(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
		this.resourceKey = resourceKey;
		this.modId = modId;
	}

	public TagKey<T> register(String id) {
		return TagKey.create(this.resourceKey, new ResourceLocation(this.modId, id));
	}
}
