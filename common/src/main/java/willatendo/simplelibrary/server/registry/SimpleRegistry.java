package willatendo.simplelibrary.server.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import willatendo.simplelibrary.server.event.registry.RegisterRegister;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleRegistry<T> {
    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final String modId;

    private final Map<SimpleHolder<? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private final Set<SimpleHolder<? extends T>> entriesView = Collections.unmodifiableSet(this.entries.keySet());

    public static <T> SimpleRegistry<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new SimpleRegistry<>(registryKey, modId);
    }

    public static ParticleRegistry createParticle(String modId) {
        return new ParticleRegistry(modId);
    }

    public static ItemRegistry createItem(String modId) {
        return new ItemRegistry(modId);
    }

    public static BlockRegistry createBlock(String modId) {
        return new BlockRegistry(modId);
    }

    public static MenuTypeRegistry createMenuType(String modId) {
        return new MenuTypeRegistry(modId);
    }

    public static RecipeBookCategoryRegistry createRecipeBookCategory(String modId) {
        return new RecipeBookCategoryRegistry(modId);
    }

    public static VillagerProfessionRegistry createVillagerProfession(String modId) {
        return new VillagerProfessionRegistry(modId);
    }

    public static EntityTypeRegistry createEntityType(String modId) {
        return new EntityTypeRegistry(modId);
    }

    protected SimpleRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = Objects.requireNonNull(registryKey);
        this.modId = Objects.requireNonNull(modId);
    }

    public <I extends T> SimpleHolder<I> register(String id, Supplier<I> value) {
        return this.register(id, key -> value.get());
    }

    public <I extends T> SimpleHolder<I> register(String id, Function<ResourceLocation, I> func) {
        ResourceLocation valueId = ResourceLocation.fromNamespaceAndPath(this.modId, id);
        SimpleHolder<I> simpleHolder = this.createHolder(this.registryKey, valueId);

        if (this.entries.putIfAbsent(simpleHolder, () -> func.apply(valueId)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + id);
        }

        return simpleHolder;
    }

    public void addEntries(RegisterRegister registerRegister) {
        if (!registerRegister.sameRegistryKey(this.registryKey)) {
            return;
        }
        for (Map.Entry<SimpleHolder<? extends T>, Supplier<? extends T>> entry : entries.entrySet()) {
            registerRegister.register(this.registryKey, entry.getKey().getId(), () -> entry.getValue().get());
            entry.getKey().bind(false);
        }
    }

    protected <I extends T> SimpleHolder<I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation valueId) {
        return (SimpleHolder<I>) SimpleHolder.create(registryKey, valueId);
    }

    public Map<SimpleHolder<? extends T>, Supplier<? extends T>> getEntries() {
        return this.entries;
    }

    public Set<SimpleHolder<? extends T>> getEntriesView() {
        return this.entriesView;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }
}
