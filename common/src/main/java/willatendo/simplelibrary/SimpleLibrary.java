package willatendo.simplelibrary;

import willatendo.simplelibrary.server.CommonConfigRegister;

public final class SimpleLibrary {
    public static void onInitialize() {
        CommonConfigRegister.registerAll();
    }
}
