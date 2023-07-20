package willatendo.simplelibrary.data;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class SimpleTranslationProvider implements DataProvider {
	private final Map<String, String> data = new TreeMap<>();
	private final List<String> translations;
	private final SimpleLanguageProvider base;
	private final PackOutput packOutput;
	private final String modid;
	private final String locale;

	public SimpleTranslationProvider(PackOutput packOutput, String modid, String locale, SimpleLanguageProvider base, List<String> translations) {
		this.packOutput = packOutput;
		this.translations = translations;
		this.base = base;
		this.modid = modid;
		this.locale = locale;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		this.addTranslations();

		if (!data.isEmpty())
			return this.save(cachedOutput, this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));

		return CompletableFuture.allOf();
	}

	private CompletableFuture<?> save(CachedOutput cachedOutput, Path path) {
		JsonObject json = new JsonObject();
		this.data.forEach(json::addProperty);
		return DataProvider.saveStable(cachedOutput, json, path);
	}

	protected void addTranslations() {
		for (int i = 0; i < this.base.getTranslationData().size(); i++) {
			this.add(this.base.getTranslationData().keySet().stream().toList().get(i), this.translations.get(i));
		}
	}

	public void add(String key, String value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate translation key " + key);
	}

	@Override
	public String getName() {
		return this.modid + ": Translation Provider (" + this.locale + ")";
	}
}
