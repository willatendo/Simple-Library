package willatendo.simplelibrary.data;

import java.util.List;

import net.minecraft.data.PackOutput;

public class SimpleTranslationProvider extends SimpleLanguageProvider {
	private final List<String> translations;

	public SimpleTranslationProvider(PackOutput packOutput, String modid, String locale, List<String> translations) {
		super(packOutput, modid, locale);
		this.translations = translations;
	}

	@Override
	protected void addTranslations() {
		for (int i = 0; i < this.getTranslationData().size(); i++) {
			this.add(this.getTranslationData().keySet().stream().toList().get(i), this.translations.get(i));
		}
	}
}
