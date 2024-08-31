package willatendo.simplelibrary.server.event;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public final class ForgeNewRegistryRegister implements NewRegistryRegister {
    private final NewRegistryEvent event;

    public ForgeNewRegistryRegister(NewRegistryEvent event) {
        this.event = event;
    }

    @Override
    public <T> void register(Registry<T> registry, ResourceKey<Registry<T>> name) {
        this.event.create(new RegistryBuilder<>().hasTags().setName(name.location()));
    }
}
