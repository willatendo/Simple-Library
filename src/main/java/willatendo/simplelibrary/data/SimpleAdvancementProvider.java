package willatendo.simplelibrary.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class SimpleAdvancementProvider extends AdvancementProvider {
	public SimpleAdvancementProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, List<AdvancementGenerator> subProviders) {
		super(fabricDataOutput, provider, subProviders.stream().map(advancementGencerator -> advancementGencerator.toSubProvider()).toList());
	}

	public interface AdvancementGenerator {
		void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> saver);

		default AdvancementSubProvider toSubProvider() {
			return (provider, saver) -> this.generate(provider, saver);
		}
	}
}
