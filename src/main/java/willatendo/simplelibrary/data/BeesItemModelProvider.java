package willatendo.simplelibrary.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BeesItemModelProvider extends SimpleItemModelProvider {
	public BeesItemModelProvider(PackOutput packOutput, String modid, ExistingFileHelper existingFileHelper) {
		super(packOutput, modid, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.basicItem(Items.ACACIA_BOAT);
	}
}
