package willatendo.simplelibrary.data;

import net.minecraft.data.PackOutput;

public class BeesLanguageProvider extends SimpleLanguageProvider {
	public BeesLanguageProvider(PackOutput packOutput, String modid, String locale) {
		super(packOutput, modid, locale);
	}

	@Override
	protected void addTranslations() {
		this.add("test", "Hello");
	}
}
