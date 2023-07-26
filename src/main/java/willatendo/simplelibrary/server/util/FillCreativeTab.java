package willatendo.simplelibrary.server.util;

import net.minecraft.world.item.CreativeModeTab;

/*
 * Implement on an item to change how it will be filled in a {@link CreativeModeTab}
 * 
 * @author Willatendo
 */
public interface FillCreativeTab {
	/*
	 * 
	 */
	void fillCreativeTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output);
}
