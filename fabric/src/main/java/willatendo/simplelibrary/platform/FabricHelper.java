package willatendo.simplelibrary.platform;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

public class FabricHelper implements ModloaderHelper {
    @Override
    public <T> Supplier<EntityDataSerializer<Holder<T>>> registerDataSerializer(String id, StreamCodec<RegistryFriendlyByteBuf, Holder<T>> streamCodec) {
        EntityDataSerializer<Holder<T>> entityDataSerializer = EntityDataSerializer.forValueType(streamCodec);
        EntityDataSerializers.registerSerializer(entityDataSerializer);
        return () -> entityDataSerializer;
    }

    @Override
    public boolean isDevEnviroment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Builder createCreativeModeTab() {
        return FabricItemGroup.builder();
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
        return builder.build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier) {
        return new ExtendedScreenHandlerType<T, BlockPos>(extendedMenuSupplier::create, BlockPos.STREAM_CODEC.cast());
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder) {
        FabricRegistryBuilder<T, MappedRegistry<T>> fabricRegistryBuilder = FabricRegistryBuilder.createSimple(resourceKey);
        if (simpleRegistryBuilder.isSynced()) {
            fabricRegistryBuilder.attribute(RegistryAttribute.SYNCED);
        }
        return fabricRegistryBuilder.buildAndRegister();
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), primaryColor, secondaryColor, properties);
    }

    @Override
    public SimpleParticleType createParticleType(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

    @Override
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new ExtendedScreenHandlerFactory<BlockPos>() {
            @Override
            public BlockPos getScreenOpeningData(ServerPlayer serverPlayer1) {
                if (menuProvider instanceof BlockEntity blockEntity) {
                    return blockEntity.getBlockPos();
                } else {
                    return blockPos;
                }
            }

            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                return menuProvider.createMenu(windowId, inventory, player);
            }
        });
    }
}
