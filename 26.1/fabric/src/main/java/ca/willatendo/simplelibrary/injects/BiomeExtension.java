package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.biome_modifier.ModifiableBiomeInfo;

public interface BiomeExtension {
    default ModifiableBiomeInfo modifiableBiomeInfo() {
        throw new RuntimeException("This has not been registered correctly! " + this.getClass());
    }
}
