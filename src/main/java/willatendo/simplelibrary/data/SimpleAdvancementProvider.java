package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class SimpleAdvancementProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<Provider> provider;
	private final String id;
	private final ExistingFileHelper existingFileHelper;

	public SimpleAdvancementProvider(PackOutput output, CompletableFuture<Provider> provider, String id, ExistingFileHelper existingFileHelper) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, id);
		this.provider = provider;
		this.id = id;
		this.existingFileHelper = existingFileHelper;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return this.provider.thenCompose((provider) -> {
			Set<ResourceLocation> set = new HashSet<>();
			List<CompletableFuture<?>> completableFuture = new ArrayList<>();
			Consumer<Advancement> consumer = (advancement) -> {
				if (!set.add(advancement.getId())) {
					throw new IllegalStateException("Duplicate advancement " + advancement.getId());
				} else {
					Path path = this.pathProvider.json(advancement.getId());
					completableFuture.add(DataProvider.saveStable(cachedOutput, advancement.deconstruct().serializeToJson(), path));
				}
			};

			this.generate(provider, consumer, this.existingFileHelper);

			return CompletableFuture.allOf(completableFuture.toArray((array) -> {
				return new CompletableFuture[array];
			}));
		});
	}

	public abstract void generate(HolderLookup.Provider provider, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper);

	public Advancement advancement(String id, Consumer<Advancement> saver) {
		return Advancement.Builder.advancement().save(saver, new ResourceLocation(this.id, id), existingFileHelper);
	}

	@Override
	public String getName() {
		return this.id + ": Advancments";
	}
}
