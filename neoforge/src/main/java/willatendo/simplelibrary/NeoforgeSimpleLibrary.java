package willatendo.simplelibrary;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import willatendo.simplelibrary.server.entity.SimpleEntityDataSerializers;
import willatendo.simplelibrary.server.event.NeoforgeSimpleRegistryRegister;
import willatendo.simplelibrary.server.registry.NeoForgeRegister;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class NeoforgeSimpleLibrary {
    public static final SimpleRegistry<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = SimpleRegistry.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SimpleUtils.SIMPLE_ID);

    public NeoforgeSimpleLibrary(IEventBus iEventBus) {
        SimpleEntityDataSerializers.init();

        SimpleLibrary.onInitialize(new NeoforgeSimpleRegistryRegister(iEventBus));
        NeoForgeRegister.register(iEventBus, ENTITY_DATA_SERIALIZER);
    }
}