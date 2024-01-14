package willatendo.simplelibrary.server.creativemodetab;

import java.util.Map;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import willatendo.simplelibrary.server.registry.SimpleHolder;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

public final class CreativeModeTabUtils {
	public static CreativeModeTab.Builder create(String modId, String id, Supplier<Item> icon, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return FabricItemGroup.builder().title(SimpleUtils.translation(modId, "itemGroup", id)).icon(() -> icon.get().getDefaultInstance()).displayItems(displayItemsGenerator);
	}

	public static CreativeModeTab.DisplayItemsGenerator fillCreativeTab(SimpleRegistry<Item> simpleRegister, SimpleHolder<? extends Item>... exceptions) {
		return fillCreativeTab(simpleRegister, Map.of(), exceptions);
	}

	public static CreativeModeTab.DisplayItemsGenerator fillCreativeTab(SimpleRegistry<Item> simpleRegister, Map<Item, FillCreativeTab> fillLike, SimpleHolder<? extends Item>... exceptions) {
		return (itemDisplayParameters, output) -> {
			for (SimpleHolder<? extends Item> item : simpleRegister.getEntries().stream().filter(item -> !SimpleUtils.toList(exceptions).contains(item)).toList()) {
				if (item.get() instanceof FillCreativeTab fillCreativeTab) {
					fillCreativeTab.fillCreativeTab(itemDisplayParameters, output);
				} else if (fillLike.containsKey(item.get())) {
					fillLike.get(item.get()).fillCreativeTab(itemDisplayParameters, output);
				} else {
					output.accept(item.get());
				}
			}
		};
	}
}
