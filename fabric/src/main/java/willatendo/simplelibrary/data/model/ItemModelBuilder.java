package willatendo.simplelibrary.data.model;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;

public class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> {
	public ItemModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}
}
