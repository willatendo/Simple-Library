package willatendo.simplelibrary;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class Items {
	public static final SimpleRegistry<Item> ITEMS = 
			SimpleRegistry.create
			(Registries.ITEM, SimpleUtils.SIMPLE_ID);

	public static final SimpleHolder<Item> TEST = ITEMS.register("test", () -> new Item(new Item.Properties()));

	public static void init() {
	}
}
