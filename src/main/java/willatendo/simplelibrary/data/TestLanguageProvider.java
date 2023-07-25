package willatendo.simplelibrary.data;

import net.minecraft.data.PackOutput;

public class TestLanguageProvider extends SimpleLanguageProvider {
	public TestLanguageProvider(PackOutput packOutput, String modid, String locale) {
		super(packOutput, modid, locale);
	}

	@Override
	protected void addTranslations() {
	}
}
