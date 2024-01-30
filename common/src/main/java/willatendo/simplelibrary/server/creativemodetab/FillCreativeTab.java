package willatendo.simplelibrary.server.creativemodetab;

import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
public interface FillCreativeTab {
	void fillCreativeTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output);
}
