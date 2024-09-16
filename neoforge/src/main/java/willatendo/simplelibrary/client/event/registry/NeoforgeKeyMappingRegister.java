package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public final class NeoforgeKeyMappingRegister implements KeyMappingRegister {
    private final RegisterKeyMappingsEvent event;

    public NeoforgeKeyMappingRegister(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public void register(KeyMapping keyMapping) {
        this.event.register(keyMapping);
    }
}
