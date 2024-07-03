package willatendo.simplelibrary.server.util;

import java.util.function.Supplier;

public interface ExtendableEnum<C extends Supplier<Object[]>> {
    static <T extends Enum<T> & ExtendableEnum<C>, C extends Supplier<Object[]>> T extend(Class<T> cls, String internalName, C params) {
        return FabricUtils.extend(cls, internalName, params);
    }

    static <T extends Enum<T> & ExtendableEnum<C>, C extends Supplier<Object[]>> T extend(Class<T> cls, String internalName) {
        return FabricUtils.extend(cls, internalName);
    }

    default void init(C args) {
    }
}
