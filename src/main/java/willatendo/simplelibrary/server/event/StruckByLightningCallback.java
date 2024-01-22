package willatendo.simplelibrary.server.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public interface StruckByLightningCallback {
	public static final Event<StruckByLightningCallback> EVENT = EventFactory.createArrayBacked(StruckByLightningCallback.class, callbacks -> (entity, lightningBolt) -> {
		for (StruckByLightningCallback callback : callbacks) {
			if (callback.callback(entity, lightningBolt)) {
				return true;
			}
		}
		return false;
	});

	boolean callback(Entity entity, LightningBolt lightningBolt);
}
