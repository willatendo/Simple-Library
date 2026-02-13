package ca.willatendo.simplelibrary.mixin;


import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor<T> {
    @Accessor("registrationInfos")
    Map<ResourceKey<T>, RegistrationInfo> simpleLibrary$getRegistrationInfos();
}