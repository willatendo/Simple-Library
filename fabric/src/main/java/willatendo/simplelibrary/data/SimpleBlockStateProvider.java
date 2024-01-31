package willatendo.simplelibrary.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public abstract class SimpleBlockStateProvider extends BlockStateProvider {
	protected final String modid;

	public SimpleBlockStateProvider(FabricDataOutput fabricDataOutput, ExistingFileHelper exFileHelper) {
		super(fabricDataOutput, fabricDataOutput.getModId(), exFileHelper);
		this.modid = fabricDataOutput.getModId();
	}

	@Override
	public String getName() {
		return this.modid + ": Block States";
	}
}
