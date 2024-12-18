package willatendo.simplelibrary.client;

import com.google.common.collect.Lists;
import willatendo.simplelibrary.common.config.ConfigType;
import willatendo.simplelibrary.common.config.SimpleConfig;

import java.util.List;

public final class ClientConfigRegister {
    private static final List<SimpleConfig> CLIENT_CONFIGS = Lists.newArrayList();

    public static SimpleConfig createClientConfig(String name) {
        SimpleConfig simpleConfig = new SimpleConfig(name, ConfigType.CLIENT);
        CLIENT_CONFIGS.add(simpleConfig);
        return simpleConfig;
    }

    public static void registerAll() {
        CLIENT_CONFIGS.forEach(simpleConfig -> {
            if (simpleConfig.create()) {
                simpleConfig.write();
            }
            simpleConfig.read();
        });
    }
}
