package willatendo.simplelibrary.server.event.registry;

import net.minecraftforge.event.RegisterCommandsEvent;
import willatendo.simplelibrary.server.command.CommandRegisterInformation;

public final class ForgeCommandRegister implements CommandRegister {
    private final RegisterCommandsEvent event;

    public ForgeCommandRegister(RegisterCommandsEvent event) {
        this.event = event;
    }

    @Override
    public void register(CommandRegisterInformation commandRegisterInformation) {
        commandRegisterInformation.register(this.event.getDispatcher(), this.event.getBuildContext(), this.event.getCommandSelection());
    }
}
