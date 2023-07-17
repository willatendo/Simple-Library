package willatendo.simplelibrary.data;

import java.util.List;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class SimpleTranslationProvider extends LanguageProvider {
	private final List<String> translations;
	private final SimpleLanguageProvider base;

	public SimpleTranslationProvider(PackOutput packOutput, String modid, SimpleLanguageProvider base, String locale, List<String> translations) {
		super(packOutput, modid, locale);
		this.translations = translations;
		this.base = base;
	}

	@Override
	protected void addTranslations() {
		for (int i = 0; i < this.base.getTranslationData().size(); i++) {
			this.add(this.base.getTranslationData().keySet().stream().toList().get(i), this.translations.get(i));
		}
	}
}
