package willatendo.simplelibrary.data.model;

import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> {
	public ItemModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}
}
