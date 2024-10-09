package willatendo.simplelibrary.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public record ResourcePackGenerator(DataGenerator dataGenerator, boolean run, String resourcePackName, PackOutput packOutput) {
    public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
        T dataProvider = factory.create(this.packOutput());
        String resourcePackName = this.resourcePackName() + "/" + dataProvider.getName();
        if (!this.dataGenerator().allProviderIds.add(resourcePackName)) {
            throw new IllegalStateException("Duplicate provider: " + resourcePackName);
        } else {
            if (this.run()) {
                this.dataGenerator().providersToRun.put(resourcePackName, dataProvider);
            }
            return dataProvider;
        }
    }
}
