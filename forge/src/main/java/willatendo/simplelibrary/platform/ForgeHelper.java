package willatendo.simplelibrary.platform;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willatendo.simplelibrary.ForgeSimpleLibrary;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
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
        Supplier<IForgeRegistry<T>> iForgeRegistry = deferredRegister.makeRegistry(() -> registryBuilder);
        return new Registry<T>() {
            @Override
            public ResourceKey<? extends Registry<T>> key() {
                return iForgeRegistry.get().getRegistryKey();
            }

            @Nullable
            @Override
            public ResourceLocation getKey(T t) {
                return iForgeRegistry.get().getKey(t);
            }

            @Override
            public Optional<ResourceKey<T>> getResourceKey(T t) {
                return Optional.empty();
            }

            @Override
            public int getId(@Nullable T t) {
                return 0;
            }

            @Nullable
            @Override
            public T get(@Nullable ResourceKey<T> resourceKey) {
                return null;
            }

            @Nullable
            @Override
            public T get(@Nullable ResourceLocation resourceLocation) {
                return null;
            }

            @Override
            public Optional<RegistrationInfo> registrationInfo(ResourceKey<T> resourceKey) {
                return Optional.empty();
            }

            @Override
            public Lifecycle registryLifecycle() {
                return null;
            }

            @Override
            public Optional<Holder.Reference<T>> getAny() {
                return Optional.empty();
            }

            @Override
            public Set<ResourceLocation> keySet() {
                return Set.of();
            }

            @Override
            public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
                return Set.of();
            }

            @Override
            public Set<ResourceKey<T>> registryKeySet() {
                return Set.of();
            }

            @Override
            public Optional<Holder.Reference<T>> getRandom(RandomSource randomSource) {
                return Optional.empty();
            }

            @Override
            public boolean containsKey(ResourceLocation resourceLocation) {
                return false;
            }

            @Override
            public boolean containsKey(ResourceKey<T> resourceKey) {
                return false;
            }

            @Override
            public Registry<T> freeze() {
                return null;
            }

            @Override
            public Holder.Reference<T> createIntrusiveHolder(T t) {
                return null;
            }

            @Override
            public Optional<Holder.Reference<T>> getHolder(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Holder.Reference<T>> getHolder(ResourceLocation resourceLocation) {
                return Optional.empty();
            }

            @Override
            public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> resourceKey) {
                return Optional.empty();
            }

            @Override
            public Holder<T> wrapAsHolder(T t) {
                return null;
            }

            @Override
            public Stream<Holder.Reference<T>> holders() {
                return Stream.empty();
            }

            @Override
            public Optional<HolderSet.Named<T>> getTag(TagKey<T> tagKey) {
                return Optional.empty();
            }

            @Override
            public HolderSet.Named<T> getOrCreateTag(TagKey<T> tagKey) {
                return null;
            }

            @Override
            public Stream<Pair<TagKey<T>, HolderSet.Named<T>>> getTags() {
                return Stream.empty();
            }

            @Override
            public Stream<TagKey<T>> getTagNames() {
                return Stream.empty();
            }

            @Override
            public void resetTags() {

            }

            @Override
            public void bindTags(Map<TagKey<T>, List<Holder<T>>> map) {

            }

            @Override
            public HolderOwner<T> holderOwner() {
                return null;
            }

            @Override
            public HolderLookup.RegistryLookup<T> asLookup() {
                return null;
            }

            @Nullable
            @Override
            public T byId(int i) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NotNull
            @Override
            public Iterator<T> iterator() {
                return iForgeRegistry.get().iterator();
            }
        };
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
    public void openContainer(MenuProvider menuProvider, BlockPos blockPos, ServerPlayer serverPlayer) {
        serverPlayer.openMenu(menuProvider, blockPos);
    }
}