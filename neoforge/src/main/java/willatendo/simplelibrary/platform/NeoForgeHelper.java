package willatendo.simplelibrary.platform;

import net.minecraft.world.item.CreativeModeTab;

public class NeoForgeHelper implements ModloaderHelper {
	@Override
	public CreativeModeTab.Builder createCreativeModeTab() {
		return CreativeModeTab.builder();
	}
}