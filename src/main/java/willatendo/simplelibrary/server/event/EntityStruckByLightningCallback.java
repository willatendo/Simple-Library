package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public interface EntityStruckByLightningCallback {
	public static final Event<EntityStruckByLightningCallback> EVENT = EventFactory.createArrayBacked(EntityStruckByLightningCallback.class, callbacks -> (entity, lightningBolt) -> {
		for (EntityStruckByLightningCallback callback : callbacks) {
			callback.callback(entity, lightningBolt);
		}
	});

	void callback(Entity entity, LightningBolt lightningBolt);
}
