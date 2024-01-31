package willatendo.simplelibrary.data.model;

import com.google.gson.JsonObject;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;

public class BlockModelBuilder extends ModelBuilder<BlockModelBuilder> {
	public BlockModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}

	@Override
	public JsonObject toJson() {
		return super.toJson();
	}
}
