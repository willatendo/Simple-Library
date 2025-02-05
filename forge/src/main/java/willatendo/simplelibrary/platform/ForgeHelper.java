package willatendo.simplelibrary.platform;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import willatendo.simplelibrary.server.menu.ExtendedMenuSupplier;
import willatendo.simplelibrary.server.util.Platform;
import willatendo.simplelibrary.server.util.SimpleRegistryBuilder;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForgeHelper implements ModloaderHelper {
    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
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
            public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKey) {
                return iForgeRegistry.get().getDelegate(resourceKey);
            }

            @Override
            public Optional<HolderSet.Named<T>> get(TagKey<T> tagKey) {
                return Optional.empty();
            }

            @Override
            public Stream<Holder.Reference<T>> listElements() {
                return Stream.empty();
            }

            @Override
            public Stream<HolderSet.Named<T>> listTags() {
                return Stream.empty();
            }

            @NotNull
            @Override
            public Iterator<T> iterator() {
                return iForgeRegistry.get().iterator();
            }

            @Override
            public ResourceKey<? extends Registry<T>> key() {
                return iForgeRegistry.get().getRegistryKey();
            }

            @Override
            public Lifecycle registryLifecycle() {
                return Lifecycle.stable();
            }

            @Nullable
            @Override
            public ResourceLocation getKey(T value) {
                return iForgeRegistry.get().getKey(value);
            }

            @Override
            public Optional<ResourceKey<T>> getResourceKey(T value) {
                return iForgeRegistry.get().getResourceKey(value);
            }

            @Override
            public int getId(@Nullable T value) {
                return 0;
            }

            @Nullable
            @Override
            public T byId(int i) {
                return null;
            }

            @Override
            public int size() {
                return iForgeRegistry.get().getValues().size();
            }

            @Nullable
            @Override
            public T getValue(@Nullable ResourceKey<T> resourceKey) {
                return iForgeRegistry.get().getValue(resourceKey.location());
            }

            @Nullable
            @Override
            public T getValue(@Nullable ResourceLocation resourceLocation) {
                return iForgeRegistry.get().getValue(resourceLocation);
            }

            @Override
            public Optional<RegistrationInfo> registrationInfo(ResourceKey<T> resourceKey) {
                return Optional.empty();
            }

            @Override
            public Optional<Holder.Reference<T>> getAny() {
                return Optional.empty();
            }

            @Override
            public Set<ResourceLocation> keySet() {
                return iForgeRegistry.get().getKeys();
            }

            @Override
            public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
                return iForgeRegistry.get().getEntries();
            }

            @Override
            public Set<ResourceKey<T>> registryKeySet() {
                return iForgeRegistry.get().getEntries().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
            }

            @Override
            public Optional<Holder.Reference<T>> getRandom(RandomSource randomSource) {
                return Optional.empty();
            }

            @Override
            public boolean containsKey(ResourceLocation resourceLocation) {
                return iForgeRegistry.get().containsKey(resourceLocation);
            }

            @Override
            public boolean containsKey(ResourceKey<T> resourceKey) {
                return iForgeRegistry.get().containsKey(resourceKey.location());
            }

            @Override
            public Registry<T> freeze() {
                return this;
            }

            @Override
            public Holder.Reference<T> createIntrusiveHolder(T t) {
                return Holder.Reference.createIntrusive(this, t);
            }

            @Override
            public Optional<Holder.Reference<T>> get(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Holder.Reference<T>> get(ResourceLocation resourceLocation) {
                return iForgeRegistry.get().getDelegate(resourceLocation);
            }

            @Override
            public Holder<T> wrapAsHolder(T value) {
                Holder.Reference<T> reference = iForgeRegistry.get().getDelegateOrThrow(value);
                return reference != null ? reference : Holder.direct(value);
            }

            @Override
            public Stream<HolderSet.Named<T>> getTags() {
                return Stream.empty();
            }

            @Override
            public PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> loadResult) {
                return null;
            }
        };
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