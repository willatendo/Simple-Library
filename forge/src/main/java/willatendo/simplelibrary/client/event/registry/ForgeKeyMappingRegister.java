package willatendo.simplelibrary.client.event.registry;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public final class ForgeKeyMappingRegister implements KeyMappingRegistry {
    private final RegisterKeyMappingsEvent event;

    public ForgeKeyMappingRegister(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public void register(KeyMapping keyMapping) {
        this.event.register(keyMapping);
    }
}
