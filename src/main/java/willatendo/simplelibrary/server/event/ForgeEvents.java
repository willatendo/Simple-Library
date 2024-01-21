package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ForgeEvents {
	public static final Event<EntityStruckByLightningCallback> ENTITY_STRUCK_BY_LIGHTNING = EventFactory.createArrayBacked(EntityStruckByLightningCallback.class, callbacks -> (entity, lightningBolt) -> {
		for (EntityStruckByLightningCallback callback : callbacks) {
			if (callback.lightning(entity, lightningBolt)) {
				return true;
			}
		}
		return false;
	});
}
