package willatendo.simplelibrary.server.event.registry;

import willatendo.simplelibrary.server.command.CommandRegisterInformation;

public interface CommandRegister {
    void register(CommandRegisterInformation commandRegisterInformation);
}
