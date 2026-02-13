package ca.willatendo.simplelibrary.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

@FunctionalInterface
public interface RegisterFunction {
    <T> void register(ResourceKey<? extends Registry<T>> registryKey, Identifier identifier, Supplier<T> value);
}
