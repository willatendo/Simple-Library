package willatendo.simplelibrary.server.event.registry;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import willatendo.simplelibrary.server.command.CommandRegisterInformation;

public final class FabricCommandRegister implements CommandRegister {
    private final Event<CommandRegistrationCallback> event = CommandRegistrationCallback.EVENT;

    @Override
    public void register(CommandRegisterInformation commandRegisterInformation) {
        this.event.register((commandRegisterInformation::register));
    }
}
