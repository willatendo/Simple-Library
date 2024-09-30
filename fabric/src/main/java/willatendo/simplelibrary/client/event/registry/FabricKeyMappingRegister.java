package willatendo.simplelibrary.client.event.registry;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public final class FabricKeyMappingRegister implements KeyMappingRegistry {
    @Override
    public void register(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
