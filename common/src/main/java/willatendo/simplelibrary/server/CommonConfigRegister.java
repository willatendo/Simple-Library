package willatendo.simplelibrary.server;

import willatendo.simplelibrary.common.config.ConfigType;
import willatendo.simplelibrary.common.config.SimpleConfig;

import java.util.ArrayList;
import java.util.List;

public final class CommonConfigRegister {
    private static final List<SimpleConfig> COMMON_CONFIGS = new ArrayList<>();

    public static SimpleConfig createCommonConfig(String name) {
        SimpleConfig simpleConfig = new SimpleConfig(name, ConfigType.COMMON);
        COMMON_CONFIGS.add(simpleConfig);
        return simpleConfig;
    }

    public static void registerAll() {
        COMMON_CONFIGS.forEach(simpleConfig -> {
            if (simpleConfig.create()) {
                simpleConfig.write();
            }
            simpleConfig.read();
        });
    }
}
