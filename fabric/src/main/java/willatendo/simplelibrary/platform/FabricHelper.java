package willatendo.simplelibrary.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class FabricHelper implements ModloaderHelper {
	@Override
	public Builder createCreativeModeTab() {
		return FabricItemGroup.builder();
	}
}
