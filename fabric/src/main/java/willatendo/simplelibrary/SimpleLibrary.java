package willatendo.simplelibrary;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import willatendo.simplelibrary.server.registry.FabricRegister;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

public class SimpleLibrary implements ModInitializer {
	@Override
	public void onInitialize() {
		SimpleRegistry<Item> items = SimpleRegistry.create(Registries.ITEM, SimpleUtils.SIMPLE_ID);
		items.register("test", () -> new Item(new Item.Properties()));
		FabricRegister.register(items);
	}
}
