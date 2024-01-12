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
	private final String modId;

	public SimpleAdvancementProvider(FabricDataOutput fabricDataOutput, CompletableFuture<HolderLookup.Provider> provider, List<AdvancementGenerator> subProviders) {
		super(fabricDataOutput, provider, subProviders.stream().map(advancementGenerator -> advancementGenerator.toSubProvider()).toList());
		this.modId = fabricDataOutput.getModId();
	}

	@Override
	public String getName() {
		return this.modId + ": Advancements";
	}

	public interface AdvancementGenerator {
		void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> saver);

		default AdvancementSubProvider toSubProvider() {
			return (provider, saver) -> this.generate(provider, saver);
		}
	}
}
