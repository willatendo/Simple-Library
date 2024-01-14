package willatendo.simplelibrary.config;

import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import willatendo.simplelibrary.config.api.ModConfigEvents;

public record ModConfigEventsHolder(String modId, Event<ModConfigEvents.Loading> loading, Event<ModConfigEvents.Reloading> reloading, Event<ModConfigEvents.Unloading> unloading) {

	private static final Map<String, ModConfigEventsHolder> MOD_SPECIFIC_EVENT_HOLDERS = Maps.newConcurrentMap();

	public static ModConfigEventsHolder modSpecific(String modId) {
		return MOD_SPECIFIC_EVENT_HOLDERS.computeIfAbsent(modId, ModConfigEventsHolder::create);
	}

	private static ModConfigEventsHolder create(String modId) {
		Event<ModConfigEvents.Loading> loading = EventFactory.createArrayBacked(ModConfigEvents.Loading.class, listeners -> config -> {
			for (ModConfigEvents.Loading event : listeners) {
				event.onModConfigLoading(config);
			}
		});
		Event<ModConfigEvents.Reloading> reloading = EventFactory.createArrayBacked(ModConfigEvents.Reloading.class, listeners -> config -> {
			for (ModConfigEvents.Reloading event : listeners) {
				event.onModConfigReloading(config);
			}
		});
		Event<ModConfigEvents.Unloading> unloading = EventFactory.createArrayBacked(ModConfigEvents.Unloading.class, listeners -> config -> {
			for (ModConfigEvents.Unloading event : listeners) {
				event.onModConfigUnloading(config);
			}
		});
		return new ModConfigEventsHolder(modId, loading, reloading, unloading);
	}
}
