package willatendo.simplelibrary.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
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
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;
import willatendo.simplelibrary.server.util.SimpleUtils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

public interface ModloaderHelper {
    ModloaderHelper INSTANCE = SimpleUtils.loadModloaderHelper(ModloaderHelper.class);

    // Simple Library ONLY
    <T> Supplier<EntityDataSerializer<Holder<T>>> registerDataSerializer(String id, StreamCodec<RegistryFriendlyByteBuf, Holder<T>> streamCodec);

    // Internal Use

    boolean isDevEnviroment();

    boolean isModLoaded(String modId);

    Path getConfigPath();

    CreativeModeTab.Builder createCreativeModeTab();

    <T extends Entity> EntityType<T> entityTypeBuilder(String name, EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory, boolean noSave, boolean fireImmune, Optional<Block> immuneTo, float width, float height);

    default <T extends Entity> EntityType<T> entityTypeBuilder(String name, EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory, float width, float height) {
        return this.entityTypeBuilder(name, entityFactory, mobCategory, false, false, Optional.empty(), width, height);
    }

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(ExtendedMenuSupplier<T> extendedMenuSupplier);

    <T> Registry<T> createRegistry(ResourceKey<Registry<T>> resourceKey, SimpleRegistryBuilder simpleRegistryBuilder);

    SpawnEggItem createSpawnEgg(Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor, Item.Properties properties);

    SimpleParticleType createParticleType(boolean overrideLimiter);

    void openContainer(BlockEntity blockEntity, BlockPos blockPos, ServerPlayer serverPlayer);
}
