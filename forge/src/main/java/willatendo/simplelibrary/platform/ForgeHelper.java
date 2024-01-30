package willatendo.simplelibrary.platform;

import net.minecraft.world.item.CreativeModeTab;

public class ForgeHelper implements ModloaderHelper {
	@Override
	public CreativeModeTab.Builder createCreativeModeTab() {
		return CreativeModeTab.builder();
	}
}
