package ca.willatendo.simplelibrary.core.registry.sub;

import ca.willatendo.simplelibrary.core.holder.SimpleHolder;
import ca.willatendo.simplelibrary.core.registry.SimpleRegistry;
import ca.willatendo.simplelibrary.core.utils.CoreUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class SoundEventsSubRegistry extends SimpleRegistry<SoundEvent> {
    public SoundEventsSubRegistry(String modId) {
        super(Registries.SOUND_EVENT, modId);
    }

    public SimpleHolder<SoundEvent> registerVariableRange(String name) {
        return this.register(name, () -> SoundEvent.createVariableRangeEvent(CoreUtils.resource(this.modId, name)));
    }
}
