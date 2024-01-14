package willatendo.simplelibrary.server.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;

public interface EntityStruckByLightningCallback {
	void lightning(Entity entity, LightningBolt lightningBolt);
}
