package willatendo.simplelibrary.config.api;

import java.util.Objects;

import net.fabricmc.fabric.api.event.Event;
import willatendo.simplelibrary.config.ModConfig;
import willatendo.simplelibrary.config.ModConfigEventsHolder;

public final class ModConfigEvents {
	private ModConfigEvents() {
	}

	public static Event<Loading> loading(String modId) {
		Objects.requireNonNull(modId, "mod id is null");
		return ModConfigEventsHolder.modSpecific(modId).loading();
	}

	public static Event<Reloading> reloading(String modId) {
		Objects.requireNonNull(modId, "mod id is null");
		return ModConfigEventsHolder.modSpecific(modId).reloading();
	}

	public static Event<Unloading> unloading(String modId) {
		Objects.requireNonNull(modId, "mod id is null");
		return ModConfigEventsHolder.modSpecific(modId).unloading();
	}

	@FunctionalInterface
	public interface Loading {
		void onModConfigLoading(ModConfig modConfig);
	}

	@FunctionalInterface
	public interface Reloading {
		void onModConfigReloading(ModConfig modConfig);
	}

	@FunctionalInterface
	public interface Unloading {
		void onModConfigUnloading(ModConfig modConfig);
	}
}
