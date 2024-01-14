package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

// Events Ported from Forge

public class ForgeEvents {
	public static final Event<EntityStruckByLightningCallback> ENTITY_STRUCK_BY_LIGHTNING = EventFactory.createArrayBacked(EntityStruckByLightningCallback.class, listener -> (entity, lightningBolt) -> {
	});
}
