package ca.willatendo.simplelibrary.injects;

import ca.willatendo.simplelibrary.server.biome_modifier.ModifiableBiomeInfo;

public interface BiomeAccessor {
    default ModifiableBiomeInfo modifiableBiomeInfo() {
        return null;
    }
}
