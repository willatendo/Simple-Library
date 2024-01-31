package willatendo.simplelibrary.data.model;

import org.jetbrains.annotations.NotNull;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public abstract class BlockModelProvider extends ModelProvider<BlockModelBuilder> {
	public BlockModelProvider(FabricDataOutput fabricDataOutput, String modId, ExistingFileHelper existingFileHelper) {
		super(fabricDataOutput, modId, BLOCK_FOLDER, BlockModelBuilder::new, existingFileHelper);
	}

	@NotNull
	@Override
	public String getName() {
		return "Block Models: " + this.modId;
	}
}
