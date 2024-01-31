package willatendo.simplelibrary.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public abstract class SimpleItemModelProvider extends ItemModelProvider {
	public SimpleItemModelProvider(FabricDataOutput fabricDataOutput, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, fabricDataOutput.getModId(), existingFileHelper);
	}

	@Override
	public String getName() {
		return this.modid + ": Item Models";
	}
}
