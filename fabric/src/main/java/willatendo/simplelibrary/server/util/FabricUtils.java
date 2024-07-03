package willatendo.simplelibrary.server.util;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FabricUtils {
    public static RecipeBookType createRecipeBookType(String name) {
        return FabricUtils.extend(RecipeBookType.class, name);
    }

    public static RecipeBookCategories createRecipeBookCategories(String name, ItemStack... itemStacks) {
        return FabricUtils.extend(RecipeBookCategories.class, name, () -> itemStacks);
    }

    // Enum Extenders

    public static <C extends Supplier<Object[]>, T extends Enum<T> & ExtendableEnum<C>> T extend(Class<T> cls, String internalName, C params) {
        try {
            T r = callEnumInvoker(cls, internalName, params.get());
            r.init(params);
            return r;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <C extends Supplier<Object[]>, T extends Enum<T> & ExtendableEnum<C>> T extend(Class<T> cls, String internalName) {
        try {
            T r = callEnumInvoker(cls, internalName);
            r.init((C) (Supplier) () -> new Object[]{});
            return r;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Enum<?>> T callEnumInvoker(Class<T> clazz, String internalName, Object... params) throws Throwable {
        List<Object> list = new ArrayList<>(List.of(internalName));
        list.addAll(List.of(params));
        params = list.toArray();

        return (T) FabricUtils.findMethod(clazz, "dark_matter$extendEnum", params).orElseThrow(() -> new IllegalStateException("%s doesn't have a dark_matter$extendEnum method".formatted(clazz.getName()))).invoke(clazz, params);
    }

    private static Optional<Method> findMethod(Class<?> clazz, String name, Object... args) {
        return Optional.ofNullable(FabricUtils.findMethod(clazz, false, name, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)));
    }

    private static <T> Method findMethod(Class<T> clazz, boolean traverse, String name, Class<?>... classes) {
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 1) {
            return FabricUtils.checkMethod(methods[0], name, classes) ? methods[0] : null;
        } else {
            try {
                return clazz.getDeclaredMethod(name, classes);
            } catch (Throwable e) {
                for (Method method : methods) {
                    if (FabricUtils.checkMethod(method, name, classes)) {
                        return method;
                    }
                }
            }
        }
        return traverse && clazz.getSuperclass() != null ? findMethod(clazz.getSuperclass(), true, name, classes) : null;
    }

    private static boolean checkMethod(Method method, String name, Class<?>[] classes) {
        if (!method.getName().equals(name)) {
            return false;
        }
        if (method.getParameterCount() != classes.length) {
            return false;
        }

        Class<?>[] pt = method.getParameterTypes();
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (!ClassUtils.isAssignable(classes[i], pt[i])) {
                return false;
            }
        }
        return true;
    }
}
