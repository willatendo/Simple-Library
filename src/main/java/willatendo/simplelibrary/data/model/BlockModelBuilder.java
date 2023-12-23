package willatendo.simplelibrary.data.model;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.data.util.ExistingFileHelper;

public class BlockModelBuilder extends ModelBuilder<BlockModelBuilder> {
	public BlockModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}

	@Override
	public JsonObject toJson() {
		return super.toJson();
	}
}
