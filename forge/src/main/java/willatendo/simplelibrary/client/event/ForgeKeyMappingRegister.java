package willatendo.simplelibrary.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class ForgeKeyMappingRegister implements KeyMappingRegister {
    private final RegisterKeyMappingsEvent event;

    public ForgeKeyMappingRegister(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public void register(KeyMapping keyMapping) {
        this.event.register(keyMapping);
    }
}
