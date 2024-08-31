package willatendo.simplelibrary.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import willatendo.simplelibrary.ForgeSimpleLibrary;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.util.Optional;
import java.util.function.Supplier;

public class ForgeHelper implements ModloaderHelper {
    @Override
    public <T> Supplier<EntityDataSerializer<Holder<T>>> registerDataSerializer(String id, StreamCodec<RegistryFriendlyByteBuf, Holder<T>> streamCodec) {
        return ForgeSimpleLibrary.ENTITY_DATA_SERIALIZER.register(id, () -> EntityDataSerializer.forValueType(streamCodec));
    }

    @Override
    public boolean isDevEnviroment() {
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends Entity> EntityType<T> entityTypeBuilder(String name, EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory, boolean noSave, boolean fireImmune, Optional<Block> immuneTo, float width, float height) {
        EntityType.Builder<T> builder = EntityType.Builder.of(entityFactory, mobCategory).sized(width, height);
        if (noSave) {
            builder.noSave();
        }
        if (fireImmune) {
            builder.fireImmune();
        }
        immuneTo.ifPresent(builder::immuneTo);
        return builder.build(name);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return IForgeMenuType.create(extendedMenuSupplier::create);
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        RegistryBuilder<T> registryBuilder = RegistryBuilder.of(resourceKey.location());
        if (!simpleRegistryBuilder.isSynced()) {
            registryBuilder.disableSync();
        }
        DeferredRegister<T> deferredRegister = DeferredRegister.create(resourceKey, resourceKey.location().getNamespace());
        deferredRegister.makeRegistry(() -> registryBuilder);
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(deferredRegister.getRegistryKey().location());
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, properties);
    }

    @Override
    public SimpleParticleType createParticleType(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }

    @Override
    public void openContainer(BlockEntity blockEntity, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu((MenuProvider) blockEntity, blockPos);
    }
}