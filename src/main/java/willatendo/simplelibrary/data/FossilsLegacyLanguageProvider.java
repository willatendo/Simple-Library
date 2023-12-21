package willatendo.simplelibrary.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class FossilsLegacyLanguageProvider extends SimpleLanguageProvider {
	public FossilsLegacyLanguageProvider(FabricDataOutput fabricDataOutput, String modId, String local) {
		super(fabricDataOutput, modId, local);
	}

	@Override
	protected void addTranslations() {
		this.add("test", "Test");
	}
}
