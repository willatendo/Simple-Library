package willatendo.simplelibrary.client.event;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class NeoforgeKeyMappingRegister implements KeyMappingRegister {
    private final RegisterKeyMappingsEvent event;

    public NeoforgeKeyMappingRegister(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public void register(KeyMapping keyMapping) {
        this.event.register(keyMapping);
    }
}
