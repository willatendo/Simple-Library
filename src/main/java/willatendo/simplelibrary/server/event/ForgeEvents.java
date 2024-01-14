package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

// Events Ported from Forge

public class ForgeEvents {
	public static final Event<EntityStruckByLightningCallback> ENTITY_STRUCK_BY_LIGHTNING = EventFactory.createArrayBacked(EntityStruckByLightningCallback.class, listeners -> (entity, lightningBolt) -> {
		for (EntityStruckByLightningCallback callbacks : listeners) {
			if (callbacks.lightning(entity, lightningBolt)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	});
}
