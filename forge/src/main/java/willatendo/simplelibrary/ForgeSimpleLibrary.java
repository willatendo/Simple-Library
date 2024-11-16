package willatendo.simplelibrary;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import willatendo.simplelibrary.server.entity.SimpleEntityDataSerializers;
import willatendo.simplelibrary.server.event.registry.ForgeSimpleRegistryRegister;
import willatendo.simplelibrary.server.registry.ForgeRegister;
import willatendo.simplelibrary.server.registry.SimpleRegistry;
import willatendo.simplelibrary.server.util.SimpleUtils;

@Mod(SimpleUtils.SIMPLE_ID)
public class ForgeSimpleLibrary {
    public static final SimpleRegistry<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = SimpleRegistry.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SimpleUtils.SIMPLE_ID);

    public ForgeSimpleLibrary() {
        SimpleEntityDataSerializers.init();

        SimpleLibrary.onInitialize(new ForgeSimpleRegistryRegister());
        ForgeRegister.register(FMLJavaModLoadingContext.get().getModEventBus(), ENTITY_DATA_SERIALIZER);
    }
}
