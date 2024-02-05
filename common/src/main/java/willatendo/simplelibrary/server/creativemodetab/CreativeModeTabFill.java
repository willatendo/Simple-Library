package willatendo.simplelibrary.server.creativemodetab;

import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
public interface CreativeModeTabFill {
	void fill(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output);
}
