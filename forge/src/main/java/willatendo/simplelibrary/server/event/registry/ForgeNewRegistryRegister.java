package willatendo.simplelibrary.server.event.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class ForgeNewRegistryRegister implements NewRegistryRegister {
    private final NewRegistryEvent event;
    private Supplier<IForgeRegistry<?>> iForgeRegistrySupplier;

    public ForgeNewRegistryRegister(NewRegistryEvent event) {
        this.event = event;
    }

    public Supplier<IForgeRegistry<?>> getiForgeRegistrySupplier() {
        return this.iForgeRegistrySupplier;
    }

    @Override
    public <T> void register(Registry<T> registry, ResourceKey<Registry<T>> name) {
        this.iForgeRegistrySupplier = (Supplier) this.event.create(new RegistryBuilder<>().hasTags().setName(name.location()));
    }
}
